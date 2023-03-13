package Managers;

import DataShared.Ability.ActionManager;
import Managers.Input.KeyBindManager;

import java.util.ArrayList;

public class DataManager {
    private ActionManager _actionManager;
    private KeyBindManager _keyBindManager;
    public static TextureManager _textureManager;

    public DataManager(){
        _actionManager = new ActionManager();
        //_keyBindManager = new KeyBindManager();
        _textureManager = new TextureManager();

        //Abilities, load these before keybinds
        //_actionManager.LoadActions();

        //Keybinds
        //_keyBindManager.SetKeyBinds(_actionManager);

        //Textures
        //_textureManager.LoadTextures();
    }

    public void Update(float delta, ArrayList<Integer> data){
        //_keyBindManager.Update(delta, data);
    }

}
