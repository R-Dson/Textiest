package Data.Events.received;

import Data.Events.Event;
import Data.Events.Send.InteractNPCEvent;
import DataShared.Network.NetworkMessages.Server.NPCDetails;
import DataShared.Network.NetworkMessages.Server.UpdateNPCs;
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

public class UpdateNearbyNPCsEvent implements Event {
    private final UpdateNPCs updateNPCs;
    private final MainScene mainScene;
    private final AreaUI areaUI;

    public UpdateNearbyNPCsEvent(MainScene mainScene, AreaUI areaUI, UpdateNPCs updateNPCs)
    {
        this.mainScene = mainScene;
        this.updateNPCs = updateNPCs;
        this.areaUI = areaUI;
    }

    @Override
    public void eventUpdate(UpdatePackageToServer updatePackageToServer) {
        VerticalGroup group = new VerticalGroup();
        for (NPCDetails npc : updateNPCs.npcList) {


            IDTextButton btn = new IDTextButton(npc.NPCName);
            btn.setId(npc.NPCID);
            btn.setDisabled(!npc.isUsable);

            ChangeListener object_listener = new ChangeListener(){
                @Override
                public void changed (ChangeEvent event, Actor actor) {
                    if (actor instanceof IDTextButton)
                    {
                        IDTextButton btn = (IDTextButton)actor;
                        int objectID = btn.getId();
                        // TODO: Add options
                        mainScene.addEvent(new InteractNPCEvent(objectID));
                    }
                }
            };
            btn.addListener(object_listener);

            group.addActor(btn);
        }

        VisTable enemiesTable = areaUI.getEnemiesTable();
        enemiesTable.clear();
        // TODO: Filter enemies from friendly npcs
        // TODO: Get info about npc from files
        enemiesTable.add(new VisLabel("NPCs")).center();
        enemiesTable.row();
        VisScrollPane vs = new VisScrollPane(group);
        vs.setFadeScrollBars(false);
        enemiesTable.add(vs).fill().expand();
    }
}
