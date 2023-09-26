package gg.use.playlegend.message;

public interface MessageLoader {

  String getRawMessage(MessageKey key);

  default String getMessage(MessageKey key, Object... replacements) {
    return String.format(getRawMessage(key), replacements);
  }
}
