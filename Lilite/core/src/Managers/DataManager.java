package Managers;

import Data.StaticValues;
import Managers.Ability.AbilityManager;
import Managers.Animation.AnimationTextures;
import Managers.Input.KeybindManager;

public class DataManager {
    private AbilityManager _abilityManager;
    private KeybindManager _keybindManager;
    public static TextureManager _textureManager;

    public DataManager(){
        _abilityManager = new AbilityManager();
        _keybindManager = new KeybindManager();
        _textureManager = new TextureManager();

        //Abilities
        _abilityManager.LoadAbilities();

        //Keybinds
        _keybindManager.SetKeyBinds();
        _keybindManager.AssignAbility(_abilityManager);

        //Textures
        _textureManager.LoadTextures();
    }

}
