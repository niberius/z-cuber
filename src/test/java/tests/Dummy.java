package tests;

import org.junit.Test;
import org.zoltor.common.Config;
import org.zoltor.model.User;

import java.sql.SQLException;
import java.text.ParseException;

/**
 * Created by zoltor on 23.10.14.
 */
public class Dummy {

    @Test
    public void dbTest() throws SQLException, ParseException {
        User.getUserInfo("zoltor1");
        Config.logger.info("hi");
    }
}
