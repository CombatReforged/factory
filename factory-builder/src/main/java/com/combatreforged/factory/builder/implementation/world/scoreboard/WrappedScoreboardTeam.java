package com.combatreforged.factory.builder.implementation.world.scoreboard;

import com.combatreforged.factory.api.world.scoreboard.Scoreboard;
import com.combatreforged.factory.api.world.scoreboard.ScoreboardTeam;
import com.combatreforged.factory.builder.extension.world.scores.PlayerTeamExtension;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Team;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.combatreforged.factory.builder.implementation.util.ObjectMappings.convertColor;
import static com.combatreforged.factory.builder.implementation.util.ObjectMappings.convertComponent;

public class WrappedScoreboardTeam extends Wrapped<PlayerTeam> implements ScoreboardTeam {
    public static final BiMap<VisibleFor, Team.Visibility> VISIBLE_MAP = HashBiMap.create();
    public static final BiMap<CollideWith, Team.CollisionRule> COLLIDE_MAP = HashBiMap.create();

    static {
        VISIBLE_MAP.put(VisibleFor.EVERYONE, Team.Visibility.ALWAYS);
        VISIBLE_MAP.put(VisibleFor.MATES_ONLY, Team.Visibility.HIDE_FOR_OTHER_TEAMS);
        VISIBLE_MAP.put(VisibleFor.NON_MATES_ONLY, Team.Visibility.HIDE_FOR_OWN_TEAM);
        VISIBLE_MAP.put(VisibleFor.NO_ONE, Team.Visibility.NEVER);

        COLLIDE_MAP.put(CollideWith.EVERYONE, Team.CollisionRule.ALWAYS);
        COLLIDE_MAP.put(CollideWith.MATES_ONLY, Team.CollisionRule.PUSH_OWN_TEAM);
        COLLIDE_MAP.put(CollideWith.NON_MATES_ONLY, Team.CollisionRule.PUSH_OTHER_TEAMS);
        COLLIDE_MAP.put(CollideWith.NO_ONE, Team.CollisionRule.NEVER);
    }

    public WrappedScoreboardTeam(PlayerTeam wrapped) {
        super(wrapped);
    }

    @Override
    public Scoreboard getScoreboard() {
        return Wrapped.wrap(((PlayerTeamExtension) wrapped).getScoreboard(), WrappedScoreboard.class);
    }

    @Override
    public String getName() {
        return wrapped.getName();
    }

    @Override
    public List<String> getPlayers() {
        return new ArrayList<>(wrapped.getPlayers());
    }

    @Override
    public Component getDisplayName() {
        return convertComponent(wrapped.getDisplayName());
    }

    @Override
    public void setDisplayName(Component displayName) {
        wrapped.setDisplayName(convertComponent(displayName));
    }

    @Override
    public Component getPrefix() {
        return convertComponent(wrapped.getPlayerPrefix());
    }

    @Override
    public void setPrefix(Component prefix) {
        wrapped.setPlayerPrefix(convertComponent(prefix));
    }

    @Override
    public Component getSuffix() {
        return convertComponent(wrapped.getPlayerSuffix());
    }

    @Override
    public void setSuffix(Component suffix) {
        wrapped.setPlayerSuffix(convertComponent(suffix));
    }

    @Override
    public boolean isFriendlyFire() {
        return wrapped.isAllowFriendlyFire();
    }

    @Override
    public void setFriendlyFire(boolean friendlyFire) {
        wrapped.setAllowFriendlyFire(friendlyFire);
    }

    @Override
    public boolean canSeeInvisibleMates() {
        return wrapped.canSeeFriendlyInvisibles();
    }

    @Override
    public void setSeeInvisibleMates(boolean seeInvisibleMates) {
        wrapped.setSeeFriendlyInvisibles(seeInvisibleMates);
    }

    @Override
    public @Nullable NamedTextColor getNameTagColor() {
        return convertColor(wrapped.getColor());
    }

    @Override
    public void setNameTagColor(@Nullable NamedTextColor color) {
        wrapped.setColor(convertColor(color));
    }

    @Override
    public VisibleFor getDeathMessagesVisibleFor() {
        return VISIBLE_MAP.inverse().get(wrapped.getDeathMessageVisibility());
    }

    @Override
    public void setDeathMessagesVisibleFor(VisibleFor visibleFor) {
        wrapped.setDeathMessageVisibility(VISIBLE_MAP.get(visibleFor));
    }

    @Override
    public VisibleFor getNameTagsVisibleFor() {
        return VISIBLE_MAP.inverse().get(wrapped.getNameTagVisibility());
    }

    @Override
    public void setNameTagsVisibleFor(VisibleFor visibleFor) {
        wrapped.setNameTagVisibility(VISIBLE_MAP.get(visibleFor));
    }

    @Override
    public CollideWith getCollideWith() {
        return COLLIDE_MAP.inverse().get(wrapped.getCollisionRule());
    }

    @Override
    public void setCollideWith(CollideWith collideWith) {
        wrapped.setCollisionRule(COLLIDE_MAP.get(collideWith));
    }
}
