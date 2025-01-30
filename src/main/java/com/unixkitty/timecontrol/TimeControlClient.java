package com.unixkitty.timecontrol;

import com.unixkitty.timecontrol.config.Config;
import com.unixkitty.timecontrol.handler.ClientTimeHandler;
import com.unixkitty.timecontrol.network.ModNetworkDispatcher;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

@Environment(EnvType.CLIENT)
public class TimeControlClient implements ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        ModNetworkDispatcher.REGISTRY.forEach((clazz, packetDesignation) -> {
            ClientPlayNetworking.registerGlobalReceiver(
                    packetDesignation.getType(),
                    (payload, context) -> {
                        context.client().execute(() -> ClientTimeHandler.handlePacket(payload, context.client()));
                    }
            );
        });
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> TimeControlClientCommand.register(dispatcher));

        ClientTickEvents.START_CLIENT_TICK.register(new ClientTimeHandler.WorldTick());

        Config.setClient();
    }
}