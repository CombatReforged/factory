package com.combatreforged.factory.api.world.scoreboard;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.Nullable;

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

    @Nullable NamedTextColor getNameTagColor();
    void setNameTagColor(@Nullable NamedTextColor color);

    VisibleFor getDeathMessagesVisibleFor();
    void setDeathMessagesVisibleFor(VisibleFor visibleFor);
    VisibleFor getNameTagsVisibleFor();
    void setNameTagsVisibleFor(VisibleFor visibleFor);
    CollideWith getCollideWith();
    void setCollideWith(CollideWith collideWith);

    default void add(String name) {
        this.getScoreboard().setTeam(name, this);
    }

    default void remove(String name) {
        this.getScoreboard().setTeam(name, null);
    }

    enum VisibleFor {
        EVERYONE, NO_ONE, NON_MATES_ONLY, MATES_ONLY
    }

    enum CollideWith {
        EVERYONE, NO_ONE, NON_MATES_ONLY, MATES_ONLY
    }
}
