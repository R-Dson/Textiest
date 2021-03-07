package Managers.Input;

import Managers.Ability.Ability;
import Managers.Ability.AbilityManager;

public class KeyBind {
    public Ability BoundAbility;
    public int BoundKey;
    public int BoundAbilityID;

    /*public KeyBind(int BindedKey, int BindedAbilityID){
        this.BoundKey = BindedKey;
        this.BoundAbilityID = BindedAbilityID;
    }*/

    private void SetAbility(AbilityManager abilityManager) {
        abilityManager.GetAbilityByID(BoundAbilityID);
    }
}
