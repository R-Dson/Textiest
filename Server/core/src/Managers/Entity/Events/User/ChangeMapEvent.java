package Managers.Entity.Events.User;

import DataShared.Network.NetworkMessages.Server.ChangeMapFromServer;
import DataShared.Network.NetworkMessages.Server.UpdatePackage;
import Managers.Entity.UserEvent;
import Managers.Map.Map;

public class ChangeMapEvent implements UserEvent {

    private final Map currentMap;

    public ChangeMapEvent(Map currentMap)
    {
        this.currentMap = currentMap;
    }

    @Override
    public void addEventToPackage(UpdatePackage updatePackage) {
        ChangeMapFromServer cm = new ChangeMapFromServer();
        cm.ID = currentMap.ID;
        cm.MapName = currentMap.MapName;
        cm.MapType = currentMap.MapType;
        cm.connectedID = currentMap.getConnectedMapsID();
        cm.connectedName = currentMap.getConnectedMapNames();

        updatePackage.changeMapFromServer = cm;
    }
}
