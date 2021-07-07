package DataShared.Ability;
import DataShared.Player.Class;

public class Ability extends Action {
    // Requirement properties
    public int LevelRequirement;

    // Effect properties
    public int TravelSpeed;
    public int MaxDistance;

    // Damage properties
    public int DamageDuration;
    public int BaseDamage;

    public Class ClassAbility;
    public AbilityType AbilityType;

    @Override
    public void ActionEvent() {
        // Do something when clicked

    }
}

