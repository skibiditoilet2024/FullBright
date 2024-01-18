package me.devjg.fullbright;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FBHandler {
  private static final KeyBinding TOGGLE_FB_BINDING = Fullbright.fullbright;
  private static final Minecraft mc = Minecraft.getMinecraft();
  private static boolean shouldIncrement;

  private static float nextLevel;
  private static final float DIMMEST = -0.1F;
  private static final float DIM = 3.0F;
  private static final float BRIGHT = 6.5F;
  private static final float BRIGHTEST = 12.0F;

  private static final String TEXT_BASE =
      EnumChatFormatting.DARK_GRAY + "[" +
      EnumChatFormatting.YELLOW + "FullBright" +
      EnumChatFormatting.DARK_GRAY + "] " +
      EnumChatFormatting.GRAY + "set to ";

  FBHandler() {
    setNextLevel();
  }

  private static float getTransitionSpeed() {
    return (float) ((Fullbright.transitionSpeed*6.0)/1000);
  }

  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onRender(RenderHandEvent event)
  {
    if (Fullbright.changeWithLightLevel)
    {
      onLightLevelChange();
    }

    else if (TOGGLE_FB_BINDING.isPressed())
    {
      if ("Instantaneous".equals(Fullbright.getMode()))
      {
        onInstantChange(nextLevel);
        mc.gameSettings.saveOptions();
      }

      else if ("Incremental".equals(Fullbright.getMode()))
        shouldIncrement = true;

      if (Fullbright.notifications)
        mc.thePlayer.addChatMessage(new ChatComponentText(getText()));
    }

    if (shouldIncrement)
      onGradualChange();
  }

  private void onLightLevelChange()
  {
    if (mc.thePlayer != null) {
      BlockPos blockpos = new BlockPos(mc.getRenderViewEntity().posX, mc.getRenderViewEntity().getEntityBoundingBox().minY, mc.getRenderViewEntity().posZ);
      Chunk chunk = mc.theWorld.getChunkFromBlockCoords(blockpos);

      float brightness = chunk.getLightSubtracted(blockpos, 0) + chunk.getLightFor(EnumSkyBlock.SKY, blockpos) + chunk.getLightFor(EnumSkyBlock.BLOCK, blockpos);
      float newBrightness = map(Math.min(brightness, 30.0f), 0f, 30f, BRIGHTEST, 1f);

      onInstantChange(newBrightness);
    }
  }

  private void onInstantChange(float _toLevel)
  {
    float moveBy = _toLevel - mc.gameSettings.gammaSetting;
    mc.gameSettings.gammaSetting += moveBy;

    setNextLevel();
  }

  private void onGradualChange()
  {
    mc.gameSettings.gammaSetting = lerp(mc.gameSettings.gammaSetting, nextLevel, getTransitionSpeed());

    if (0.1 >= Math.abs(mc.gameSettings.gammaSetting - nextLevel))
    {
      shouldIncrement = false;
      mc.gameSettings.saveOptions();
      setNextLevel();
    }
  }

  private static void setNextLevel()
  {
    float currentLevel = mc.gameSettings.gammaSetting;

    if (2 >= Math.abs(DIMMEST - currentLevel))
      nextLevel = DIM;
    else if (2 >= Math.abs(DIM - currentLevel))
      nextLevel = BRIGHT;
    else if (2 >= Math.abs(BRIGHT - currentLevel))
      nextLevel = BRIGHTEST;
    else
      nextLevel = DIMMEST;
  }

  private static String getText()
  {
    if ("Incremental".equals(Fullbright.getMode()))
    {
      if (DIMMEST == nextLevel)
        return TEXT_BASE + EnumChatFormatting.DARK_RED + "DIMMEST";
      else if (DIM == nextLevel)
        return TEXT_BASE + EnumChatFormatting.RED + "DIM";
      else if (BRIGHT == nextLevel)
        return TEXT_BASE + EnumChatFormatting.YELLOW + "BRIGHT";
      return TEXT_BASE + EnumChatFormatting.GREEN + "BRIGHTEST";
    }

    else
    {
      if (DIMMEST == nextLevel)
        return TEXT_BASE + EnumChatFormatting.GREEN + "BRIGHTEST";
      else if (DIM == nextLevel)
        return TEXT_BASE + EnumChatFormatting.DARK_RED + "DIMMEST";
      else if (BRIGHT == nextLevel)
        return TEXT_BASE + EnumChatFormatting.RED + "DIM";
      return TEXT_BASE + EnumChatFormatting.YELLOW + "BRIGHT";
    }
  }

  private static float lerp(float from, float to, float by) { return (float) ((from * (1.0 - by)) + (to * by)); }
  private static float map(float x, float input_start, float input_end, float output_start, float output_end)
  {
    return (x - input_start) / (input_end - input_start) * (output_end - output_start) + output_start;
  }
}
