package Managers.UI;

import Data.updatePackageToServerDummy;
import DataShared.Item.Item;
import DataShared.Network.NetworkMessages.Client.InteractObjectRequest;
import DataShared.Network.NetworkMessages.Server.PlayerStatus;
import DataShared.Network.NetworkMessages.Server.SentWorldObject;
import DataShared.Player.PlayerData;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.*;

import java.util.ArrayList;
import java.util.Iterator;


public class AreaUI extends VisTable {
    private VisTable usersTable;
    private VisLabel locationText;
    private VisTable connectedMapsTable;
    private VisTable worldObjectsTable;
    private Stage stage;

    VisScrollPane userPanel;

    private String addUniqueID;
    private String IgnoreUniqueID;
    private String InvitePartyUniqueID;

    public AreaUI(boolean vertical, Stage stage)
    {
        super(vertical);
        this.stage = stage;
        locationText = new VisLabel();
        worldObjectsTable = new VisTable();
        usersTable = new VisTable();
        connectedMapsTable = new VisTable();

        add(locationText).expandX().fillX().row();
        add(worldObjectsTable).fill().expand();
        add(usersTable).expand().fill().expand();
        add(connectedMapsTable).expand().fill().expand().row();
    }

    public void updateOtherUsers(ArrayList<PlayerData> others)
    {
        if (others == null)
            return;

        if (userPanel != null && userPanel.getChildren().size < 2 && others.size() == 0)
            return;

        usersTable.clear();
        VerticalGroup group = new VerticalGroup();
        group.addActor(new VisLabel("Players:"));

        for (PlayerData pd : others) {
            VisTextButton visTextButton = new VisTextButton(pd.UserName);

            visTextButton.addListener(new LocalPlayerButtonListener(pd.UserName, pd.UniqueUserID, stage, this));

            group.addActor(visTextButton);

        }

        userPanel = new VisScrollPane(group);
        userPanel.setFadeScrollBars(false);
        usersTable.add(userPanel).fill().expand();
        userPanel.setWidth(100);

    }

    public void setWorldObjects(ArrayList<SentWorldObject> worldObjects)
    {
        VerticalGroup group = new VerticalGroup();
        for (SentWorldObject worldObject : worldObjects) {
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
                        InteractObjectRequest interactObjectRequest = new InteractObjectRequest();
                        interactObjectRequest.objectID = objectID;
                        updatePackageToServerDummy.setInteractObjectType(interactObjectRequest);
                    }
                }
            };
            btn.addListener(object_listener);

            group.addActor(btn);
        }
        worldObjectsTable.clear();
        worldObjectsTable.add(new VisLabel("Objects")).center();
        worldObjectsTable.row();
        VisScrollPane vs = new VisScrollPane(group);
        vs.setFadeScrollBars(false);
        worldObjectsTable.add(vs).fill().expand();

    }

    public void setLocationText(String text)
    {
        locationText.setText("Zone: " + text);
    }

    public void setConnectedMaps(ArrayList<String> maps, ArrayList<Integer> mapIDs)
    {
        connectedMapsTable.clear();
        connectedMapsTable.add(new VisLabel("Nearby areas:")).expandX().fillX().row();

        ChangeListener zone_listener = new ChangeListener(){
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                if (actor instanceof IDTextButton)
                {
                    IDTextButton btn = (IDTextButton)actor;
                    int mapID = btn.getId();
                    updatePackageToServerDummy.setNewMapId(mapID);
                }
            }
        };

        for (int i = 0; i < maps.size(); i++) {

            IDTextButton btn = new IDTextButton(maps.get(i));
            btn.setId(mapIDs.get(i));
            btn.addListener(zone_listener);
            connectedMapsTable.add(btn).expandX().fillX().row();
        }

    }

    public String getAddUniqueID() {
        String returnS = addUniqueID;
        addUniqueID = null;
        return returnS;
    }

    public void setAddUniqueID(String addUniqueID) {
        this.addUniqueID = addUniqueID;
    }

    public String getIgnoreUniqueID() {
        String returnS = IgnoreUniqueID;
        IgnoreUniqueID = null;
        return returnS;
    }

    public void setIgnoreUniqueID(String ignoreUniqueID) {
        IgnoreUniqueID = ignoreUniqueID;
    }

    public String getInvitePartyUniqueID() {
        String returnS = InvitePartyUniqueID;
        InvitePartyUniqueID = null;
        return returnS;
    }

    public void setInvitePartyUniqueID(String invitePartyUniqueID) {
        InvitePartyUniqueID = invitePartyUniqueID;
    }
}
