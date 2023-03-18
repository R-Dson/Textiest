package Managers.Scenes;

import Data.StaticValues;
import Data.updatePackageToServerDummy;
import DataShared.Item.EquipmentItem;
import DataShared.Item.Item;
import DataShared.Item.ItemEnums;
import DataShared.Network.NetworkMessages.Client.AddUserRequest;
import DataShared.Network.NetworkMessages.Client.ChangeMapFromClient;
import DataShared.Network.NetworkMessages.Client.IgnoreUserRequest;
import DataShared.Network.NetworkMessages.Client.PartyInviteRequest;
import DataShared.Network.NetworkMessages.Server.*;
import DataShared.Network.NetworkMessages.ChatMessage;
import DataShared.Network.UpdatePackageToServer;
import DataShared.Player.PlayerData;
import Managers.CameraManager;
import Managers.InputManager;
import Managers.Map.MapManager;
import Managers.PlayerManager;
import Managers.UI.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.esotericsoftware.kryonet.Client;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.*;

import java.util.ArrayList;

public class MainScene extends Scene {

    private MapManager _MapManager;
    private CameraManager _CameraManager;
    private InputManager _InputManager;
    private Client _Client;
    private ChatUI chatWindow;
    private SocialUI socialUI;
    private TabType currentTabType;
    SplitWindow rootWindow;
    VisTable utilWindow;
    VisTable ItemWindow;
    VisTable rightTopTable, rightBottomTable;
    CombatUI combat;
    AreaUI area;

    //private Vis

    public MainScene(Client client){
        super();
        _Client = client;
        ExtendViewport viewport = new ExtendViewport((int)_CameraManager.get_Camera().viewportWidth, (int)_CameraManager.get_Camera().viewportHeight);
        stage.setViewport(viewport);
        stage.getViewport().update((int)_CameraManager.get_Camera().viewportWidth, (int)_CameraManager.get_Camera().viewportHeight, true);
        currentTabType = TabType.INVENTORY;
    }

    public void updateUsers(ArrayList<PlayerData> users)
    {
        area.updateOtherUsers(users);
    }

    public void updateData()
    {
        ItemWindow.clear();
        switch (currentTabType) {
            case STATS:
                break;
            case INVENTORY:
                buildInventoryUI();
                break;
            case GEAR:
                buildGearUI();
                break;
        }
    }

    public void updateLocation()
    {
        area.setLocationText(PlayerManager.playerData.LastMultiLocation);
    }

    public void updateLocation(ChangeMapFromServer changeMap)
    {
        area.setLocationText(changeMap.MapName);
        area.setConnectedMaps(changeMap.connectedName, changeMap.connectedID);
        //updateObjects(changeMap.worldObjects);
    }

    public void updateObjects(ArrayList<SentWorldObject> sentWorldObjects)
    {
        area.setWorldObjects(sentWorldObjects);
    }

    public void updateFriends(UpdateFriends updateFriends)
    {
        if (updateFriends != null)
            socialUI.updateUsersFriendsList(updateFriends.FriendsList);
    }

    public void updateIgnore(UpdateIgnore updateIgnore)
    {
        if (updateIgnore != null)
            socialUI.updateUsersIgnoreList(updateIgnore.IgnoreList);
    }

    public void updateParty(UpdateParty updateParty)
    {
        if (updateParty != null)
            socialUI.updateUsersParty(updateParty.PartyList);
    }

    @Override
    public void render(SpriteBatch batch) {
        try {
            _CameraManager.render(batch);
            if (_CameraManager.get_Camera() != null){
                stage.getBatch().begin();

                stage.getBatch().end();
            }
            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();
        }
        catch (IllegalStateException | NullPointerException | ArrayIndexOutOfBoundsException e)
        {

        }
    }

    private float timer = 0;

    @Override
    public void update(float delta) {
        //_InputManager.Update(delta);
        timer += delta;
        if (true)//(timer > StaticValues.updateFrequency)
        {
            UpdatePackageToServer updatePackageToServer = new UpdatePackageToServer();

            //move dummy values to package object
            updatePackageToServer.inputsAsIntegers = updatePackageToServerDummy.inputsAsIntegers;
            updatePackageToServer.messages = chatWindow.getToBeSent();

            if (updatePackageToServerDummy.newMapId != -1) {
                ChangeMapFromClient changeMapFromClient = new ChangeMapFromClient();
                changeMapFromClient.mapID = updatePackageToServerDummy.newMapId;
                updatePackageToServer.changeMapFromClient = changeMapFromClient;

                updatePackageToServerDummy.resetMapID();
            }

            if (updatePackageToServerDummy.getInteractObjectType() != null)
                updatePackageToServer.interactObjectRequest = updatePackageToServerDummy.getInteractObjectType();

            String uniqueID = area.getAddUniqueID();
            if (uniqueID != null)
            {
                updatePackageToServer.addUserRequest = new AddUserRequest();
                updatePackageToServer.addUserRequest.UniqueUserID = uniqueID;
            }
            area.setAddUniqueID(null);

            uniqueID = area.getIgnoreUniqueID();
            if (uniqueID != null)
            {
                updatePackageToServer.ignoreUserRequest = new IgnoreUserRequest();
                updatePackageToServer.ignoreUserRequest.uniqueID = uniqueID;
            }

            uniqueID = area.getInvitePartyUniqueID();
            if (uniqueID != null)
            {
                updatePackageToServer.partyInviteRequest = new PartyInviteRequest();
                updatePackageToServer.partyInviteRequest.UniqueID = uniqueID;
            }


            //Send data
            _Client.sendTCP(updatePackageToServer);
            chatWindow.clearMessages();

            //clear the current values in the dummy
            updatePackageToServerDummy.resetInputList();
            updatePackageToServerDummy.setInteractObjectType(null);

            timer -= StaticValues.updateFrequency;
        }

    }

