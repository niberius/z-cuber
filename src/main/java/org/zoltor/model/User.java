package org.zoltor.model;

import org.zoltor.common.HelperUtils;
import org.zoltor.model.entities.FormulaEntity;
import org.zoltor.model.entities.ResultEntity;
import org.zoltor.model.entities.RoomEntity;
import org.zoltor.model.entities.UserEntity;
import org.zoltor.model.queries.IUserQueries;

import java.sql.SQLException;
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
    public static UserEntity getUserInfo(String nick) throws SQLException {
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

    /**
     * Get room for autojoin user after page refresh
     * @param nick Nick of user
     * @return -1 if no active room exists or room_id of active room where user is joined
     * @throws SQLException Nick contains wrong symbols or it's null
     */
    public static long getRoomIdForUser(String nick) throws SQLException {
        UserEntity userInfo = getUserInfo(nick);
        List<Map <String, String>> result = db.get(SELECT_GET_GAME_ID_FOR_USER, userInfo.getId(), userInfo.getId());
        if (result.size() > 0) {
            return Long.valueOf(result.get(0).get("id"));
        } else {
            return -1;
        }
    }

    /**
     * Get user results in specified room
     * @param forUser User info in UserEntity to refresh result statistics
     * @param inRoom Room info in RoomEntity to gather statistic
     * @return UserEntity with refreshed result list
     * @throws SQLException
     */
    public static UserEntity getResult(UserEntity forUser, RoomEntity inRoom) throws SQLException {
        return processUserResultsFromDb(forUser, db.get(SELECT_RESULTS_FOR_ROOM, forUser.getId(), inRoom.getId()));
    }

    /**
     * Get user results for all-time he playing
     * @param forUser User info in UserEntity to refresh result statistics
     * @return UserEntity with refreshed result list
     * @throws SQLException
     */
    public static UserEntity getResult(UserEntity forUser) throws SQLException {
        return processUserResultsFromDb(forUser, db.get(SELECT_RESULTS, forUser.getId()));
    }

    /**
     * Post result of game to database
     * @param forUser User info in UserEntity to post result for
     * @param inRoom Room info in RoomEntity to post result for
     * @param result Result info in ResultEntity with Formula info inside
     * @throws SQLException
     */
    public static void setResult(UserEntity forUser, RoomEntity inRoom, ResultEntity result) throws SQLException {
        db.update(INSERT_POST_USER_RESULT, forUser.getId(), inRoom.getId(), result.getFormula().getId(), result.getResultTime());
    }

    ////////////////////////
    // Private methods
    ////////////////////////

    /**
     * Get results of user from DB and set it to list of ResultEntity with results info
     * @param forUser User info in UserEntity to refresh result statistics
     * @param results List of Map with string key-value pair from DB {@link org.zoltor.common.DataBase#get(String, Object...)}
     * @return UserEntity with refreshed result list
     */
    private static UserEntity processUserResultsFromDb(UserEntity forUser, List<Map <String, String>> results) {
        forUser.getResults().removeAll(forUser.getResults());
        for (Map<String, String> result : results) {
            ResultEntity tmpResult = new ResultEntity();
            FormulaEntity tmpFormula = new FormulaEntity();
            tmpFormula.setId(Long.valueOf(result.get("id")));
            tmpFormula.setFormula(result.get("formula"));
            tmpResult.setId(Long.valueOf(result.get("id")));
            tmpResult.setFormula(tmpFormula);
            tmpResult.setResultRegistered(result.get("result_datetime"));
            tmpResult.setResultTime(Long.valueOf(result.get("result_time")));
            forUser.getResults().add(tmpResult);
        }
        return forUser;
    }

}
