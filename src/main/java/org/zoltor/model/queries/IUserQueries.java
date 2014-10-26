package org.zoltor.model.queries;

/**
 * Created by zoltor on 22.10.14.
 */
public interface IUserQueries {

    // SELECT queries

    String SELECT_USER_BY_NICK =
            "SELECT id, nick, password, email, registered_datetime FROM users WHERE nick = ?";

    String SELECT_USER_BY_ID =
            "SELECT id, nick, password, email, registered_datetime FROM users WHERE id = ?";

    String SELECT_GET_GAME_ID_FOR_USER =
            "SELECT r.id FROM rooms r\n" +
            "LEFT JOIN rel_users_rooms rur ON rur.room_id = r.id\n" +
            "WHERE r.is_active = 1 AND (r.host_id = ? OR rur.user_id = ?)";

    // INSERT queries

    String INSERT_REGISTER_USER =
            "INSERT INTO users (nick, password, email) VALUES (?, ?, ?)";

    // UPDATE queries

    String UPDATE_USER_INFO =
            "UPDATE users SET nick = ?, password = ?, email = ? WHERE nick = ?";
}
