package leikr.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

/**
 * The class to handle keyboard interactions within Leikr programs
 *
 * @author Torbuntu
 */
public class LeikrKeyboard {

    private static LeikrKeyboard instance;

    public static LeikrKeyboard getLeikrKeyboard() {
        if (instance == null) {
            instance = new LeikrKeyboard();
        }
        return instance;
    }

    /**
     * Returns boolean of if the value of a key is pressed. Examples: "K",
     * "Enter", "Left Shift"
     *
     * @param key
     * @return
     */
    public boolean key(String key) {
        return Gdx.input.isKeyPressed(Input.Keys.valueOf(key));
    }

    /**
     * Returns boolean of if the value of a key is just pressed. Will run once
     * when key is pressed, unlike key which will poll continuously.
     *
     * @see key() Examples: "K", "Enter", "Left Shift"
     * @param key
     * @return
     */
    public boolean keyPress(String key) {
        return Gdx.input.isKeyJustPressed(Input.Keys.valueOf(key));
    }
}
