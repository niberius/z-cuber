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

    String SELECT_RESULTS =
            "SELECT res.id, res.result_time, res.result_datetime, res.formula_id, f.formula FROM results res" +
            "JOIN formulas f ON f.id = res.formula_id" +
            "WHERE res.user_id = ?";

    String SELECT_RESULTS_FOR_ROOM =
            "SELECT res.id, res.result_time, res.result_datetime, res.formula_id, f.formula FROM results res" +
            "JOIN formulas f ON f.id = res.formula_id" +
            "WHERE res.user_id = ? AND res.room_id = ?";

    // INSERT queries

    String INSERT_REGISTER_USER =
            "INSERT INTO users (nick, password, email) VALUES (?, ?, ?)";

    String INSERT_POST_USER_RESULT =
            "INSERT INTO results (user_id, room_id, formula_id, result_time) VALUES (?, ?, ?, ?)";

    // UPDATE queries

    String UPDATE_USER_INFO =
            "UPDATE users SET nick = ?, password = ?, email = ? WHERE nick = ?";
}
