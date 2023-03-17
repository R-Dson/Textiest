package Managers.Entity.Events.User;

import DataShared.Network.NetworkMessages.Server.UpdateFriends;
import DataShared.Network.NetworkMessages.Server.UpdatePackage;
import Managers.Entity.UserEvent;
import Managers.EntityManager;
import com.badlogic.gdx.Gdx;

import java.util.ArrayList;

public class FriendsEvent implements UserEvent {
    private final ArrayList<String> friendsUniqueID;
    public FriendsEvent(ArrayList<String> friendsUniqueID)
    {
        this.friendsUniqueID = friendsUniqueID;
    }

    @Override
    public void addEventToPackage(UpdatePackage updatePackage) {
        try{
            updatePackage.updateFriends = new UpdateFriends();
            updatePackage.updateFriends.FriendsList = new ArrayList<>();
            friendsUniqueID.forEach(f -> updatePackage.updateFriends.FriendsList.add(EntityManager.getEntityByUniqueID(f).UserName));
        }
        catch (NullPointerException e)
        {
            Gdx.app.log("ERROR", "Failed to add friend.");
        }
    }
}
