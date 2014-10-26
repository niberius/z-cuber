package org.zoltor.model;

import org.zoltor.common.HelperUtils;
import org.zoltor.model.queries.IRoomQueries;

import java.sql.SQLException;
import java.util.Map;

import static org.zoltor.common.Config.db;

/**
 * Created by zoltor on 22.10.14.
 */
public class Room implements IRoomQueries {

    /**
     * Begin new game
     * @param userNick nick of user host
     * @param roomName name for room
     * @param password password for room (if not empty - room will be private with password access to it)
     * @throws SQLException Some errors in args
     */
    public void startNewGame(String userNick, String roomName, String password) throws SQLException {
        boolean isPrivate = password.isEmpty();
        password = (password.isEmpty()) ? "" : HelperUtils.getMd5Digest(password);
        db.update(INSERT_START_NEW_GAME_BY_USER_NICK, userNick, roomName, password, isPrivate);
    }

    /**
     * Join to game
     * @param userNick User nick which should be joined to game
     * @param roomId Id of room to join
     * @throws SQLException Some errors in args
     */
    public void joinGame(String userNick, int roomId) throws SQLException {
        db.update(INSERT_JOIN_GAMY_BY_NICK, userNick, roomId);
    }

    /**
     * Get information about active room (game NOT closed)
     * @param roomId Id of room
     * @return Map<String,String> with values of room. Keys of map:
     *         id - roomId, host_id - id of host user, host_nick - nickname of host user,
     * @throws SQLException
     */
    public Map<String, String> getRoomInfo(int roomId) throws SQLException {
        return db.get(SELECT_ROOM_INFO, roomId).get(0);
    }

}
