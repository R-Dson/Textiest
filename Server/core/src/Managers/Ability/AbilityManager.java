package Managers.Ability;

import Data.FixedValues;
import Managers.FileManager;

import java.util.ArrayList;
import java.util.List;

public class AbilityManager {

    private List<Ability> Abilities = new ArrayList<>();

    public void LoadAbilities(){
        Abilities = FileManager.GetAbilities(FixedValues.AbilityData);
    }

    public Ability GetAbilityByID(int ID){
        for (Ability ability : Abilities) {
            if (ability.ID == ID)
                return ability;
        }
        return null;
    }
}
