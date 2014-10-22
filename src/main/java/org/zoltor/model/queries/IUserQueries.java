package org.zoltor.model.queries;

/**
 * Created by zoltor on 22.10.14.
 */
public interface IUserQueries {
    String SELECT_USER_BY_USERNAME =
            "SELECT id, nick, username, password, email FROM users WHERE username = :userName";
}
