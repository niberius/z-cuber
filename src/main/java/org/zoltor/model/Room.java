package org.zoltor.model;

import org.zoltor.model.entities.RoomEntity;
import org.zoltor.model.entities.UserEntity;
import org.zoltor.model.queries.IRoomQueries;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static org.zoltor.common.Config.db;

/**
 * Created by zoltor on 22.10.14.
 */
public class Room implements IRoomQueries {

    /**
     * Begin new game
     * @param roomInfo RoomEntity with information about room
     * @throws SQLException Some errors in args
     */
    public static void startNewGame(RoomEntity roomInfo) throws SQLException {
        db.update(INSERT_START_NEW_GAME_BY_USER_NICK, roomInfo.getHostUser().getId(), roomInfo.getName(), roomInfo.getEncryptedPassword(), roomInfo.isPrivate());
    }

    /**
     * Join user to room
     * @param user User described in UserEntity which should be joined to game
     * @param room Room described in RoomEntity
     * @throws SQLException Some errors in args
     */
    public static void joinGame(UserEntity user, RoomEntity room) throws SQLException {
        db.update(INSERT_JOIN_GAME_BY_NICK, user.getId(), room.getId());
    }

    /**
     * Get information about active room (game NOT closed)
     * @param roomId Id of room
     * @return RoomEntity with information about room, host user and joined users
     * @throws SQLException
     */
    public static RoomEntity getRoomInfo(long roomId) throws SQLException {
        RoomEntity roomInfo = new RoomEntity();
        List<Map<String, String>> rooms = db.get(SELECT_ROOM_INFO, roomId);
        boolean isMainInfoFetched = false;
        for (Map<String, String> room : rooms) {
            if (!isMainInfoFetched) {
                isMainInfoFetched = true;
                UserEntity host = User.getUserInfo(room.get("host_nick"));
                roomInfo.setHostUser(host);
                roomInfo.setId(Long.valueOf(room.get("id")));
                roomInfo.setName(room.get("name"));
                roomInfo.setEncryptedPassword(room.get("password"));
                roomInfo.setPrivate(room.get("is_private").equals("1"));
                roomInfo.setActive(room.get("is_active").equals("1"));
                roomInfo.setCreated(room.get("created_datetime"));
            }
            UserEntity joinedUser = User.getUserInfo(room.get("user_nick"));
            roomInfo.getJoinedUsers().add(joinedUser);
        }
        return roomInfo;
    }

    public static long getActiveRoomIdByName(String roomName) throws SQLException {
        String roomIdAsString = "";
        try {
            roomIdAsString = db.get(SELECT_GET_ACTIVE_ROOM_ID_BY_NAME, roomName).get(0).get("id");
        } catch (Throwable e) {
            // :(
        }
        return (roomIdAsString.isEmpty()) ? -1 : Long.valueOf(roomIdAsString);
    }
}
