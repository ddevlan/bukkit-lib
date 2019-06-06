package me.ohvalsgod.bukkitlib.database.redis.listener;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.ohvalsgod.bukkitlib.database.redis.packet.Packet;

import java.lang.reflect.Method;

@Getter
@AllArgsConstructor
public class PacketListenerData {

    private Object instance;
    private Method method;
    private Class packetClass;

    public boolean matches(Packet packet) {
        return this.packetClass == packet.getClass();
    }
}
