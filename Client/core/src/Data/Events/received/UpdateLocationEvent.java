package Data.Events.received;

import Data.Events.Event;
import DataShared.Network.NetworkMessages.Server.ChangeMapFromServer;
import DataShared.Network.UpdatePackageToServer;
import Managers.Scenes.MainScene;
import Managers.UI.AreaUI;
import Managers.UI.IDTextButton;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisTable;

import java.util.ArrayList;

public class UpdateLocationEvent implements Event {
    private final AreaUI areaUI;
    private final ArrayList<String> connectedNames;
    private final ArrayList<Integer> IDs;
    private final ChangeMapFromServer changeMap;
    private final MainScene mainScene;
    public UpdateLocationEvent(MainScene mainScene, AreaUI areaUI, ChangeMapFromServer changeMap)
    {
        this.mainScene = mainScene;
        this.areaUI = areaUI;
        this.connectedNames = changeMap.connectedName;
        this.IDs = changeMap.connectedID;
        this.changeMap = changeMap;
    }

    @Override
    public void eventUpdate(UpdatePackageToServer updatePackageToServer) {
        VisTable connectedMapsTable = areaUI.getConnectedMapsTable();

        connectedMapsTable.clear();

        ChangeListener zone_listener = new ChangeListener(){
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                if (actor instanceof IDTextButton)
                {
                    IDTextButton btn = (IDTextButton)actor;
                    int mapID = btn.getId();
                    mainScene.addEvent(new ChangeMapEvent(mapID));
                }
            }
        };

        for (int i = 0; i < connectedNames.size(); i++) {

            IDTextButton btn = new IDTextButton(connectedNames.get(i));
            btn.setId(IDs.get(i));
            btn.addListener(zone_listener);
            connectedMapsTable.add(btn).expand().fillX().row();
        }

        areaUI.setLocationText(changeMap.MapName);
    }


}
