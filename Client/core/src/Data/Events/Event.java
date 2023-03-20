package Data.Events;

import DataShared.Network.UpdatePackageToServer;

public interface Event {
    public void eventUpdate(UpdatePackageToServer updatePackageToServer);

}
