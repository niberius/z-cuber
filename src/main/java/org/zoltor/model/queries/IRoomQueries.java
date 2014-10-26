package org.zoltor.model.queries;

/**
 * Created by zoltor on 22.10.14.
 */
public interface IRoomQueries {

    // SELECT queries

    String SELECT_ACTIVE_ROOMS_LIST =
            "SELECT r.id, u.nick creator, r.created_datetime, r.is_private, count(rur.room_id) players_count FROM rooms r\n" +
            "JOIN users u ON r.host_id = u.id\n" +
            "JOIN rel_users_rooms rur ON rur.room_id = r.id\n" +
            "WHERE r.is_active = 1\n" +
            "GROUP BY rur.room_id";

    String SELECT_ROOM_INFO =
            "SELECT r.id, r.host_id, h.nick host_nick, u.nick user_nick FROM rooms r\n" +
            "JOIN rel_users_rooms rur ON rur.room_id = r.id\n" +
            "JOIN users u ON rur.user_id = u.id\n" +
            "JOIN users h ON h.id = r.host_id\n" +
            "WHERE r.id = ?";

    // INSERT queries

    String INSERT_START_NEW_GAME_BY_USER_NICK =
            "INSERT INTO rooms (host_id, name, password, is_private) VALUES (?, ?, ?, ?)";

    String INSERT_JOIN_GAME_BY_NICK =
            "INSERT INTO rel_users_rooms (user_id, room_id) VALUES (?, ?)";

    // UPDATE queries

    String UPDATE_CLOSE_GAME =
            "UPDATE rooms SET is_active = 0 WHERE id = ?";
}
