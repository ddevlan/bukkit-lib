package me.ohvalsgod.bukkitlib.menu.buttons;

import lombok.AllArgsConstructor;
import me.ohvalsgod.bukkitlib.menu.Button;
import me.ohvalsgod.bukkitlib.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class BackButton extends Button {
    private Menu back;

    @Override
    public Material getMaterial(Player player) {
        return Material.BED;
    }

    @Override
    public byte getDamageValue(Player player) {
        return 0;
    }

    @Override
    public String getName(Player player) {
        return "§cGo back";
    }

    @Override
    public List<String> getDescription(Player player) {
        return new ArrayList<>();
    }

    @Override
    public void clicked(Player player, int i, ClickType clickType, int hb) {
        Button.playNeutral(player);

        back.openMenu(player);
    }
}
