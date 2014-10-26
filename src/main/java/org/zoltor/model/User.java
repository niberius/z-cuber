package org.zoltor.model;

import org.zoltor.common.HelperUtils;
import org.zoltor.model.entities.UserEntity;
import org.zoltor.model.queries.IUserQueries;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import static org.zoltor.common.Config.db;

/**
 * Created by zoltor on 22.10.14.
 */
public class User implements IUserQueries {

    /**
     * Register new user (all fields are required)
     * @param user User entity with set parameters (all required except id) {@link org.zoltor.model.entities.UserEntity}
     * @throws SQLException If user with same nick or email is exists, or some required field is null
     */
    public static void registerUser(UserEntity user) throws SQLException {
        db.update(INSERT_REGISTER_USER,
                user.getNick(), user.getEncryptedPassword(), user.getEmail());
    }

    /**
     * Is user authorized to get access to game
     * @param user UserEntity object with set `nick` and `encryptedPassword` fields.
     *             Password which appropriate to user with `nick`.
     *             Password should be encrypted with salt by md5 algorithm.
     *             Encryption method {@link HelperUtils#getMd5Digest(String...)}, args are:
     *             nick, password (unencrypted), email.
     *             Encrypted password should be stored on cookies or session.
     * @return True- if user exists and given password is appropriate for given user nick. Otherwise - false.
     * @throws SQLException Wrong parameter given
     */
    public static boolean isAuthorized(UserEntity user) throws SQLException {
        List<Map<String, String>> userInfo = db.get(SELECT_USER_BY_NICK, user.getNick());
        if (userInfo.size() >= 1) {
            user.setEmail(userInfo.get(0).get("email"));
            return userInfo.get(0).get("password").equals(user.getEncryptedPassword());
        } else {
            return false;
        }
    }

    /**
     * Update user info
     * @param oldUserInfo UserEntity with old info (nick must be set)
     * @param newUserInfo UserEntity with new user info (nick, email, password (plain, not encrypted))
     * @throws SQLException Some parameter contains wrong symbols or it empty / null
     */
    public static void updateUserInfo(UserEntity oldUserInfo, UserEntity newUserInfo) throws SQLException {
        db.update(UPDATE_USER_INFO,
                newUserInfo.getNick(), newUserInfo.getEncryptedPassword(), newUserInfo.getEmail(), oldUserInfo.getNick());
    }

    /**
     * Get user info by nickname
     * @param nick Nickname of existing user
     * @return Map<String, String> with user info (keys of map ar: id, nick, password, email, registered_datetime)
     * @throws SQLException When nick contains illegal characters or it empty / null
     */
    public static UserEntity getUserInfo(String nick) throws SQLException, ParseException {
        List<Map<String, String>> result = db.get(SELECT_USER_BY_NICK, nick);
        if (result.size() > 0) {
            UserEntity userInfo = new UserEntity();
            userInfo.setId(Long.valueOf(result.get(0).get("id")));
            userInfo.setNick(result.get(0).get("nick"));
            userInfo.setEmail(result.get(0).get("email"));
            userInfo.setEncryptedPassword(result.get(0).get("password"));
            userInfo.setRegistered(result.get(0).get("registered_datetime"));
            return userInfo;
        } else {
            return null;
        }
    }

}
