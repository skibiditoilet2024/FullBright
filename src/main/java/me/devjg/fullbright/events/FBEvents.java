package me.devjg.fullbright.events;

import me.devjg.fullbright.gui.FBGui;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class FBEvents {
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (FBGui.show && TickEvent.Phase.END == event.phase) {
            Minecraft.getMinecraft().displayGuiScreen(new FBGui());
            FBGui.show = false;
        }
    }

//    @SubscribeEvent
//    public void onSkyLightUpdate() {
//
//    }
}
