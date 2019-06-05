package me.ohvalsgod.bklib.scoreboard;

import lombok.Getter;
import lombok.Setter;

public class SideboardConfiguration {

    @Getter @Setter private TitleGetter titleGetter;
    @Getter @Setter private ScoreGetter scoreGetter;

}
