package Managers.Network;

import Data.FixedValues;
import Data.Objects.ObjectActivity;
import Data.Objects.Ore;
import Data.Objects.Tree;
import Data.Objects.WorldObject;
import Data.Party;
import DataShared.Network.NetworkMessages.Server.*;
import DataShared.Player.PlayerData;
import Managers.Chat.ChatMessage;
import Managers.EntityManager;
import Managers.Map.Map;
import Managers.Map.MapLayer;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.vaniljstudio.server.ServerClass;

import java.util.ArrayList;

public class UserIdentity extends Entity {
    public String UniqueID;
    public int connectionID;
    public PlayerData playerData;
    public MapLayer currentLayer;
    public Map currentMap;
    public String UserName;
    public ArrayList<String> friendsUniqueID;
    public ArrayList<String> ignoreUniqueID;
    private Party party;
    private float timer = 0;
    private ArrayList<ChatMessage> newMessages;
    private ObjectActivity objectActivity;
    private boolean updatedUserInfo;
    private boolean changeMap;
    private ArrayList<String> partyNames;
    private boolean leaveParty;
    private boolean updateFriends;
    private boolean updateIgnore;


    public UserIdentity(String UniqueID, int connectionID){
        this.UniqueID = UniqueID;
        this.connectionID = connectionID;
        newMessages = new ArrayList<>();
        updatedUserInfo = true;
        changeMap = true;
        leaveParty = false;
        updateFriends = true;
        updateIgnore = true;
        friendsUniqueID = new ArrayList<>();
        ignoreUniqueID = new ArrayList<>();
    }

    public void RemoveUserIdentityFromLayer(){
        currentLayer.RemoveUserFromLayer(this);
    }

    public void Update(float delta) {
        timer += delta;
        //Creates new package with data
        UpdatePackage updatePackage = new UpdatePackage();
        //Sets data

        if (updatedUserInfo) {
            updatePackage.receiverData = playerData;
            updatedUserInfo = false;
        }

        FillOtherUsers(updatePackage);

        updateMessages(updatePackage);

        if (changeMap) {
            changeMap(updatePackage);
            updateMapObjects(updatePackage);
            changeMap = false;
        }
        else if (currentLayer.getUpdatedObjects())
            updateMapObjects(updatePackage);


        if (objectActivity != null) {
            updateStatus(updatePackage);
        }

        if (leaveParty)
        {
            leaveParty(updatePackage);
            leaveParty = false;
        }
        else if(party != null)
        {
            updatePartyNames(updatePackage);
        }

        if (updateFriends)
        {
            updateFriends(updatePackage);
            updateFriends = false;
        }


        ServerClass.GameServer.getServer().sendToTCP(connectionID, updatePackage);
        //System.out.println("Sent data to " + connectionID);

        if (timer >= FixedValues.UpdateFrequency5)
        {

            timer -= FixedValues.UpdateFrequency5;
        }

        //TODO SEND INFORMATION TO USER

    }

    public void setUpdatedUserInfo(boolean updatedUserInfo) {
        this.updatedUserInfo = updatedUserInfo;
    }

    public void addPlayerMessages(ArrayList<ChatMessage> newMessages)
    {
        this.newMessages.addAll(newMessages);
    }

    private void updateMessages(UpdatePackage updatePackage)
    {
        ArrayList<DataShared.Network.NetworkMessages.ChatMessage> msgs = new ArrayList<>();
        for (ChatMessage newMessage : newMessages) {
            DataShared.Network.NetworkMessages.ChatMessage chatMessage = new DataShared.Network.NetworkMessages.ChatMessage();
            chatMessage.ChatMessage = newMessage.message;
            chatMessage.UserName = newMessage.userIdentity.UserName;
            chatMessage.date = newMessage.date;
            chatMessage.chatEnum = newMessage.chatEnum;
            msgs.add(chatMessage);
        }
        newMessages.clear();
        ChatMessages cm = new ChatMessages();
        cm.messages = msgs;
        updatePackage.newMessages = cm;
    }

