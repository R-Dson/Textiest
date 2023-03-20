package Managers.Scenes;

import Data.Events.*;
import Data.Events.received.*;
import Data.StaticValues;
import Data.updatePackageToServerDummy;
import DataShared.Item.EquipmentItem;
import DataShared.Item.Item;
import DataShared.Item.ItemEnums;
import DataShared.Network.NetworkMessages.Client.ChangeMapFromClient;
import DataShared.Network.NetworkMessages.Client.PartyInviteRequest;
import DataShared.Network.NetworkMessages.Server.*;
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
import java.util.Iterator;
import java.util.LinkedHashSet;

public class MainScene extends Scene {
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
    AreaUI areaUI;

    //private Vis

    private final LinkedHashSet<Event> updateEvents;

    public MainScene(Client client){
        super();
        currentTabType = TabType.INVENTORY;

        _Client = client;

        updateEvents = new LinkedHashSet<>();

        ExtendViewport viewport = new ExtendViewport((int)_CameraManager.get_Camera().viewportWidth, (int)_CameraManager.get_Camera().viewportHeight);
        stage.setViewport(viewport);
        stage.getViewport().update((int)_CameraManager.get_Camera().viewportWidth, (int)_CameraManager.get_Camera().viewportHeight, true);

    }

    public void updateUsers(ArrayList<PlayerData> users)
    {
        if (users != null)
            addEvent(new UpdateOtherUsersEvent(areaUI, users, this, stage));
    }

    public void updateData()
    {
        addEvent(new UpdateDataEvent(this));
    }

    public void updateLocation()
    {
        areaUI.setLocationText(PlayerManager.playerData.LastMultiLocation);
    }

    public void updateLocation(ChangeMapFromServer changeMap)
    {
        if (changeMap != null) {
            addEvent(new UpdateLocationEvent(this, areaUI, changeMap));
            addEvent(new UpdateDataEvent(this));
        }
    }

    public void updateObjects(ArrayList<SentWorldObject> sentWorldObjects)
    {
        if (sentWorldObjects != null)
            addEvent(new UpdateWorldObjectsEvent(this, areaUI, sentWorldObjects));
    }

    public void updateFriends(UpdateFriends updateFriends)
    {
        if (updateFriends != null)
            addEvent(new UpdateFriendsEvent(socialUI, updateFriends.FriendsList));
    }

    public void updateIgnore(UpdateIgnore updateIgnore)
    {
        if (updateIgnore != null)
            socialUI.updateUsersIgnoreList(updateIgnore.IgnoreList);
    }

    public void updateParty(UpdateParty updateParty)
    {
        if (updateParty != null)
            addEvent(new UpdateUserPartyEvent(socialUI, updateParty.PartyList));
    }

    @Override
    public void render(SpriteBatch batch) {
        try {
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
        timer += delta;

        if (true)//(timer > StaticValues.updateFrequency)
        {
            UpdatePackageToServer updatePackageToServer = new UpdatePackageToServer();

            Iterator<Event> eventIterator = updateEvents.iterator();
            while (eventIterator.hasNext())
            {
                Event event = eventIterator.next();
                event.eventUpdate(updatePackageToServer);
                eventIterator.remove();
            }

            //move dummy values to package object
            updatePackageToServer.inputsAsIntegers = updatePackageToServerDummy.inputsAsIntegers;
            updatePackageToServer.messages = chatWindow.getToBeSent();

            //Send data
            _Client.sendTCP(updatePackageToServer);
            chatWindow.clearMessages();

            //clear the current values in the dummy
            updatePackageToServerDummy.resetInputList();

            timer -= StaticValues.updateFrequency;
        }

    }

    @Override
    public void create(){
        table.setFillParent(true);
        stage.getActors().forEach(Actor::remove);

        //Independent new functions
        _CameraManager = new CameraManager();
        _InputManager = new InputManager();

        _CameraManager.create();

        rootWindow = new SplitWindow("User: " + PlayerManager.playerData.UserName, false);
        rootWindow.setFillParent(true);
        rootWindow.setResizeBorder(10);
        rootWindow.setResizable(true);
        rootWindow.setMovable(false);

        table.addActor(rootWindow);

        buildUI();
    }

    private void buildUI()
    {
        buildLeft();
        buildRight();
    }

    private void buildLeft()
    {
        areaUI = new AreaUI(true, stage, this);
        combat = new CombatUI(true);
        SplitWindow leftSplit = new SplitWindow("", true, areaUI, combat);
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

        // CHAT
        chatWindow = new ChatUI("Chat");
        socialUI = new SocialUI();
        rightBottomTable.add(chatWindow).fill().expand();
        rightBottomTable.add(socialUI).fillY().expandY();

        rootWindow.getRightTable().add(rightSplit).expand().fill();
    }

    public void addMessages(ChatMessages chatMessages)
    {
        if (chatMessages != null)
            addEvent(new MessageEvent(chatMessages, chatWindow));
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

    public void buildGearUI() {
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

    public void buildInventoryUI()
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
        if (playerStatus != null)
            addEvent(new UpdateStatusEvent(playerStatus, rootWindow));
    }

    public void addEvent(Event event)
    {
        updateEvents.add(event);
    }

    public enum TabType{
        STATS,
        INVENTORY,
        GEAR
    }

    public TabType getCurrentTabType() {
        return currentTabType;
    }

    public VisTable getItemWindow() {
        return ItemWindow;
    }
}
