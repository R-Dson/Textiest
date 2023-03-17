package Managers.Entity.Events.User;

import DataShared.Network.NetworkMessages.Server.UpdateFriends;
import DataShared.Network.NetworkMessages.Server.UpdatePackage;
import Managers.Entity.UserEvent;
import Managers.EntityManager;
import com.badlogic.gdx.Gdx;

import java.util.ArrayList;

public class IgnoreEvent implements UserEvent {
    private final ArrayList<String> ignoreUniqueID;
    public IgnoreEvent(ArrayList<String> ignoreUniqueID)
    {
        this.ignoreUniqueID = ignoreUniqueID;
    }

    @Override
    public void addEventToPackage(UpdatePackage updatePackage) {
        try{
            updatePackage.updateFriends = new UpdateFriends();
            updatePackage.updateFriends.FriendsList = new ArrayList<>();
            ignoreUniqueID.forEach(f -> updatePackage.updateFriends.FriendsList.add(EntityManager.getEntityByUniqueID(f).UserName));
        }
        catch (NullPointerException e)
        {
            Gdx.app.log("ERROR", "Failed to add friend.");
        }
    }
}
