package me.ohvalsgod.bukkitlib.command.param.defaults;

import com.google.common.collect.ImmutableList;
import me.ohvalsgod.bukkitlib.command.param.ParameterType;
import me.ohvalsgod.bukkitlib.util.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

public class ItemStackParameterType implements ParameterType<ItemStack> {

    @Override
    public ItemStack transform(CommandSender sender, String source) {
        ItemStack item = ItemUtils.get(source);

        if (item == null) {
            sender.sendMessage(ChatColor.RED + "No item with the name " + source + " found.");
            return null;
        }

        return item;
    }

    @Override
    public List<String> tabComplete(Player sender, Set<String> flags, String source) {
        return ImmutableList.of(); // it would probably be too intensive to go through all the aliases
    }

}
