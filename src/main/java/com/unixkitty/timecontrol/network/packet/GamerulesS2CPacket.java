package com.unixkitty.timecontrol.network.packet;

import com.unixkitty.timecontrol.TimeControl;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.GameRules;

public record GamerulesS2CPacket(boolean vanillaRuleValue, boolean modRuleValue) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<GamerulesS2CPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(TimeControl.MODID, "gamerules_packet"));

    public static final StreamCodec<FriendlyByteBuf, GamerulesS2CPacket> CODEC =
            StreamCodec.composite(
                    StreamCodec.of(FriendlyByteBuf::writeBoolean, FriendlyByteBuf::readBoolean), GamerulesS2CPacket::vanillaRuleValue,
                    StreamCodec.of(FriendlyByteBuf::writeBoolean, FriendlyByteBuf::readBoolean), GamerulesS2CPacket::modRuleValue,
                    GamerulesS2CPacket::new
            );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }

    public static GamerulesS2CPacket fromServerLevel(ServerLevel world)
    {
        return new GamerulesS2CPacket(
                world.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT),
                world.getGameRules().getBoolean(TimeControl.DO_DAYLIGHT_CYCLE_TC)
        );
    }
}