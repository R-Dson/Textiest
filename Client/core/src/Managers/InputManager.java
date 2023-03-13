package Managers;

import Data.updatePackageToServerDummy;
import DataShared.Ability.Action;
import Managers.Input.KeyBindManager;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.InputProcessor;
import com.vaniljstudio.textiest.Textiest;

import java.util.ArrayList;

public class InputManager implements ApplicationListener, InputProcessor {

    private int OldKey = -1;
    private int OldActionID = -1;
    private ArrayList<Integer> pressedKeys = new ArrayList<>();

    private KeyBindManager keyBindManager;

    @Override
    public void create() {
    }

    @Override
    public void resize(int width, int height) {

    }

    public void Update(float delta){
        boolean found = false;

        Textiest.DataManager.Update(delta, pressedKeys);

        for (Action action: KeyBindManager.queuedActions) {
            //Once per click event
            if (action.ID != OldActionID)
            {

            }

            // Adding the action to network package
            updatePackageToServerDummy.inputsAsIntegers.add(action.ID);

            OldActionID = action.ID;
            KeyBindManager.queuedActions.clear();
        }

    }

    @Override
    public void render() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean keyDown(int keycode) {
        pressedKeys.add(keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        // remove?
        pressedKeys.remove(Integer.valueOf(keycode));
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
