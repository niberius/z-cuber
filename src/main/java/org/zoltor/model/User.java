package org.zoltor.model;

import org.zoltor.common.HelperUtils;
import org.zoltor.model.queries.IUserQueries;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static org.zoltor.common.Config.*;

/**
 * Created by zoltor on 22.10.14.
 */
public class User implements IUserQueries {

    /**
     * Register new user (all fields are required)
     * @param nick Nickname of user (should be unique)
     * @param password Password (as plain text, not encrypted!)
     * @param email E-mail
     * @throws SQLException If user with same nick or email is exists, or some required field is null
     */
    public static void registerUser(String nick, String password, String email) throws SQLException {
        password = HelperUtils.getMd5Digest(nick, password, email);
        db.update(INSERT_REGISTER_USER, nick, password, email);
    }

    /**
     * Is user authorized to get access to game
     * @param nick Nickname of existing user
     * @param password Password which appropriate to user with `nick`.
     *                 Password should be encrypted with salt  by md5 algorithm.
     *                 Encryption method {@link HelperUtils#getMd5Digest(String...)}, args are:
     *                 nick, password (unencrypted), email.
     *                 Encrypted password should be stored on cookies or session.
     * @return True - if user exists and given password is appropriate for given user nick. Otherwise - false.
     * @throws SQLException Wrong parameter given
     */
    public static boolean isAuthorized(String nick, String password) throws SQLException {
        List<Map<String, String>> userInfo = db.get(SELECT_USER_BY_NICK, nick);
        return userInfo.size() >= 1 && userInfo.get(0).get("password").equals(password);
    }

    /**
     * Update user info
     * @param oldNick Current user nick
     * @param newNick New user nick
     * @param password New user password (should be unencrypted)
     * @param email New user email
     * @throws SQLException Some parameter contains wrong symbols or it empty / null
     */
    public static void updateUserInfo(String oldNick, String newNick, String password, String email) throws SQLException {
        password = HelperUtils.getMd5Digest(newNick, password, email);
        db.update(UPDATE_USER_INFO, newNick, password, email, oldNick);
    }

    /**
     * Get user info by nickname
     * @param nick Nickname of existing user
     * @return Map<String, String> with user info (keys of map ar: id, nick, password, email, registered_datetime)
     * @throws SQLException When nick contains illegal characters or it empty / null
     */
    public static Map<String, String> getUserInfo(String nick) throws SQLException {
        return db.get(SELECT_USER_BY_NICK, nick).get(0);
    }

}
