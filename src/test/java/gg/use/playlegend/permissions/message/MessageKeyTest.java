package gg.use.playlegend.permissions.message;

import static org.junit.jupiter.api.Assertions.*;

import gg.use.playlegend.permissions.message.impl.MessageKeys;
import gg.use.playlegend.permissions.message.impl.YamlMessageLoader;
import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class MessageKeyTest {

  private static YamlMessageLoader messageLoader;

  @BeforeAll
  public static void setup() {
    String path = "src/main/resources/";
    FileConfiguration config = YamlConfiguration.loadConfiguration(new File(path, "config.yml"));
    messageLoader = new YamlMessageLoader(config);
  }

  @Test
  public void testYamlMessageLoader() {
    String str = messageLoader.getMessage(MessageKeys.GROUP_DOES_NOT_EXIST, "Member");
    assertEquals("[Permissions] Group Member does not exist", str);
  }

}
