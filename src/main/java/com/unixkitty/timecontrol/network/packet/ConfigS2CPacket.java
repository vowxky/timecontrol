package com.unixkitty.timecontrol.network.packet;

import com.unixkitty.timecontrol.TimeControl;
import com.unixkitty.timecontrol.config.Config;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ConfigS2CPacket(
        int day_length_seconds,
        int night_length_seconds,
        int sync_to_system_time_rate,
        boolean sync_to_system_time,
        double sync_to_system_time_offset
) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<ConfigS2CPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(TimeControl.MODID, "config_packet"));

    public static final StreamCodec<FriendlyByteBuf, ConfigS2CPacket> CODEC =
            StreamCodec.composite(
                    StreamCodec.of(FriendlyByteBuf::writeInt, FriendlyByteBuf::readInt), ConfigS2CPacket::day_length_seconds,
                    StreamCodec.of(FriendlyByteBuf::writeInt, FriendlyByteBuf::readInt), ConfigS2CPacket::night_length_seconds,
                    StreamCodec.of(FriendlyByteBuf::writeInt, FriendlyByteBuf::readInt), ConfigS2CPacket::sync_to_system_time_rate,
                    StreamCodec.of(FriendlyByteBuf::writeBoolean, FriendlyByteBuf::readBoolean), ConfigS2CPacket::sync_to_system_time,
                    StreamCodec.of(FriendlyByteBuf::writeDouble, FriendlyByteBuf::readDouble), ConfigS2CPacket::sync_to_system_time_offset,
                    ConfigS2CPacket::new
            );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }

    public static ConfigS2CPacket fromConfig()
    {
        return new ConfigS2CPacket(
                Config.day_length_seconds.get(),
                Config.night_length_seconds.get(),
                Config.sync_to_system_time_rate.get(),
                Config.sync_to_system_time.get(),
                Config.sync_to_system_time_offset.get()
        );
    }
}