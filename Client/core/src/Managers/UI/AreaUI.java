package Managers.UI;

import Data.updatePackageToServerDummy;
import DataShared.Item.Item;
import DataShared.Network.NetworkMessages.Client.InteractObjectRequest;
import DataShared.Network.NetworkMessages.Server.SentWorldObject;
import DataShared.Player.PlayerData;
import Managers.Scenes.MainScene;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.*;

import java.util.ArrayList;


public class AreaUI extends VisTable {
    private final VisTable usersTable;
    private final VisLabel locationText;
    private final VisTable connectedMapsTable;
    private final VisTable worldObjectsTable;
    private final VisTable enemiesTable;
    private final Stage stage;
    private final MainScene mainScene;
    VisScrollPane userPanel;

    public AreaUI(boolean vertical, Stage stage, MainScene mainScene)
    {
        super(vertical);
        this.stage = stage;
        this.mainScene = mainScene;

        locationText = new VisLabel();
        worldObjectsTable = new VisTable();
        usersTable = new VisTable();
        connectedMapsTable = new VisTable();
        enemiesTable = new VisTable();

        add(locationText).fillX();//.row();
        add(new VisLabel("Players")).fillX();
        add(new VisLabel("Monsters")).fillX();
        add(new VisLabel("Nearby areas")).fillX().row();

        worldObjectsTable.setWidth(300);
        add(worldObjectsTable).fill().expandY();
        add(usersTable).expand().fill().expandY();
        add(enemiesTable).expand().row();
        add(connectedMapsTable).fill().expandY().row();
    }

    public void setLocationText(String text)
    {
        locationText.setText("Zone: " + text);
    }

    public VisTable getConnectedMapsTable() {
        return connectedMapsTable;
    }

    public VisTable getWorldObjectsTable() {
        return worldObjectsTable;
    }

    public VisScrollPane getUserPanel() {
        return userPanel;
    }

    public VisTable getUsersTable() {
        return usersTable;
    }
}
