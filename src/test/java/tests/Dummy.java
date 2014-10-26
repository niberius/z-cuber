package tests;

import org.junit.Test;
import org.zoltor.common.DataBase;
import org.zoltor.common.HelperUtils;
import org.zoltor.model.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by zoltor on 23.10.14.
 */
public class Dummy {

    @Test
    public void dbTest() {
        try {
            //user.registerUser("zoltor", "pass", "zoltor@local.host");
            System.out.println(User.isAutorized("zoltor", "����E<,�*v�\b#��� "));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void md5DigestTest() {
        //System.out.println(HelperUtils.getMd5Digest("zoltor", "pass", "zoltor@local.host"));
    }
}