    @Override
    public void create(){
        table.setFillParent(true);
        //Independent new functions
        stage.getActors().forEach(Actor::remove);
        _MapManager = new MapManager();
        _CameraManager = new CameraManager();
        _InputManager = new InputManager();

        _CameraManager.create();
        //group.addActor(new VisLabel(String.valueOf(PlayerManager.playerData.CurrentHealth)));

        rootWindow = new SplitWindow("User: " + PlayerManager.playerData.UserName, false);
        rootWindow.setFillParent(true);
        rootWindow.setResizeBorder(10);
        rootWindow.setResizable(true);
        rootWindow.setMovable(false);
        //rootWindow.setFillParent(true);

        table.addActor(rootWindow);

        buildUI();
        //table.add(new VisLabel(PlayerManager.playerData.UserName));

        //Gdx.input.setInputProcessor(_InputManager);
        //create functions
        /*
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                //_MapManager.create("Maps/map.tmx");
                //_CameraManager.create(_MapManager);
            }
        });*/
    }

    private void buildUI()
    {
        buildLeft();
        buildRight();
    }

    private void buildLeft()
    {
        area = new AreaUI(true, stage);
        combat = new CombatUI(true);
        SplitWindow leftSplit = new SplitWindow("", true, area, combat);
        rootWindow.getLeftTable().add(leftSplit).expand().fill();

    }

    private void buildRight()
    {
        SplitWindow rightSplit = new SplitWindow("", true);
        rightTopTable = rightSplit.getTopTable();
        rightBottomTable = rightSplit.getBottomTable();

        utilWindow = new VisTable();
        rightTopTable.add(utilWindow).fill().expand();

        // UTILITIES
        buildTabs();
        ItemWindow = new VisTable();
        utilWindow.add(ItemWindow).expand().fill();

        //buildInventoryUI();

        // CHAT
        chatWindow = new ChatUI("Chat");
        socialUI = new SocialUI();
        rightBottomTable.add(chatWindow).fill().expand();
        rightBottomTable.add(socialUI).fillY().expandY();

        rootWindow.getRightTable().add(rightSplit).expand().fill();
    }

    public void addMessages(ChatMessages chatMessages)
    {
        if (chatMessages == null)
            return;

        for (ChatMessage message : chatMessages.messages) {
            chatWindow.appendMessage(message);
        }
    }


    private void buildTabs()
    {
        HorizontalGroup group = new HorizontalGroup();
        final VisTextButton tabStats = new VisTextButton("Stats");
        final VisTextButton tabInventory = new VisTextButton("Inventory");
        final VisTextButton tabGear = new VisTextButton("Gear");

        group.addActor(tabStats);
        group.addActor(tabGear);
        group.addActor(tabInventory);

        ChangeListener tab_listener = new ChangeListener(){
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                ItemWindow.clear();
                tabStats.setDisabled(false);
                tabGear.setDisabled(false);
                tabInventory.setDisabled(false);

                if (actor.equals(tabStats)){
                    currentTabType = TabType.STATS;
                    ItemWindow.add(new VisLabel("STATS HERE")).fill().expand();
                    tabStats.setDisabled(true);
                }
                else if (actor.equals(tabGear)) {
                    currentTabType = TabType.GEAR;
                    buildGearUI();
                    tabGear.setDisabled(true);
                }
                else if (actor.equals(tabInventory)) {
                    currentTabType = TabType.INVENTORY;
                    buildInventoryUI();
                    tabInventory.setDisabled(true);
                }
            }
        };

        tabInventory.setDisabled(true);

        tabStats.addListener(tab_listener);
        tabGear.addListener(tab_listener);
        tabInventory.addListener(tab_listener);

        utilWindow.add(group).expandX().fillX().row();

    }

    private void buildGearUI() {
        int i = 0;
        for (EquipmentItem item : PlayerManager.playerData.Equipment) {
            ItemEnums.EquipmentType equipmentType = ItemEnums.EquipmentType.values()[i];
            if (item != null)
                ItemWindow.add(new VisLabel(equipmentType.toString() + ": " + item.Name)).fill().expand().pad(5);
            else
                ItemWindow.add(new VisLabel(equipmentType.toString() + ": " + "Empty")).fill().expand().pad(5);
            if (i % 3 == 0)
                ItemWindow.row();
            i++;
        }
    }

    private void buildInventoryUI()
    {
        int i = 0;
        for (Item item : PlayerManager.playerData.Inventory) {
            if (item != null)
                ItemWindow.add(new VisLabel(item.Name)).fill().expand().pad(5);
            else
                ItemWindow.add(new VisLabel("Empty")).fill().expand().pad(5);
            if (i % 5 == 0)
                ItemWindow.row();
            i++;
        }

    }

    @Override
    public void resize(int width, int height) {
        if (width == 0 && height == 0) return;
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        VisUI.dispose();
    }

    public void updateStatus(PlayerStatus playerStatus) {
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

    private enum TabType{
        STATS,
        INVENTORY,
        GEAR
    }
}
