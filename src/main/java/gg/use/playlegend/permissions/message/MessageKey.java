package gg.use.playlegend.permissions.message;

/**
 * A MessageKey is a static reference to a specified string that can be changed at will
 * This system can easily be extended with a language function in the future if needed
 */
public interface MessageKey {

  /**
   * Returns the string identifier of the message key or the message respectively
   *
   * @return identifier as string
   */
  String getIdentifier();

}
