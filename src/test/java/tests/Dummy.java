package tests;

import org.junit.Test;
import org.zoltor.model.User;

import java.sql.SQLException;

/**
 * Created by zoltor on 23.10.14.
 */
public class Dummy {

    @Test
    public void dbTest() {
        try {
            //user.registerUser("zoltor", "pass", "zoltor@local.host");
            System.out.println(User.isAuthorized("zoltor", "����E<,�*v�\b#��� "));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void md5DigestTest() {
        //System.out.println(HelperUtils.getMd5Digest("zoltor", "pass", "zoltor@local.host"));
    }
}
