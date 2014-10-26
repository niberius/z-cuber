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

    // INSERT queries

    String INSERT_REGISTER_USER =
            "INSERT INTO users (nick, password, email) VALUES (?, ?, ?)";

    // UPDATE queries

    String UPDATE_USER_INFO =
            "UPDATE users SET nick = ?, password = ?, email = ? WHERE nick = ?";
}
