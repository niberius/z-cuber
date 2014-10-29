package org.zoltor.model;

import org.zoltor.model.entities.RoomEntity;
import org.zoltor.model.entities.UserEntity;
import org.zoltor.model.queries.IRoomQueries;

import java.sql.SQLException;
import java.util.ArrayList;
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
            // TODO Refactor : get all info from joined cols
            UserEntity joinedUser = User.getUserInfo(room.get("user_nick"));
            roomInfo.getJoinedUsers().add(joinedUser);
        }
        return roomInfo;
    }

    /**
     * Get id of active (not closed) room by it name
     * @param roomName Room name
     * @return -1 if room not found. Otherwise - id of room
     * @throws SQLException Something going wrong
     */
    public static long getActiveRoomIdByName(String roomName) throws SQLException {
        String roomIdAsString = "";
        try {
            roomIdAsString = db.get(SELECT_GET_ACTIVE_ROOM_ID_BY_NAME, roomName).get(0).get("id");
        } catch (Throwable e) {
            // :(
        }
        return (roomIdAsString.isEmpty()) ? -1 : Long.valueOf(roomIdAsString);
    }

    /**
     * Get active rooms list
     * @return List with active rooms info
     * @throws SQLException
     */
    public static List<RoomEntity> getActiveRoomsList() throws SQLException {
        List<RoomEntity> result = new ArrayList<RoomEntity>();
        List<Map<String, String>> roomsIds = db.get(SELECT_ACTIVE_ROOMS_IDS);
        for (Map<String, String> roomId : roomsIds) {
            result.add(getRoomInfo(Long.valueOf(roomId.get("id"))));
        }
        return result;
    }

    /**
     * Leave room by user. If user is game hoster - all other users will be leave
     * @param user UserEntity with info of user which leaving
     * @param room RoomEntity with room nfo which user leaved
     * @throws SQLException
     */
    public static void leaveRoom(UserEntity user, RoomEntity room) throws SQLException {
        if (room.getHostUser().getId() == user.getId()) {
            db.update(DELETE_ALL_ON_HOST_LEAVE, room.getId());
            db.update(UPDATE_CLOSE_GAME, room.getId());
        } else {
            db.update(DELETE_LEAVE_GAME, room.getId(), user.getId());
        }
    }
}
