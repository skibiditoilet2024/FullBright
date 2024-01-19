package me.devjg.fullbright;

import me.devjg.fullbright.commands.FBCommand;
import me.devjg.fullbright.events.FBEvents;
import me.devjg.fullbright.events.FBHandler;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.lwjgl.input.Keyboard;

@Mod(modid = "devjg_fullbright", version = "2.3.2", acceptedMinecraftVersions = "[1.8,]")
public class FB {
    public static float transitionSpeed;
    public static boolean notifications;
    public static boolean changeWithLightLevel;
    public static final KeyBinding FB_KEYBIND = new KeyBinding("Next Brightness Level", Keyboard.KEY_B, "discord.gg/b35rQvS");

    public static String getMode() {
        return 0 == transitionSpeed ? "Instantaneous" : "Incremental";
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ConfigFile.readData(ConfigFile.FILE_PATH);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new FBEvents());
        MinecraftForge.EVENT_BUS.register(new FBHandler());

        ClientRegistry.registerKeyBinding(FB_KEYBIND);
        ClientCommandHandler.instance.registerCommand(new FBCommand());
    }
}
