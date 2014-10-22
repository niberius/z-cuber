package tests;

import org.junit.Test;
import org.zoltor.common.DataBase;

/**
 * Created by zoltor on 23.10.14.
 */
public class Dummy {

    @Test
    public void dbTest() {
        DataBase db = DataBase.getInstance();
        db.get("SELECT * FROM users");
    }
}
