package com.unixkitty.timecontrol.network;

import com.unixkitty.timecontrol.network.packet.ConfigS2CPacket;
import com.unixkitty.timecontrol.network.packet.GamerulesS2CPacket;
import com.unixkitty.timecontrol.network.packet.TimeS2CPacket;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public class ModNetworkDispatcher
{
    public static final Object2ObjectOpenHashMap<Class<? extends CustomPacketPayload>, PacketDesignation> REGISTRY = new Object2ObjectOpenHashMap<>();

    static
    {
        registerPacket(TimeS2CPacket.class, TimeS2CPacket.TYPE, TimeS2CPacket.CODEC);
        registerPacket(GamerulesS2CPacket.class, GamerulesS2CPacket.TYPE, GamerulesS2CPacket.CODEC);
        registerPacket(ConfigS2CPacket.class, ConfigS2CPacket.TYPE, ConfigS2CPacket.CODEC);
    }

    private static <T extends CustomPacketPayload> void registerPacket(Class<T> packetClass, CustomPacketPayload.Type<T> type, StreamCodec<FriendlyByteBuf, T> codec)
    {
        PayloadTypeRegistry.playS2C().register(type, codec);
        REGISTRY.put(packetClass, new PacketDesignation(type));
    }

    public static void send(@NotNull ServerLevel level, @NotNull CustomPacketPayload packet)
    {
        level.players().forEach(serverPlayer -> ServerPlayNetworking.send(serverPlayer, packet));
    }

    public static class PacketDesignation
    {
        private final CustomPacketPayload.Type<? extends CustomPacketPayload> type;

        private PacketDesignation(CustomPacketPayload.Type<? extends CustomPacketPayload> type)
        {
            this.type = type;
        }

        public CustomPacketPayload.Type<? extends CustomPacketPayload> getType()
        {
            return this.type;
        }
    }
}