package me.ohvalsgod.bukkitlib.command;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import lombok.Getter;
import me.ohvalsgod.bukkitlib.BukkitLib;
import me.ohvalsgod.bukkitlib.command.param.Parameter;
import me.ohvalsgod.bukkitlib.command.param.ParameterData;
import me.ohvalsgod.bukkitlib.command.param.ParameterType;
import me.ohvalsgod.bukkitlib.command.param.defaults.*;
import me.ohvalsgod.bukkitlib.util.ClassUtils;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CommandHandler implements Listener {

    @Getter
    private static List<CommandData> commands = new ArrayList<>();
    private static Map<Class<?>, ParameterType> parameterTypes = new HashMap<>();
    private static boolean initiated = false;

    // Static class -- cannot be created.
    private CommandHandler() {
    }

    /**
     * Initiates the command handler.
     * This can only be called once, and is called automatically when Core enables.
     */
    public static void init() {
        BukkitLib.getLibrary().getLogger().info("Initializing command handler...");
        // Only allow the CoreCommandHandler to be initiated once.
        // Note the '!' in the .checkState call.
        Preconditions.checkState(!initiated);
        initiated = true;

        BukkitLib.getLibrary().getServer().getPluginManager().registerEvents(new CommandHandler(), BukkitLib.getLibrary());

        // Run this on a delay so everything is registered.
        // Not really needed, but it's nice to play it safe.
        new BukkitRunnable() {

            public void run() {
                try {
                    // Command map field (we have to use reflection to get this)
                    Field commandMapField = BukkitLib.getLibrary().getServer().getClass().getDeclaredField("commandMap");
                    commandMapField.setAccessible(true);

                    Object oldCommandMap = commandMapField.get(BukkitLib.getLibrary().getServer());
                    CommandMap newCommandMap = new CommandMap(BukkitLib.getLibrary().getServer());

                    // Start copying the knownCommands field over
                    // (so any commands registered before we hook in are kept)
                    Field knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
                    knownCommandsField.setAccessible(true);

                    knownCommandsField.set(newCommandMap, knownCommandsField.get(oldCommandMap));
                    // End coping the knownCommands field over

                    commandMapField.set(BukkitLib.getLibrary().getServer(), newCommandMap);
                } catch (Exception e) {
                    // Shouldn't happen, so we can just
                    // printout the exception (and do nothing else)
                    e.printStackTrace();
                }
            }

        }.runTaskLater(BukkitLib.getLibrary(), 5L);

        // Register our default parameter types.
        // boolean.class is the same as Boolean.TYPE,
        // however using .class improves readability.
        registerParameterType(UUID.class, new UUIDParameterType());
        registerParameterType(boolean.class, new BooleanParameterType());
        registerParameterType(float.class, new FloatParameterType());
        registerParameterType(double.class, new DoubleParameterType());
        registerParameterType(int.class, new IntegerParameterType());
        registerParameterType(OfflinePlayer.class, new OfflinePlayerParameterType());
        registerParameterType(Player.class, new PlayerParameterType());
        registerParameterType(World.class, new WorldParameterType());
        registerParameterType(ItemStack.class, new ItemStackParameterType());

        CommandHandler.loadCommandsFromPackage(BukkitLib.getLibrary(), "me.ohvalsgod.bukkitlib.command.commands");
        BukkitLib.getLibrary().getLogger().info("Command handler initialized");
    }

    /**
     * Loads all commands from the given package into the command handler.
     *
     * @param plugin      The plugin responsible for these commands. This is here
     *                    because the .getClassesInPackage method requires it (for no real reason)
     * @param packageName The package to load commands from. Example: "net.frozenorb.mshared.commands"
     */
    public static void loadCommandsFromPackage(Plugin plugin, String packageName) {
        long start = System.currentTimeMillis();
        int i = 0;
        BukkitLib.getLibrary().getLogger().info("Loading commands from '" + plugin.getName() + "'...");
        for (Class<?> clazz : ClassUtils.getClassesInPackage(plugin, packageName)) {
            registerClass(clazz);
            i++;
        }
        BukkitLib.getLibrary().getLogger().info("Loaded " + i + " commands from '" + plugin.getName() + "' in " + (System.currentTimeMillis() - start) + "ms.");
    }

    /**
     * Register a custom parameter adapter.
     *
     * @param transforms    The class this parameter type will return (IE KOTH.class, Player.class, etc.)
     * @param parameterType The ParameterType object which will perform the transformation.
     */
    public static void registerParameterType(Class<?> transforms, ParameterType parameterType) {
        parameterTypes.put(transforms, parameterType);
    }

    /**
     * Registers a single class with the command handler.
     *
     * @param registeredClass The class to scan/register.
     */
    protected static void registerClass(Class<?> registeredClass) {
        for (Method method : registeredClass.getMethods()) {
            if (method.getAnnotation(Command.class) != null) {
                registerMethod(method);
            }
        }
    }

    /**
     * Registers a single method with the command handler.
     *
     * @param method The method to register (if applicable)
     */
    protected static void registerMethod(Method method) {
        Command commandAnnotation = method.getAnnotation(Command.class);
        List<ParameterData> parameterData = new ArrayList<>();

        if (!Modifier.isStatic(method.getModifiers())) {
            BukkitLib.getLibrary().getLogger().log(Level.SEVERE, "Method '" + method.getName() + "' cannot be registered as a command because it is not static.");
            return;
        }

        // Offset of 1 here for the sender parameter.
        for (int parameterIndex = 1; parameterIndex < method.getParameterTypes().length; parameterIndex++) {
            Parameter parameterAnnotation = null;

            for (Annotation annotation : method.getParameterAnnotations()[parameterIndex]) {
                if (annotation instanceof Parameter) {
                    parameterAnnotation = (Parameter) annotation;
                    break;
                }
            }

            if (parameterAnnotation != null) {
                parameterData.add(new ParameterData(parameterAnnotation, method.getParameterTypes()[parameterIndex]));
            } else {
                BukkitLib.getLibrary().getLogger().warning("Method '" + method.getName() + "' has a parameter without a @Parameter annotation.");
                return;
            }
        }

        commands.add(new CommandData(commandAnnotation, parameterData, method, method.getParameterTypes()[0].isAssignableFrom(Player.class)));

        // We sort here so to ensure that our commands are matched properly.
        // The way we process commands (see onCommandPreProcess) requires the commands list
        // be sorted by the length of the commands.
        // It's easier (and more efficient) to do that sort here than on command.
        Collections.sort(commands, new Comparator<CommandData>() {

            @Override
            public int compare(CommandData o1, CommandData o2) {
                return (o2.getName().length() - o1.getName().length());
            }

        });
    }

    /**
     * @return the full command line input of a player before running or tab completing a Core command
     */
    public static String[] getParameters(Player player) {
        return CommandMap.parameters.get(player.getUniqueId());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    // Allow command cancellation; this was an issue on KitPvP
    public void onCommandPreProcess(PlayerCommandPreprocessEvent event) {
        // The substring is to chop off the '/' that Bukkit gives us here.
        String command = event.getMessage().substring(1);

        CommandMap.parameters.put(event.getPlayer().getUniqueId(), command.split(" "));

        if (evalCommand(event.getPlayer(), command) != null) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onConsoleCommand(ServerCommandEvent event) {
        if (evalCommand(event.getSender(), event.getCommand()) != null) {
            event.setCancelled(true);
        }

    }

    /**
     * Process a command (permission checks, argument validation, etc.)
     *
     * @param sender  The CommandSender executing this command.
     *                It should be noted that any non-player sender is treated with full permissions.
     * @param command The command to process (without a prepended '/')
     * @return The Command executed
     */
    public static CommandData evalCommand(final CommandSender sender, String command) {
        String[] args = new String[]{};
        CommandData found = null;

        CommandLoop:
        for (CommandData commandData : commands) {
            for (String alias : commandData.getNames()) {
                String messageString = command.toLowerCase() + " "; // Add a space.
                String aliasString = alias.toLowerCase() + " "; // Add a space.
                // The space is added so '/pluginslol' doesn't match '/plugins'

                if (messageString.startsWith(aliasString)) {
                    found = commandData;

                    if (messageString.length() > aliasString.length()) {
                        if (found.getParameters().size() == 0) {
                            continue;
                        }
                    }

                    // If there's 'space' after the command, parse args.
                    // The +1 is there to account for a space after the command if there's parameters
                    if (command.length() > alias.length() + 1) {
                        // See above as to... why this works.
                        List<String> argsList = new ArrayList<>();
                        Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(command.substring(alias.length() + 1));
                        while (m.find()) {
                            argsList.add(m.group().replace("\"", ""));
                        }

                        args = argsList.toArray(new String[0]);
                    }

                    // We break to the command loop as we have 2 for loops here.
                    break CommandLoop;
                }
            }
        }

        if (found == null) {
            return (null);
        }

        if (!(sender instanceof Player) && !found.isConsoleAllowed()) {
            sender.sendMessage(ChatColor.RED + "This command does not support execution from the console.");
            return (found);
        }

        if (!found.canAccess(sender)) {
            sender.sendMessage(ChatColor.RED + "No permission.");
            return (found);
        }

        if (found.isAsync()) {
            final CommandData foundClone = found;
            final String[] argsClone = args;

            new BukkitRunnable() {

                public void run() {
                    foundClone.execute(sender, argsClone);
                }

            }.runTaskAsynchronously(BukkitLib.getLibrary());
        } else {
            found.execute(sender, args);
        }

        return (found);
    }

    /**
     * Transforms a parameter.
     *
     * @param sender      The CommandSender executing the command (or whoever we should transform 'for')
     * @param parameter   The String to transform ('' if none)
     * @param transformTo The class we should use to fetch our ParameterType (which we delegate transforming down to)
     * @return The Object that we've transformed the parameter to.
     */
    protected static Object transformParameter(CommandSender sender, String parameter, Class<?> transformTo) {
        // Special-case Strings as they never need transforming.
        if (transformTo.equals(String.class)) {
            return (parameter);
        }

        // This will throw a NullPointerException if there's no registered
        // parameter type, but that's fine -- as that's what we'd do anyway.
        return (parameterTypes.get(transformTo).transform(sender, parameter));
    }

    /**
     * Tab completes a parameter.
     *
     * @param sender           The Player tab completing the command (not CommandSender as tab completion is for players only)
     * @param parameter        The last thing the player typed in their chat box before hitting tab ('' if none)
     * @param transformTo      The class we should use to fetch our ParameterType (which we delegate tab completing down to)
     * @param tabCompleteFlags The list of custom flags to use when tab completing this parameter.
     * @return A List<String> of available tab completions. (empty if none)
     */
    protected static List<String> tabCompleteParameter(Player sender, String parameter, Class<?> transformTo, String[] tabCompleteFlags) {
        if (!parameterTypes.containsKey(transformTo)) {
            return (new ArrayList<>());
        }

        return (parameterTypes.get(transformTo).tabComplete(sender, ImmutableSet.copyOf(tabCompleteFlags), parameter));
    }

}