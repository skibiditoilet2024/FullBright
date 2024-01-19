package me.devjg.fullbright.commands;

import me.devjg.fullbright.gui.FBGui;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

import java.util.Collections;
import java.util.List;

public class FBCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "fullbright";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/fullbright";
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("fb");
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        FBGui.show = true;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return -1;
    }
}
