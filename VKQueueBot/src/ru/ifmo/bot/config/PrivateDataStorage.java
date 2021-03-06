package ru.ifmo.bot.config;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrivateDataStorage {
  public static Map<String, String> accessData = new HashMap<>();

  static {
    try {
      List<String> lines = Files.readAllLines(Paths.get("config.txt"), StandardCharsets.UTF_8);

      for (int i = 0; i < lines.size(); i += 2) {
        accessData.put(lines.get(i), lines.get(i + 1));
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
