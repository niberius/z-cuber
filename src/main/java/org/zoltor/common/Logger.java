package org.zoltor.common;

/**
 * Created by zoltor on 23.10.14.
 */
public class Logger {
    private static Logger logger;

    private Logger() {
        // protect from instantiating
    }

    public static Logger getInstance() {
        if (logger == null) {
            logger = new Logger();
        }
        return logger;
    }

    public void info(String message) {
        log("INFO: " + message, false);
    }

    public void warn(String message) {
        log("WARNING: " + message, false);
    }

    public void error(String message) {
        log("ERROR: " + message, true);
    }

    ///////////////////
    // Private methods
    ///////////////////

    private void log(String message, boolean isStdErr) {
        if (isStdErr) {
            System.err.println(message);
        } else {
            System.out.println(message);
        }
    }
}
