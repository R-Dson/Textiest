package Managers.Entity.Events.User;

import DataShared.Network.NetworkMessages.Server.UpdateFriends;
import DataShared.Network.NetworkMessages.Server.UpdatePackage;
import Managers.Entity.UserEvent;
import Managers.EntityManager;
import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.HashSet;

public class FriendsEvent implements UserEvent {
    private final HashSet<String> friendsUniqueID;
    public FriendsEvent(HashSet<String> friendsUniqueID)
    {
        this.friendsUniqueID = friendsUniqueID;
    }

    @Override
    public void addEventToPackage(UpdatePackage updatePackage) {
        try{
            if (friendsUniqueID.size() < 1)
                return;
            updatePackage.updateFriends = new UpdateFriends();
            updatePackage.updateFriends.FriendsList = new ArrayList<>();
            friendsUniqueID.forEach(f -> updatePackage.updateFriends.FriendsList.add(EntityManager.getEntityByUniqueID(f).UserName));
            int a = 2;
        }
        catch (NullPointerException e)
        {
            Gdx.app.log("ERROR", "Failed to add friend.");
        }
    }
}
