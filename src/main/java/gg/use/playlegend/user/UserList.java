package gg.use.playlegend.user;

import java.util.UUID;

public interface UserList {

  User addUser(UUID uuid);

  void removeUser(UUID uuid);

  User getUser(UUID uuid);
}
