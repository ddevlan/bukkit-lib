package me.ohvalsgod.bukkitlib.database.redis;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ohvalsgod.bukkitlib.database.redis.handler.IncomingPacketHandler;
import me.ohvalsgod.bukkitlib.database.redis.listener.PacketListener;
import me.ohvalsgod.bukkitlib.database.redis.listener.PacketListenerData;
import me.ohvalsgod.bukkitlib.database.redis.packet.Packet;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;

@Getter
@NoArgsConstructor
public class RedisImpl {


    private JavaPlugin plugin;
    private static JsonParser jsonParser = new JsonParser();

    private JedisPool jedisPool;
    private JedisPubSub jedisPubSub;
    private List<PacketListenerData> packetListeners;
    private Map<Integer, Class<Packet>> idToType;
    private Map<Class<Packet>, Integer> typeToId;
    private String database;

    public RedisImpl(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void connect() {
        String host = plugin.getConfig().getString("redis.host");
        String database = plugin.getConfig().getString("redis.database");
        String password = plugin.getConfig().getString("redis.password");
        int port = plugin.getConfig().getInt("redis.port");
        boolean auth = plugin.getConfig().getBoolean("redis.authenticate");

        this.jedisPool = new JedisPool(host, port);

        if (auth) {
            try (Jedis jedis = this.jedisPool.getResource()) {
                jedis.auth(password);
            }
        }

        this.packetListeners = new ArrayList<>();
        this.idToType = new HashMap<>();
        this.typeToId = new HashMap<>();
        this.jedisPool = new JedisPool(host, port);
        this.database = database;

        setup();
    }

    public void close() {
        try {
            jedisPool.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendPacket(Packet packet) {
        try {
            JsonObject object = packet.serialize();

            if (object == null) {
                throw new IllegalStateException("Packet cannot generate null serialized data");
            }

            try (Jedis jedis = this.jedisPool.getResource()) {
                jedis.publish(database, packet.id() + ";" + object.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Packet buildPacket(int id) {
        if (!this.idToType.containsKey(id)) {
            throw new IllegalStateException("A packet with ID " + id + " does not exist!");
        }

        try {
            return this.idToType.get(id).newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            throw new IllegalStateException("Could not create instance of packet type " + idToType.get(id).getName());
        }
    }

    public void registerPacket(Class<Packet> clazz) {
        try {
            int id = (int) clazz.getDeclaredMethod("id", new Class[0]).invoke(clazz);
            if (this.idToType.containsKey(id) || this.typeToId.containsKey(clazz)) {
                throw new IllegalStateException("A packet with ID " + id + " has already been registered!");
            }
            this.idToType.put(id, clazz);
            this.typeToId.put(clazz, id);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void registerPacketListener(PacketListener packetListener) {
        for (Method method : packetListener.getClass().getDeclaredMethods()) {
            if (method.getDeclaredAnnotation(IncomingPacketHandler.class) != null) {
                Class packetClass = null;

                if (method.getParameters().length > 0 && Packet.class.isAssignableFrom(method.getParameters()[0].getType())) {
                    packetClass = method.getParameters()[0].getType();
                }

                if (packetClass != null) {
                    this.packetListeners.add(new PacketListenerData(packetClass, method, packetClass));
                }
            }
        }
    }

    private void setup() {
        this.jedisPubSub = new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                if (channel.equalsIgnoreCase(database)) {
                    try {
                        String[] args = message.split(";");
                        int id = Integer.parseInt(args[0]);
                        Packet packet = buildPacket(id);
                        if (packet != null) {
                            packet.deserialize(jsonParser.parse(args[1]).getAsJsonObject());
                            for (PacketListenerData data : packetListeners) {
                                if (data.matches(packet)) {
                                    data.getMethod().invoke(data.getInstance(), packet);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        ForkJoinPool.commonPool().execute(() -> {

            try (Jedis jedis = this.jedisPool.getResource()) {
                jedis.subscribe(this.jedisPubSub, this.database);
            }
        });
    }

}
