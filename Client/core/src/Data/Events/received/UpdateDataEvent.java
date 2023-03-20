package Data.Events.received;

import Data.Events.Event;
import DataShared.Network.UpdatePackageToServer;
import Managers.Scenes.MainScene;

public class UpdateDataEvent implements Event {
    private final MainScene mainScene;

    public UpdateDataEvent(MainScene mainScene)
    {
        this.mainScene = mainScene;
    }

    @Override
    public void eventUpdate(UpdatePackageToServer updatePackageToServer) {
        mainScene.getItemWindow().clear();

        switch (mainScene.getCurrentTabType()) {
            case STATS:
                break;
            case INVENTORY:
                mainScene.buildInventoryUI();
                break;
            case GEAR:
                mainScene.buildGearUI();
                break;
        }
    }
}
