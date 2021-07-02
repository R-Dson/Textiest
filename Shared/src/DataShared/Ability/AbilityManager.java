package DataShared.Ability;

import java.util.ArrayList;
import java.util.List;
import DataShared.Files.FileManagerShared;
import DataShared.SharedStaticValues;

public class AbilityManager {

    //TODO: Change to HashMap with Unique IDs
    private List<Ability> Abilities = new ArrayList<>();

    public void LoadAbilities(){
        Abilities = FileManagerShared.GetAbilities(SharedStaticValues.AbilityData);
    }

    public Ability GetAbilityByID(int ID){
        for (Ability ability : Abilities) {
            if (ability.ID == ID)
                return ability;
        }
        return null;
    }
}