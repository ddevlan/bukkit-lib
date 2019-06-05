package me.ohvalsgod.bklib.item;

import lombok.AllArgsConstructor;
import me.ohvalsgod.bklib.util.Callback;
import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagString;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class ExecutableNBTTag extends NBTTagString {
    private Callback<Player> callback;

    public void execute(Player player) {
        callback.callback(player);
    }

    @Override
    public NBTBase clone() {
        return new ExecutableNBTTag(callback);
    }
}