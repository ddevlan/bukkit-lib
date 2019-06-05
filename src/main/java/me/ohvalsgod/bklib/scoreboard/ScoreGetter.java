package me.ohvalsgod.bklib.scoreboard;

import org.bukkit.entity.Player;

import java.util.List;

public interface ScoreGetter {

    List<String> getScores(Player player);

}
