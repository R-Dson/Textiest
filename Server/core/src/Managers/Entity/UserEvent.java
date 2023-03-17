package Managers.Entity;

import DataShared.Network.NetworkMessages.Server.UpdatePackage;

public interface UserEvent {
    
   /* public Event()
    {
        
    }*/

    public void addEventToPackage(UpdatePackage updatePackage);
}
