package Data.Events.Send;

import Data.Events.Event;
import DataShared.Network.NetworkMessages.Client.IgnoreUserRequest;
import DataShared.Network.UpdatePackageToServer;

public class IgnoreUserEvent implements Event {
    private final String uniqueID;
    public IgnoreUserEvent(String uniqueID)
    {
        this.uniqueID = uniqueID;
    }

    @Override
    public void eventUpdate(UpdatePackageToServer updatePackageToServer) {
        updatePackageToServer.ignoreUserRequest = new IgnoreUserRequest();
        updatePackageToServer.ignoreUserRequest.uniqueID = uniqueID;
    }
}
