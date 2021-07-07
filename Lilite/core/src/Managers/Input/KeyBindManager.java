package Managers.Input;

import Data.StaticValues;
import DataShared.Ability.Action;
import DataShared.Ability.ActionManager;
import Managers.FileManager;
import Managers.InputManager;

import java.util.*;

public class KeyBindManager {

    public static Queue<Action> queuedActions = new LinkedList<>();

    // KeyBindID and keyBind
    public HashMap<Integer, ButtonBind> buttonBinds = new HashMap<>();
    float clickTimer = 0;

    public void SetKeyBinds(ActionManager actionManager){
        List<KeyBind> KeyBinds = FileManager.GetKeyBindings(StaticValues.KeyBindData);
        KeyBinds.forEach( keyBind -> {
            buttonBinds.put(keyBind.BoundKey, new ButtonBind(keyBind, actionManager.GetActionByID(keyBind.BoundActionID)));
        });
    }

    public void Update(float delta, ArrayList<Integer> data)
    {
        clickTimer += delta;
        if (clickTimer > 0.5f)
        {
            // add each button press to
            data.forEach(d -> {
                queuedActions.add(buttonBinds.get(d).getAction());
            });
            clickTimer -= 0.5f;
        }

    }
}
