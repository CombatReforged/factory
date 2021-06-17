package com.combatreforged.factory.api.world.scoreboard;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.List;

public interface ScoreboardTeam {
    Scoreboard getScoreboard();
    String getName();
    List<String> getPlayers();
    Component getDisplayName();
    void setDisplayName(Component displayName);
    Component getPrefix();
    void setPrefix(Component prefix);
    Component getSuffix();
    void setSuffix(Component suffix);
    boolean isFriendlyFire();
    void setFriendlyFire(boolean friendlyFire);
    boolean canSeeInvisibleMates();
    void setSeeInvisibleMates(boolean seeInvisibleMates);

    NamedTextColor getNameTagColor();
    void setNameTagColor(NamedTextColor color);

    VisibleFor getDeathMessagesVisibleFor();
    void setDeathMessagesVisibleFor(VisibleFor visibleFor);
    VisibleFor getNameTagsVisibleFor();
    void setNameTagsVisibleFor(VisibleFor visibleFor);
    CollideWith getCollideWith();
    void setCollideWith(CollideWith collideWith);

    enum VisibleFor {
        EVERYONE, NO_ONE, NON_MATES_ONLY, MATES_ONLY
    }

    enum CollideWith {
        EVERYONE, NO_ONE, NON_MATES_ONLY, MATES_ONLY
    }
}
