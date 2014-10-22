package org.zoltor.common;

import java.io.File;

/**
 * Created by zoltor on 22.10.14.
 * Class with common config constants and object instances
 */
public interface Config {
    // Logger instance
    static Logger logger = Logger.getInstance();

    // Path to root project directory
    static String ROOT_DIR = System.getProperty("basedir", "");

    // Database config
    static String DB_DRIVER_NAME = "org.sqlite.JDBC";
    static String DB_JDBC_URL = "jdbc:sqlite:" + new File(ROOT_DIR).getAbsolutePath() + File.separator + "data.db";
}
