package me.devjg.fullbright.gui;

import me.devjg.fullbright.ConfigFile;
import me.devjg.fullbright.FB;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlider;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import org.lwjgl.opengl.GL11;

public class FBGui extends GuiScreen {
    public static boolean show;

    @Override
    public void initGui() {
        buttonList.clear();

        GuiSlider.FormatHelper formatHelper = new GuiSlider.FormatHelper() {
            @Override
            public String getText(int id, String name, float value) {
                return String.format("%s: %.1f", name, value);
            }
        };

        buttonList.add(new GuiSlider(guiResponder, 5, width / 2 - 75, height / 2 - 40, "Transition Speed", 0.0F, 5.0F, FB.transitionSpeed, formatHelper));
        buttonList.add(new GuiButton(2, width / 2 - 87, height / 2 - 10, 175, 20, "Change w/ Light Level: " + FB.changeWithLightLevel));

        buttonList.add(new GuiButton(3, width - 130, height - 50, 125, 20, "Notifications: " + FB.notifications));
        buttonList.add(new GuiButton(4, width - 130, height - 25, 125, 20, "Discord"));

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);

        GL11.glPushMatrix();
        GL11.glScalef(2.0F, 2.0F, 2.0F);
        drawCenteredString(fontRendererObj, "The Future Of", width / 4, 2, 0x596891);
        drawCenteredString(fontRendererObj, "FullBright", width / 4, 12, 0x596891);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glScalef(1.0F, 1.0F, 1.0F);
        drawCenteredString(fontRendererObj, "Mode: " + FB.getMode(), width / 2, height / 2 - 55, 0x596891);
        GL11.glPopMatrix();
    }

    @Override
    protected void actionPerformed(GuiButton pressedButton) {
        switch (pressedButton.id) {
            case 1:
                if (3 == FB.transitionSpeed)
                    FB.transitionSpeed = 0;
                else
                    FB.transitionSpeed++;

                mc.displayGuiScreen(new FBGui());
                break;
            case 2:
                FB.changeWithLightLevel = !FB.changeWithLightLevel;
                mc.displayGuiScreen(new FBGui());
                break;
            case 3:
                FB.notifications = !FB.notifications;
                mc.displayGuiScreen(new FBGui());
                break;
            case 4:
                mc.displayGuiScreen(null);

                IChatComponent url = new ChatComponentText(EnumChatFormatting.DARK_GRAY + "[" + EnumChatFormatting.YELLOW + "FullBright" + EnumChatFormatting.DARK_GRAY + "] " + EnumChatFormatting.GRAY + "Click here to join the discord!");
                url.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/b35rQvS"));
                mc.thePlayer.addChatMessage(url);
                break;
            case 5:
                FB.transitionSpeed = ((GuiSlider) pressedButton).func_175220_c();
                break;
        }
    }

    @Override
    public void onGuiClosed() {
        ConfigFile.saveData(ConfigFile.FILE_PATH, FB.transitionSpeed, FB.notifications, FB.changeWithLightLevel);
    }

    GuiPageButtonList.GuiResponder guiResponder = new GuiPageButtonList.GuiResponder() {
        @Override
        public void onTick(int id, float value) {
            // Handle slider value being changed
            if (id == 5) {
                FB.transitionSpeed = value;
            }
        }

        @Override
        public void func_175319_a(int id, String value) {
            if (id == 5) {
                try {
                    FB.transitionSpeed = Float.parseFloat(value);
                } catch (NumberFormatException ignored) {
                }
            }
        }

        @Override
        public void func_175321_a(int id, boolean value) {}
    };
}
