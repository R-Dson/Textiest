package Managers.Input;

import Data.StaticValues;
import DataShared.Ability.Action;
import DataShared.Ability.ActionManager;
import Managers.FileManager;

import java.util.*;

public class KeyBindManager {

    public static Queue<Action> queuedActions = new LinkedList<>();

    // KeyBindID and ButtonBind
    public HashMap<Integer, ButtonBind> buttonBinds = new HashMap<>();
    float clickTimer = 0;

    public void SetKeyBinds(ActionManager actionManager){
        List<ButtonBind> KeyBinds = FileManager.GetKeyBindings(StaticValues.KeyBindData);
        KeyBinds.forEach(b -> {
            b.setKeyBind(new KeyBind(b.BoundKey));
            b.setAction(actionManager.GetActionByID(b.BoundActionID));
            buttonBinds.put(b.BoundKey, b);
        });
        /*KeyBinds.forEach( keyBind -> {
            buttonBinds.put(keyBind.BoundKey, new ButtonBind(keyBind, actionManager.GetActionByID(keyBind.BoundActionID)));
        });*/
    }

    public void Update(float delta, ArrayList<Integer> data)
    {
        // buttonkey to action
        data.forEach(d -> {
            ButtonBind buttonBind = buttonBinds.get(d);
            if (buttonBind != null && buttonBind.getAction() != null)
                queuedActions.add(buttonBind.getAction());
        });
        clickTimer += delta;
        if (clickTimer > 0.5f)
        {

            clickTimer -= 0.5f;
        }

    }
}
