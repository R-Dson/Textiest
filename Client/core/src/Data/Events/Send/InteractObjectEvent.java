package Data.Events.Send;

import Data.Events.Event;
import DataShared.Network.NetworkMessages.Client.InteractObjectRequest;
import DataShared.Network.UpdatePackageToServer;

public class InteractObjectEvent implements Event {
    private final int objectID;
    public InteractObjectEvent(int objectID)
    {
        this.objectID = objectID;
    }

    @Override
    public void eventUpdate(UpdatePackageToServer updatePackageToServer) {
        updatePackageToServer.interactObjectRequest = new InteractObjectRequest();
        updatePackageToServer.interactObjectRequest.objectID = objectID;
    }
}
