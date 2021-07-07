package DataShared.Ability;

import java.util.HashMap;
import java.util.List;

import DataShared.Files.FileManagerShared;
import DataShared.SharedStaticValues;

public class ActionManager {

    //TODO: Change to HashMap with Unique IDs
    private final HashMap<Integer, Action> Actions = new HashMap<>();

    public void LoadActions(){
        List<Ability> Abilities = FileManagerShared.GetAbilities(SharedStaticValues.AbilityData);
        List<Action> actions = FileManagerShared.GetActions(SharedStaticValues.ActionData);
        actions.forEach(a -> Actions.put(a.ID, a));
        Abilities.forEach(a -> Actions.put(a.ID, a));

    }

    public Action GetActionByID(int ID){
        return Actions.get(ID);
    }
}