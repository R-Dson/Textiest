package Data.Events.received;

import Data.Events.Event;
import DataShared.Item.Item;
import DataShared.Network.NetworkMessages.Server.PlayerStatus;
import DataShared.Network.UpdatePackageToServer;
import Managers.UI.SplitWindow;

public class UpdateStatusEvent implements Event {
    private final PlayerStatus playerStatus;
    private final SplitWindow rootWindow;

    public UpdateStatusEvent(PlayerStatus playerStatus, SplitWindow rootWindow)
    {
        this.playerStatus = playerStatus;
        this.rootWindow = rootWindow;
    }

    @Override
    public void eventUpdate(UpdatePackageToServer updatePackageToServer) {
        if (playerStatus == null)
        {
            if (!rootWindow.isDefaultTitle())
                rootWindow.resetDefaultTitle();
            return;
        }
        String objectName = Item.ObjectNameToText(playerStatus.objectName);
        switch (playerStatus.interactObjectType) {
            case TREE:
                rootWindow.updateTitle("Chopping: " + objectName);
                break;
            case ORE:
                rootWindow.updateTitle("Mining: " + objectName);
                break;
        }
    }
}
