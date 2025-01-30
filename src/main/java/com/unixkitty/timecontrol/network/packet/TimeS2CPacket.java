package com.unixkitty.timecontrol.network.packet;

import com.unixkitty.timecontrol.TimeControl;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record TimeS2CPacket(long customtime, double multiplier) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<TimeS2CPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(TimeControl.MODID, "time_packet"));

    public static final StreamCodec<FriendlyByteBuf, TimeS2CPacket> CODEC =
            StreamCodec.composite(
                    StreamCodec.of(FriendlyByteBuf::writeLong, FriendlyByteBuf::readLong), TimeS2CPacket::customtime,
                    StreamCodec.of(FriendlyByteBuf::writeDouble, FriendlyByteBuf::readDouble), TimeS2CPacket::multiplier,
                    TimeS2CPacket::new
            );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }
}