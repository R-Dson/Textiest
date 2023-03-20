package Data.Events.Send;

import Data.Events.Event;
import DataShared.Network.NetworkMessages.Client.AddUserRequest;
import DataShared.Network.UpdatePackageToServer;

public class AddUserEvent implements Event {
    private final String uniqueID;

    public AddUserEvent(String uniqueID)
    {
        this.uniqueID = uniqueID;
    }

    @Override
    public void eventUpdate(UpdatePackageToServer updatePackageToServer) {
        updatePackageToServer.addUserRequest = new AddUserRequest();
        updatePackageToServer.addUserRequest.UniqueUserID = uniqueID;
    }
}
