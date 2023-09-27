package gg.use.playlegend.permissions.message.impl;

import gg.use.playlegend.permissions.message.MessageKey;

public enum MessageKeys implements MessageKey {
  PLAYER_SIGN_SUCCESSFULLY_CREATED,
  PLAYER_SIGN_WRONG_BLOCK_TYPE,

  PLAYER_GROUP_INFO,
  PLAYER_GROUP_NOT_FOUND,
  PLAYER_GROUP_SUCCESSFULLY_UPDATED,

  GROUP_LIST_TITLE,
  GROUP_SUCCESSFULLY_CREATED,
  GROUP_SUCCESSFULLY_REMOVED,
  GROUP_SUCCESSFULLY_SET,
  GROUP_ATTRIBUTE_SUCCESSFULLY_RENAMED,
  GROUP_ALREADY_EXISTS,
  GROUP_DOES_NOT_EXIST;

  @Override
  public String getIdentifier() {
    return "message-keys." + this.name().toLowerCase();
  }
}
