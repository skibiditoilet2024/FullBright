package me.devjg.fullbright;

import net.minecraftforge.fml.common.FMLLog;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigFile {
    public static final String FILE_PATH = "DevJG_FullBright.txt";

    public static void saveData(String filePath, Object... data) {
        try (FileWriter writer = new FileWriter(filePath)) {
            for (Object d : data)
                writer.write(d.toString() + "\n");

            FMLLog.info("[FullBright]: The configuration file was saved correctly.");
        } catch (IOException e) {
            FMLLog.severe("[FullBright]: The configuration file could not save its data: " + e.getMessage());
        }
    }

    public static void readData(String filePath) {
        try {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                Files.createFile(path);
                saveData(FILE_PATH, 0.0f, true, false);
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                FB.transitionSpeed = Float.parseFloat(reader.readLine());
                FB.notifications = Boolean.parseBoolean(reader.readLine());
                FB.changeWithLightLevel = Boolean.parseBoolean(reader.readLine());

                FMLLog.info("[FullBright]: The configuration file was read correctly.");
            }
        } catch (IOException | NumberFormatException e) {
            FMLLog.severe("[FullBright]: The configuration file could not read its data: " + e.getMessage());
        }
    }
}
