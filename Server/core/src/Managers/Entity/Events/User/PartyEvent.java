package Managers.Entity.Events.User;

import DataShared.Network.NetworkMessages.Server.UpdatePackage;
import DataShared.Network.NetworkMessages.Server.UpdateParty;
import Managers.Entity.UserEvent;

import java.util.ArrayList;

public class PartyEvent implements UserEvent {
    private ArrayList<String> partyNames;
    public PartyEvent(ArrayList<String> partyNames)
    {
        this.partyNames = partyNames;
    }

    @Override
    public void addEventToPackage(UpdatePackage updatePackage) {
        if (partyNames != null) {
            updatePackage.updateParty = new UpdateParty();
            updatePackage.updateParty.PartyList = partyNames;
        }
    }

}
