package Managers.Entity.Events.Layer;

import Managers.Entity.LayerEvent;
import Managers.Network.UserIdentity;

public class CombatInitEvent implements LayerEvent {
    private final int id;
    private final UserIdentity userIdentity;

    public CombatInitEvent(int id, UserIdentity userIdentity)
    {
        this.id = id;
        this.userIdentity = userIdentity;
    }

    @Override
    public void eventUpdate() {

    }
}
