package Managers.Entity.Events.User;

import DataShared.Network.NetworkMessages.Server.UpdatePackage;
import Managers.Entity.UserEvent;
import Managers.Network.UserIdentity;

import java.util.ArrayList;
import java.util.Collection;

public class OtherUsersEvent implements UserEvent {

    private Collection<UserIdentity> userIdentities;
    private UserIdentity reciever;

    public OtherUsersEvent(Collection<UserIdentity> userIdentities, UserIdentity userIdentity)
    {
        this.userIdentities = userIdentities;
        reciever = userIdentity;
    }

    @Override
    public void addEventToPackage(UpdatePackage updatePackage) {
        updatePackage.otherPlayers = new ArrayList<>();
        for (UserIdentity userIdentity : userIdentities)
            if (!userIdentity.equals(reciever))
                updatePackage.otherPlayers.add(userIdentity.playerData);
    }
}
