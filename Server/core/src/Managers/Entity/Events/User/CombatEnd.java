package Managers.Entity.Events.User;

import Data.Combat.CombatActivity;
import DataShared.Network.NetworkMessages.Server.UpdateCombat;
import DataShared.Network.NetworkMessages.Server.UpdatePackage;
import Managers.Entity.UserEvent;
import Managers.Map.MapLayer;
import Managers.Network.UserIdentity;

import java.util.HashSet;

public class CombatEnd implements UserEvent {
    private final HashSet<UserIdentity> userIdentities;
    private final MapLayer layer;
    private final CombatActivity combatActivity;

    public CombatEnd(HashSet<UserIdentity> userIdentities, CombatActivity combatActivity, MapLayer layer)
    {
        this.layer = layer;
        this.userIdentities = userIdentities;
        this.combatActivity = combatActivity;
    }

    @Override
    public void addEventToPackage(UpdatePackage updatePackage) {
        updatePackage.updateCombat = new UpdateCombat();
        layer.removeCombatActivity(combatActivity);

    }
}
