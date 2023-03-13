package Managers.Input;

import DataShared.Ability.Action;

public class ButtonBind {

    // private
    private KeyBind keyBind;
    private Action action;

    // public
    public int BoundKey;
    public int BoundActionID;

    public ButtonBind(KeyBind keyBind, Action action){
        this.keyBind = keyBind;
        this.action = action;
    }

    public ButtonBind(){
        keyBind = new KeyBind();
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public void setKeyBind(KeyBind keyBind) {
        this.keyBind = keyBind;
    }

    public Action getAction(){
        return action;
    }

    public int getBoundKey() {
        return keyBind.BoundKey;
    }

}
