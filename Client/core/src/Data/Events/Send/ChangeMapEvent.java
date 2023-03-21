package Data.Events.Send;

import Data.Events.Event;
import DataShared.Network.NetworkMessages.Client.ChangeMapFromClient;
import DataShared.Network.UpdatePackageToServer;

public class ChangeMapEvent implements Event {

    private final int mapID;

    public ChangeMapEvent(int mapID)
    {
        this.mapID = mapID;
    }

    @Override
    public void eventUpdate(UpdatePackageToServer updatePackageToServer) {
        updatePackageToServer.changeMapFromClient = new ChangeMapFromClient();
        updatePackageToServer.changeMapFromClient.mapID = this.mapID;
    }
}
