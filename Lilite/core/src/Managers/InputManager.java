package Managers;

import Data.UpdatePackageToServer;
import Data.updatePackageToServerDummy;
import Managers.Input.KeyBind;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import java.util.ArrayList;
import java.util.List;

public class InputManager implements ApplicationListener, InputProcessor {

    private int OldKey = -1;
    private ArrayList<Integer> pressedKeys = new ArrayList<>();

    @Override
    public void create() {

    }

    @Override
    public void resize(int width, int height) {

    }

    public void Update(float delta){
        for (Integer integer:pressedKeys) {
            //Once per click event
            if (integer != OldKey)
            {

            }

            //Spam event
            updatePackageToServerDummy.inputsAsIntegers.add(integer);
            OldKey = integer;
        }

        System.out.println(updatePackageToServerDummy.inputsAsIntegers.size());
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