    private void changeMap(UpdatePackage updatePackage)
    {
        ChangeMapFromServer cm = new ChangeMapFromServer();
        cm.ID = currentMap.ID;
        cm.MapName = currentMap.MapName;
        cm.MapType = currentMap.MapType;
        cm.connectedID = currentMap.getConnectedMapsID();
        cm.connectedName = currentMap.getConnectedMapNames();

        updatePackage.changeMapFromServer = cm;
    }

    public void updateMapObjects(UpdatePackage updatePackage)
    {
        UpdateObjects updateObjects = new UpdateObjects();
        updateObjects.sentWorldObjects = new ArrayList<>();

        for (WorldObject layerObject : currentLayer.getLayerObjects()) {
            SentWorldObject swo = new SentWorldObject();
            swo.isUsable = layerObject.getUsable();
            swo.objectID = layerObject.getObjectID();
            swo.objectName = layerObject.getObjectName();
            updateObjects.sentWorldObjects.add(swo);
        }

        updatePackage.updateObjects = updateObjects;

    }

    public void updateStatus(UpdatePackage updatePackage)
    {
        PlayerStatus playerStatus = new PlayerStatus();
        WorldObject object = objectActivity.getWorldObject();
        if (object != null) // should never be null
        {
            playerStatus.objectName = object.getObjectName();
            playerStatus.objectID = object.getObjectID();

            if (object instanceof Ore)
                playerStatus.interactObjectType = PlayerStatus.InteractObjectType.ORE;
            else if (object instanceof Tree)
                playerStatus.interactObjectType = PlayerStatus.InteractObjectType.TREE;

            updatePackage.playerStatus = playerStatus;
        }
    }

    public void setObjectActivity(ObjectActivity objectActivity) {
        this.objectActivity = objectActivity;
    }

    private void FillOtherUsers(UpdatePackage UpdatePackage){
        if(currentLayer != null)
            for (UserIdentity userIdentity : currentLayer.users.values())
                if (!userIdentity.equals(this))
                    UpdatePackage.OtherPlayers.add(userIdentity.playerData);
    }

    public void setParty(Party party)
    {
        this.party = party;
    }

    public void updatePartyNames(UpdatePackage updatePackage)
    {
        ArrayList<String> names = party.getUserNames();
        if (!names.equals(partyNames))
        {
            updatePackage.updateParty = new UpdateParty();
            updatePackage.updateParty.PartyList = names;
            partyNames = names;
        }
    }

    private void updateFriends(UpdatePackage updatePackage)
    {
        try{
            updatePackage.updateFriends = new UpdateFriends();
            updatePackage.updateFriends.FriendsList = new ArrayList<>();
            friendsUniqueID.forEach(f -> updatePackage.updateFriends.FriendsList.add(EntityManager.getEntityByUniqueID(f).UserName));
        }
        catch (NullPointerException e)
        {
            Gdx.app.log("ERROR", "Failed to add friend.");
        }
    }

    private void leaveParty(UpdatePackage updatePackage)
    {
        updatePackage.updateParty = new UpdateParty();
        updatePackage.updateParty.PartyList = new ArrayList<>();
        partyNames.clear();
    }

    public void removeParty()
    {
        party = null;
        leaveParty = true;
    }

    public void AddFriend(String uniqueID)
    {
        friendsUniqueID.add(uniqueID);
        updateFriends = true;
    }

    public void removeFriend(String uniqueID)
    {
        friendsUniqueID.remove(uniqueID);
        updateFriends = true;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof UserIdentity)
            return ((UserIdentity) o).connectionID == this.connectionID;
        return false;
    }

    public boolean getChangeMap()
    {
        return changeMap;
    }

    public void setChangeMap(boolean changeMap) {
        this.changeMap = changeMap;
    }
}
