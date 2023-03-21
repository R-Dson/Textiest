package Data.Events.Send;

import Data.Events.Event;
import DataShared.Network.NetworkMessages.Client.Chat.InteractWithNPC;
import DataShared.Network.UpdatePackageToServer;

public class InteractNPCEvent implements Event {

    private final int npcID;

    public InteractNPCEvent(int npcID)
    {
        this.npcID = npcID;
    }

    @Override
    public void eventUpdate(UpdatePackageToServer updatePackageToServer) {
        updatePackageToServer.interactWithNPC = new InteractWithNPC();
        updatePackageToServer.interactWithNPC.NPCID = npcID;
    }
}
