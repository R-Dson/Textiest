package Managers.Entity.Events.User;

import DataShared.Network.NetworkMessages.Server.UpdatePackage;
import DataShared.Player.PlayerData;
import Managers.Entity.UserEvent;

public class UserUpdateEvent implements UserEvent {

    private final PlayerData playerData;

    public UserUpdateEvent(PlayerData playerData)
    {
        this.playerData = playerData;
    }

    @Override
    public void addEventToPackage(UpdatePackage updatePackage) {
        updatePackage.receiverData = playerData;
    }
}
