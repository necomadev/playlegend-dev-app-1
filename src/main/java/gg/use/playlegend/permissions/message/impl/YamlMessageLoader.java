package gg.use.playlegend.permissions.message.impl;

import gg.use.playlegend.permissions.message.MessageKey;
import gg.use.playlegend.permissions.message.MessageLoader;
import org.bukkit.configuration.file.FileConfiguration;

public class YamlMessageLoader implements MessageLoader {

  private final FileConfiguration config;

  public YamlMessageLoader(FileConfiguration config) {
    this.config = config;
  }

  @Override
  public String getRawMessage(MessageKey key) {
    return this.config.getString(key.getIdentifier());
  }
}
