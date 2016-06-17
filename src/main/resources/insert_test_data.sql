-- New Users
INSERT INTO users (id, name, passwd) VALUES (0, '[DELETED]', 'nopass');
INSERT INTO users (name, passwd) VALUES ('Xavier B',  'password1');
INSERT INTO users (name, passwd) VALUES ('Maro G',    'password1');
INSERT INTO users (name, passwd) VALUES ('Maliik G',  'password2');
INSERT INTO users (name, passwd) VALUES ('Gina ', 'password2');

-- New Groups
INSERT INTO groups (name) VALUES ('Regular Group');
INSERT INTO groups (name, admin_id) VALUES ('An Administrated Group', 1);
INSERT INTO groups (name, admin_id, private) VALUES ('Private Room', null, 1);

-- Adding Users To Groups
INSERT INTO user_group (user_id, group_id) VALUES (1, 1);
INSERT INTO user_group (user_id, group_id) VALUES (2, 1);
INSERT INTO user_group (user_id, group_id) VALUES (3, 1);
INSERT INTO user_group (user_id, group_id) VALUES (4, 1);

-- Inserting into administrated group
INSERT INTO user_group (user_id, group_id) VALUES (2, 2);
INSERT INTO user_group (user_id, group_id) VALUES (3, 2);

-- Inserting into private room
INSERT INTO user_group (user_id, group_id) VALUES (3, 3);
INSERT INTO user_group (user_id, group_id) VALUES (4, 3);

-- Messages
INSERT INTO messages (id, sender_id, group_id, content, time) VALUES (null, 1, 1, 'Hey Guys, this is a test message', datetime('now'));
INSERT INTO messages (id, sender_id, group_id, content, time) VALUES (null, 1, 1, 'Hey Guys, this is another test message', datetime('now'));
INSERT INTO messages (id, sender_id, group_id, content, time) VALUES (null, 1, 2, 'This is a message to the administrated group', datetime('now'));
INSERT INTO messages (id, sender_id, group_id, content, time) VALUES (null, 3, 2, 'This is a second message to the administrated group', datetime('now'));
INSERT INTO messages (id, sender_id, group_id, content, time) VALUES (null, 3, 3, 'this is a message to the private group', datetime('now'));
INSERT INTO messages (id, sender_id, group_id, content, time) VALUES (null, 3, 3, 'this is a test message to the private group', datetime('now'));
INSERT INTO messages (id, sender_id, group_id, content, time) VALUES (null, 4, 3, 'this is another message to the private group', datetime('now'));