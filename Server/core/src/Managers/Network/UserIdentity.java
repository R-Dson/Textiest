package Managers.Network;

import Data.FixedValues;
import Data.Objects.ObjectActivity;
import Data.Objects.WorldObject;
import Data.Party;
import DataShared.Network.NetworkMessages.Server.*;
import DataShared.Player.PlayerData;
import Managers.Chat.ChatMessage;
import Managers.Entity.Events.User.*;
import Managers.Entity.UserEvent;
import Managers.Map.Map;
import Managers.Map.MapLayer;

import com.badlogic.ashley.core.Entity;
import com.vaniljstudio.server.ServerClass;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;

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

    private final ArrayList<ChatMessage> newMessages;
    private final LinkedHashSet<UserEvent> updateEvents;


    public UserIdentity(String UniqueID, int connectionID){
        this.UniqueID = UniqueID;
        this.connectionID = connectionID;
        newMessages = new ArrayList<>();
        friendsUniqueID = new ArrayList<>();
        ignoreUniqueID = new ArrayList<>();
        updateEvents = new LinkedHashSet<>();
    }

    public void Update(float delta) {
        timer += delta;
        //Creates new package with data
        UpdatePackage updatePackage = new UpdatePackage();
        //Sets data

        Iterator<UserEvent> ui = updateEvents.iterator();
        if (updateEvents.size() > 0)
        {
            while (ui.hasNext()) {
                UserEvent e = ui.next();
                e.addEventToPackage(updatePackage);

                ui.remove();
            }

            ServerClass.GameServer.getServer().sendToTCP(connectionID, updatePackage);

        }


        if (timer >= FixedValues.UpdateFrequency5)
        {

            timer -= FixedValues.UpdateFrequency5;
        }

    }

    public void RemoveUserIdentityFromLayer(){
        currentLayer.RemoveUserFromLayer(this);
    }


    // PLAYER

    public void sendUpdateDataSignal()
    {
        updateEvents.add(new UserUpdateEvent(playerData));
    }

    public void addPlayerMessages(ArrayList<ChatMessage> newMessages)
    {
        this.newMessages.addAll(newMessages);
        updateEvents.add(new MessageEvent(this.newMessages));
        this.newMessages.clear();
    }

    public void sendObjectSignal(ArrayList<WorldObject> worldObjects)
    {
        updateEvents.add(new ObjectEvent(worldObjects));
    }

    // ZONE

    public void sendChangeMap()
    {
        updateEvents.add(new ChangeMapEvent(currentMap));
    }

    public void setObjectActivity(ObjectActivity objectActivity) {
        updateEvents.add(new PlayerStatusEvent(objectActivity));
    }

    // PARTY

    public void addToParty(Party party)
    {
        this.party = party;
        updateParty();
    }

    public void removeParty()
    {
        party = null;
        updateParty();
    }

    public void updateParty()
    {
        if (party != null)
            updateEvents.add(new PartyEvent(party.getUserNames()));
        else
            updateEvents.add(new PartyEvent(null));

    }

    // SOCIAL

    public void addIgnore(String uniqueID)
    {
        ignoreUniqueID.add(uniqueID);
        updateEvents.add(new IgnoreEvent(ignoreUniqueID));
    }

    public void removeIgnore(String uniqueID)
    {
        ignoreUniqueID.add(uniqueID);
        updateEvents.add(new IgnoreEvent(ignoreUniqueID));
    }

    public void addFriend(String uniqueID)
    {
        friendsUniqueID.add(uniqueID);
        updateEvents.add(new FriendsEvent(friendsUniqueID));
    }

    public void removeFriend(String uniqueID)
    {
        friendsUniqueID.remove(uniqueID);
        updateEvents.add(new FriendsEvent(friendsUniqueID));
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof UserIdentity)
            return ((UserIdentity) o).connectionID == this.connectionID;
        return false;
    }

}
