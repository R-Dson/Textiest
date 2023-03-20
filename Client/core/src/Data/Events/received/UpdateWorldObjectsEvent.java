package Data.Events.received;

import Data.Events.Event;
import DataShared.Item.Item;
import DataShared.Network.NetworkMessages.Server.SentWorldObject;
import DataShared.Network.UpdatePackageToServer;
import Managers.Scenes.MainScene;
import Managers.UI.AreaUI;
import Managers.UI.IDTextButton;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;

import java.util.ArrayList;

public class UpdateWorldObjectsEvent implements Event {
    private final AreaUI areaUI;
    private final ArrayList<SentWorldObject> sentWorldObjects;
    private final MainScene mainScene;
    public UpdateWorldObjectsEvent(MainScene mainScene, AreaUI areaUI, ArrayList<SentWorldObject> sentWorldObjects)
    {
        this.mainScene = mainScene;
        this.areaUI = areaUI;
        this.sentWorldObjects = sentWorldObjects;
    }

    @Override
    public void eventUpdate(UpdatePackageToServer updatePackageToServer) {

        VerticalGroup group = new VerticalGroup();
        for (SentWorldObject worldObject : sentWorldObjects) {
            String name = Item.ObjectNameToText(worldObject.objectName);

            IDTextButton btn = new IDTextButton(name);
            btn.setId(worldObject.objectID);
            btn.setDisabled(!worldObject.isUsable);

            ChangeListener object_listener = new ChangeListener(){
                @Override
                public void changed (ChangeEvent event, Actor actor) {
                    if (actor instanceof IDTextButton)
                    {
                        IDTextButton btn = (IDTextButton)actor;
                        int objectID = btn.getId();
                        mainScene.addEvent(new InteractObjectEvent(objectID));
                    }
                }
            };
            btn.addListener(object_listener);

            group.addActor(btn);
        }
        VisTable worldObjectsTable = areaUI.getWorldObjectsTable();
        worldObjectsTable.clear();
        worldObjectsTable.add(new VisLabel("Objects")).center();
        worldObjectsTable.row();
        VisScrollPane vs = new VisScrollPane(group);
        vs.setFadeScrollBars(false);
        worldObjectsTable.add(vs).fill().expand();
    }
}
