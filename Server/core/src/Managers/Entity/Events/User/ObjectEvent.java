package Managers.Entity.Events.User;

import Data.Objects.WorldObject;
import DataShared.Network.NetworkMessages.Server.SentWorldObject;
import DataShared.Network.NetworkMessages.Server.UpdateObjects;
import DataShared.Network.NetworkMessages.Server.UpdatePackage;
import Managers.Entity.UserEvent;

import java.util.ArrayList;

public class ObjectEvent implements UserEvent {

    private final ArrayList<WorldObject> worldObjects;

    public ObjectEvent(ArrayList<WorldObject> worldObjects)
    {
        this.worldObjects = worldObjects;
    }

    @Override
    public void addEventToPackage(UpdatePackage updatePackage) {
        UpdateObjects updateObjects = new UpdateObjects();
        updateObjects.sentWorldObjects = new ArrayList<>();

        for (WorldObject layerObject : worldObjects) {
            SentWorldObject swo = new SentWorldObject();
            swo.isUsable = layerObject.getUsable();
            swo.objectID = layerObject.getObjectID();
            swo.objectName = layerObject.getObjectName();
            updateObjects.sentWorldObjects.add(swo);
        }

        updatePackage.updateObjects = updateObjects;
    }
}
