package Managers.Input;

import DataShared.Ability.Ability;
import DataShared.Ability.AbilityManager;

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
