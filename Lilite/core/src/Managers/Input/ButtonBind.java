package Managers.Input;

import DataShared.Ability.Action;

public class ButtonBind {

    //private
    private KeyBind keyBind;
    private Action action;

    public ButtonBind(KeyBind keyBind, Action action){
        this.keyBind = keyBind;
        this.action = action;
    }

    public Action getAction(){
        return action;
    }

    public int getKeyBindActionID() {
        return keyBind.BoundActionID;
    }

    public int getBoundKey() {
        return keyBind.BoundKey;
    }

}
