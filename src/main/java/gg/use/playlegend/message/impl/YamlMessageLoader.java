package gg.use.playlegend.message.impl;

import gg.use.playlegend.message.MessageKey;
import gg.use.playlegend.message.MessageLoader;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

public class YamlMessageLoader implements MessageLoader {

  private final FileConfiguration config;

  public YamlMessageLoader(FileConfiguration config) {
    this.config = config;
  }

  @Override
  public String getRawMessage(MessageKey key) {
    return this.config.getString(key.getKey());
  }
}
