package tests;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.zoltor.model.Room;
import org.zoltor.model.User;
import org.zoltor.model.entities.RoomEntity;
import org.zoltor.model.entities.UserEntity;

import java.sql.SQLException;
import java.text.ParseException;

import static org.junit.Assert.*;
import static org.zoltor.common.Config.db;

/**
 * Created by zoltor on 23.10.14.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Dummy {

    @BeforeClass
    public static void a1_refreshTestData() throws SQLException {
        db.update("DELETE FROM rel_users_rooms WHERE user_id = (SELECT id FROM users WHERE nick = 'test')");
        db.update("DELETE FROM rel_users_rooms WHERE room_id = (SELECT id FROM rooms WHERE name = 'testroom')");
        db.update("DELETE FROM rooms WHERE name = 'testroom'");
        db.update("DELETE FROM users WHERE nick = 'test'");
    }

    @Test
    public void a2_userRegister() throws SQLException, ParseException {
        db.update("DELETE FROM users WHERE nick = 'test'");
        UserEntity newUser = new UserEntity();
        newUser.setNick("test");
        newUser.setPassword("pass");
        newUser.setEmail("test@localhost");
        User.registerUser(newUser);
        newUser = User.getUserInfo("test");
        assertEquals("Wrong user name", "test", newUser.getNick());
        assertEquals("Wrong user email", "test@localhost", newUser.getEmail());
        assertEquals("Wrong user id", false, String.valueOf(newUser.getId()).isEmpty());
    }

    @Test
    public void a3_createRoom() throws SQLException {
        RoomEntity room = new RoomEntity();
        room.setHostUser(User.getUserInfo("test"));
        room.setName("testroom");
        room.setPassword("");
        room.setActive(true);
        room.setPrivate(false);
        Room.startNewGame(room);
        room = Room.getRoomInfo(Room.getActiveRoomIdByName("testroom"));
        assertEquals("Wrong room host nick", "test", room.getHostUser().getNick());
        assertEquals("Wrong room host email", "test@localhost", room.getHostUser().getEmail());
        assertEquals("Wrong room name", "testroom", room.getName());
        assertEquals("Wrong room access", false, room.isPrivate());
        assertEquals("Wrong room state", true, room.isActive());
    }

    @Test
    public void a4_gameForUser() throws SQLException {
        Long roomId = User.getRoomIdForUser("test");
        assertEquals("No game for user 'test' or rom id worg: " + String.valueOf(roomId), true, roomId > -1);
    }

}
