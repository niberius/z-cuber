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
import java.util.List;

import static org.junit.Assert.*;
import static org.zoltor.common.Config.db;

/**
 * Created by zoltor on 23.10.14.
 * TODO More negative test
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Dummy {

    @BeforeClass
    public static void a1_refreshTestData() throws SQLException {
        db.update("DELETE FROM rel_users_rooms WHERE user_id = (SELECT id FROM users WHERE nick LIKE '%test%')");
        db.update("DELETE FROM rel_users_rooms WHERE room_id = (SELECT id FROM rooms WHERE name LIKE '%testroom%')");
        db.update("DELETE FROM rooms WHERE name LIKE '%testroom%'");
        db.update("DELETE FROM users WHERE nick LIKE '%test%'");
    }

    @Test
    public void a2_testUserRegistration1() throws SQLException {
        UserEntity newUser = new UserEntity();
        newUser.setNick("testa2");
        newUser.setPassword("pass");
        newUser.setEmail("testa2@localhost");
        User.registerUser(newUser);
        newUser = User.getUserInfo("testa2");
        assertEquals("Wrong user name", "testa2", newUser.getNick());
        assertEquals("Wrong user email", "testa2@localhost", newUser.getEmail());
        assertEquals("Wrong user id", false, String.valueOf(newUser.getId()).isEmpty());
    }

    @Test
    public void a3_testUserRegistration2() throws SQLException {
        UserEntity newUser = new UserEntity();
        newUser.setNick("testa3");
        newUser.setPassword("pass");
        newUser.setEmail("testa3@localhost");
        User.registerUser(newUser);
        newUser = User.getUserInfo("testa3");
        assertEquals("Wrong user name", "testa3", newUser.getNick());
        assertEquals("Wrong user email", "testa3@localhost", newUser.getEmail());
        assertEquals("Wrong user id", false, String.valueOf(newUser.getId()).isEmpty());
    }

    @Test(expected = SQLException.class)
    public void a4_testUserRegistrationDuplicateNick() throws SQLException {
        UserEntity newUser = new UserEntity();
        newUser.setNick("testa3");
        newUser.setPassword("pass");
        newUser.setEmail("testa4@localhost");
        User.registerUser(newUser);
    }

    @Test(expected = SQLException.class)
    public void a5_testUserRegistrationDuplicateEmail() throws SQLException {
        UserEntity newUser = new UserEntity();
        newUser.setNick("testa5");
        newUser.setPassword("pass");
        newUser.setEmail("testa2@localhost");
        User.registerUser(newUser);
    }

    @Test
    public void a6_testCreateRoom() throws SQLException {
        RoomEntity room = new RoomEntity();
        room.setHostUser(User.getUserInfo("testa2"));
        room.setName("testroom");
        room.setPassword("");
        room.setActive(true);
        room.setPrivate(false);
        Room.startNewGame(room);
        room = Room.getRoomInfo(Room.getActiveRoomIdByName("testroom"));
        assertEquals("Wrong room host nick", "testa2", room.getHostUser().getNick());
        assertEquals("Wrong room host email", "testa2@localhost", room.getHostUser().getEmail());
        assertEquals("Wrong room name", "testroom", room.getName());
        assertEquals("Wrong room access", false, room.isPrivate());
        assertEquals("Wrong room state", true, room.isActive());
    }

    @Test
    public void a7_testGameForUser() throws SQLException {
        UserEntity creator =  User.getUserInfo("testa2");
        UserEntity joined =  User.getUserInfo("testa3");
        Long roomId = User.getRoomIdForUser(creator.getNick());
        assertEquals("No game for user 'test' or room id wrong: " + String.valueOf(roomId), true, roomId > -1);
        RoomEntity room = Room.getRoomInfo(roomId);
        Room.joinGame(joined, room);
        roomId = User.getRoomIdForUser(joined.getNick());
        assertEquals("No game for user 'test' or room id wrong: " + String.valueOf(roomId), true, roomId > -1);
        Room.leaveRoom(joined, room);
        roomId = User.getRoomIdForUser(joined.getNick());
        assertEquals("No game for user 'test' or room id wrong: " + String.valueOf(roomId), true, roomId == -1);
        roomId = User.getRoomIdForUser(creator.getNick());
        assertEquals("No game for user 'test' or room id wrong: " + String.valueOf(roomId), true, roomId > -1);
    }

    @Test
    public void a8_testRoomListAndFinishGame() throws SQLException {
        List<RoomEntity> rooms = Room.getActiveRoomsList();
        assertEquals("No active rooms found", true, rooms.size() > 0);
        UserEntity creator =  User.getUserInfo("testa2");
        Room.leaveRoom(creator, rooms.get(0));
        rooms = Room.getActiveRoomsList();
        assertEquals("No active rooms found", true, rooms.size() == 0);
        long roomId = User.getRoomIdForUser(creator.getNick());
        assertEquals("No game for user 'test' or room id wrong: " + String.valueOf(roomId), true, roomId == -1);
    }

}
