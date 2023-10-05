package Managers;

import com.badlogic.gdx.Gdx;

public class Logger {

    public static void log(ErrorLevel errorLevel, String logMessage)
    {
        Gdx.app.log(errorLevel.name(), logMessage);
    }

    public static enum ErrorLevel {
        CRITICAL,
        MODEST
    }

}
