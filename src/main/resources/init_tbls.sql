PRAGMA FOREIGN_KEYS = ON;

-- Create the users table
CREATE TABLE IF NOT EXISTS users (
  id      INTEGER PRIMARY KEY AUTOINCREMENT ,
  name    TEXT    NOT NULL ,
  passwd  TEXT    NOT NULL
);

-- Create the groups table
CREATE TABLE IF NOT EXISTS groups (
  id        INTEGER   PRIMARY KEY AUTOINCREMENT ,
  name      TEXT      NOT NULL ,
  admin_id  INTEGER   DEFAULT NULL ,
  privacy_level   INTEGER   DEFAULT 0,
  FOREIGN KEY (admin_id) REFERENCES users(id) ON DELETE SET DEFAULT
);

-- Create the user_group table
CREATE TABLE IF NOT EXISTS user_group (
  id        INTEGER PRIMARY KEY ,
  user_id   INTEGER NOT NULL ,
  group_id  INTEGER NOT NULL ,
  FOREIGN KEY (user_id)   REFERENCES users(id) ON DELETE CASCADE ,
  FOREIGN KEY (group_id)  REFERENCES groups(id) ON DELETE CASCADE ,
  UNIQUE (user_id, group_id)
);

-- Create the messages table
CREATE TABLE IF NOT EXISTS messages (
  id        INTEGER   PRIMARY KEY AUTOINCREMENT ,
  sender_id INTEGER   DEFAULT 0 ,
  group_id  INTEGER   NOT NULL ,
  content   TEXT      NOT NULL ,
  time      datetime  DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE SET DEFAULT ,
  FOREIGN KEY (group_id)  REFERENCES groups(id) ON DELETE CASCADE
);

-- Make sure that if a group is created with an admin id, the admin is added to the group
-- Maybe I should check to see if the user exists, and reject the group creation if not?
CREATE TRIGGER add_admin_to_group AFTER INSERT
  ON groups
  WHEN new.admin_id IS NOT NULL
BEGIN
  INSERT INTO user_group (user_id, group_id) VALUES (new.admin_id, new.id);
END;

-- Make sure that the user is actually in the group they are trying to send the message to
CREATE TRIGGER message_to_group BEFORE INSERT
  ON messages
  WHEN NOT exists(SELECT user_group.user_id
                  FROM user_group
                  WHERE new.sender_id = user_group.user_id AND
                        new.group_id = user_group.group_id)
BEGIN
    SELECT RAISE(ABORT, 'A user cannot send messages to a group they are not in');
END;

-- Make sure that private groups (direct messages) only hold up to 2 users.
CREATE TRIGGER max_private_group BEFORE INSERT 
  ON user_group
  WHEN (SELECT groups.private
        FROM groups
        WHERE groups.id = new.group_id) = 1 and
       (SELECT COUNT(*) FROM user_group WHERE user_group.group_id = new.group_id ) > 1
BEGIN
  SELECT raise(ABORT, 'There is a limit of 2 people in a private group.');
END;