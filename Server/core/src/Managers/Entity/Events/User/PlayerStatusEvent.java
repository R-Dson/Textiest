package Managers.Entity.Events.User;

import Data.Objects.ObjectActivity;
import Data.Objects.Ore;
import Data.Objects.Tree;
import Data.Objects.WorldObject;
import DataShared.Network.NetworkMessages.Server.PlayerStatus;
import DataShared.Network.NetworkMessages.Server.UpdatePackage;
import Managers.Entity.UserEvent;

public class PlayerStatusEvent implements UserEvent {

    private ObjectActivity objectActivity;

    public PlayerStatusEvent(ObjectActivity objectActivity)
    {
        this.objectActivity = objectActivity;
    }

    @Override
    public void addEventToPackage(UpdatePackage updatePackage) {
        PlayerStatus playerStatus = new PlayerStatus();

        if (objectActivity == null || objectActivity.getWorldObject() == null)
        {
            playerStatus.interactObjectType = PlayerStatus.InteractObjectType.NONE;
            playerStatus.objectID = -1;
            playerStatus.objectName = "NullObject";
            return;
        }

        WorldObject object = objectActivity.getWorldObject();
        playerStatus.objectName = object.getObjectName();
        playerStatus.objectID = object.getObjectID();

        if (object instanceof Ore)
            playerStatus.interactObjectType = PlayerStatus.InteractObjectType.ORE;
        else if (object instanceof Tree)
            playerStatus.interactObjectType = PlayerStatus.InteractObjectType.TREE;

        updatePackage.playerStatus = playerStatus;
    }
}
