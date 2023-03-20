package Data.Events.Send;

import Data.Events.Event;
import DataShared.Network.NetworkMessages.Client.PartyInviteRequest;
import DataShared.Network.UpdatePackageToServer;

public class InvitePartyUniqueIDEvent implements Event {

    private final String UniqueID;
    public InvitePartyUniqueIDEvent(String UniqueID)
    {
        this.UniqueID = UniqueID;
    }

    @Override
    public void eventUpdate(UpdatePackageToServer updatePackageToServer) {
        updatePackageToServer.partyInviteRequest = new PartyInviteRequest();
        updatePackageToServer.partyInviteRequest.UniqueID = UniqueID;
    }
}
