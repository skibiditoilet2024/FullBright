package me.devjg.fullbright.events;

import me.devjg.fullbright.FB;
import me.devjg.fullbright.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FBHandler {
    private static final Minecraft mc = Minecraft.getMinecraft();

    private static final float DIMMEST = -0.1F;
    private static final float DIM = 3.0F;
    private static final float BRIGHT = 6.5F;
    private static final float BRIGHTEST = 12.0F;

    private static boolean shouldIncrement;
    private static float nextLevel;

    public FBHandler() {
        setNextLevel();
    }

    @SubscribeEvent
    public void onRender(RenderHandEvent event) {
        if (FB.changeWithLightLevel)
            onLightLevelChange();
        else if (FB.FB_KEYBIND.isPressed())
            keyPressed();

        if (shouldIncrement)
            graduallyChange();
    }

    private void onLightLevelChange() {
        if (mc.thePlayer != null) {
            BlockPos blockpos = new BlockPos(mc.getRenderViewEntity().posX, mc.getRenderViewEntity().getEntityBoundingBox().minY, mc.getRenderViewEntity().posZ);
            Chunk chunk = mc.theWorld.getChunkFromBlockCoords(blockpos);

            float brightness = chunk.getLightSubtracted(blockpos, 0) + chunk.getLightFor(EnumSkyBlock.SKY, blockpos) + chunk.getLightFor(EnumSkyBlock.BLOCK, blockpos);
            float newBrightness = Utils.map(Math.min(brightness, 30.0f), 0f, 30f, BRIGHTEST, 1f);

            instantChange(newBrightness);
        }
    }

    private void instantChange(float toLevel) {
        float moveBy = toLevel - mc.gameSettings.gammaSetting;
        mc.gameSettings.gammaSetting += moveBy;
        mc.gameSettings.saveOptions();

        setNextLevel();
    }

    private void graduallyChange() {
        mc.gameSettings.gammaSetting = Utils.lerp(mc.gameSettings.gammaSetting, nextLevel, Utils.getTransitionSpeed());

        if (0.1 >= Math.abs(mc.gameSettings.gammaSetting - nextLevel)) {
            shouldIncrement = false;
            mc.gameSettings.saveOptions();
            setNextLevel();
        }
    }

    private static void setNextLevel() {
        float currentLevel = mc.gameSettings.gammaSetting;

        // TODO refactor
        if (2 >= Math.abs(DIMMEST - currentLevel))
            nextLevel = DIM;
        else if (2 >= Math.abs(DIM - currentLevel))
            nextLevel = BRIGHT;
        else if (2 >= Math.abs(BRIGHT - currentLevel))
            nextLevel = BRIGHTEST;
        else
            nextLevel = DIMMEST;
    }

    private static String getText() {
        final String TEXT_BASE =
                EnumChatFormatting.DARK_GRAY + "[" +
                EnumChatFormatting.YELLOW + "FullBright" +
                EnumChatFormatting.DARK_GRAY + "] " +
                EnumChatFormatting.GRAY + "set to ";

        // TODO refactor
        if ("Incremental".equals(FB.getMode())) {
            if (DIMMEST == nextLevel)
                return TEXT_BASE + EnumChatFormatting.DARK_RED + "DIMMEST";
            else if (DIM == nextLevel)
                return TEXT_BASE + EnumChatFormatting.RED + "DIM";
            else if (BRIGHT == nextLevel)
                return TEXT_BASE + EnumChatFormatting.YELLOW + "BRIGHT";
            return TEXT_BASE + EnumChatFormatting.GREEN + "BRIGHTEST";
        }

        else {
            if (DIMMEST == nextLevel)
                return TEXT_BASE + EnumChatFormatting.GREEN + "BRIGHTEST";
            else if (DIM == nextLevel)
                return TEXT_BASE + EnumChatFormatting.DARK_RED + "DIMMEST";
            else if (BRIGHT == nextLevel)
                return TEXT_BASE + EnumChatFormatting.RED + "DIM";
            return TEXT_BASE + EnumChatFormatting.YELLOW + "BRIGHT";
        }
    }

    private void keyPressed() {
        final String CURRENT_MODE = FB.getMode();

        if ("Instantaneous".equals(CURRENT_MODE))
            instantChange(nextLevel);
        else if ("Incremental".equals(CURRENT_MODE))
            shouldIncrement = true;

        if (FB.notifications)
            mc.thePlayer.addChatMessage(new ChatComponentText(getText()));
    }
}
