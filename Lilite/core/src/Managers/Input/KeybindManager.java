package Managers.Input;

import Data.StaticValues;
import DataShared.Ability.AbilityManager;
import Managers.FileManager;

import java.util.List;

public class KeybindManager {

    public List<KeyBind> KeyBinds;

    public void SetKeyBinds(){
        KeyBinds = FileManager.GetKeyBindings(StaticValues.KeyBindData);
    }

    public void AssignAbility(AbilityManager abilityManager){
        for (KeyBind keyBind : KeyBinds) {
            keyBind.BoundAbility = abilityManager.GetAbilityByID(keyBind.BoundAbilityID);
        }
    }

}
