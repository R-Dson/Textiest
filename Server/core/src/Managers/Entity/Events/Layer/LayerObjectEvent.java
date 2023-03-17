package Managers.Entity.Events.Layer;

import Data.Objects.WorldObject;
import Managers.Entity.LayerEvent;
import Managers.Network.UserIdentity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class LayerObjectEvent implements LayerEvent {

    private final Collection<UserIdentity> identities;
    private final ArrayList<WorldObject> objects;

    public LayerObjectEvent(Collection<UserIdentity> identities, ArrayList<WorldObject> objects)
    {
        this.identities = identities;
        this.objects = objects;
    }

    @Override
    public void eventUpdate() {
        for (UserIdentity i : identities)
        {
            i.sendObjectSignal(objects);
        }
    }
}
