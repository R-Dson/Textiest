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
import com.badlogic.gdx.Gdx;
import com.vaniljstudio.server.ServerClass;

import java.util.*;

public class UserIdentity extends Entity {
    public String UniqueID;
    public int connectionID;
    public PlayerData playerData;
    public MapLayer currentLayer;
    public Map currentMap;
    public String UserName;
    public ArrayList<String> ignoreUniqueIDs;
    private Party party;
    private float timer = 0;

    private final ArrayList<ChatMessage> newMessages;
    private final LinkedHashSet<UserEvent> updateEvents;


    public UserIdentity(String UniqueID, int connectionID){
        this.UniqueID = UniqueID;
        this.connectionID = connectionID;
        newMessages = new ArrayList<>();
        ignoreUniqueIDs = new ArrayList<>();
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

    public void sendUpdateFriends()
    {
        updateEvents.add(new FriendsEvent(playerData.friendsUniqueIDs));
    }

    public void sendUpdateIgnore()
    {
        updateEvents.add(new IgnoreEvent(ignoreUniqueIDs));
    }

    // ZONE

    public void sendChangeMap()
    {
        updateEvents.add(new ChangeMapEvent(currentMap));
        currentLayer.sendObjectLayerUpdate();
    }

    public void setObjectActivity(ObjectActivity objectActivity) {
        updateEvents.add(new PlayerStatusEvent(objectActivity));
    }

    public void sendNearbyPlayerChange(Collection<UserIdentity> userIdentities)
    {
        updateEvents.add(new OtherUsersEvent(userIdentities, this));
    }

    // PARTY

    public void addToParty(Party party)
    {
        this.party = party;
        sendUpdateParty();
    }

    public void removeParty()
    {
        party = null;
        sendUpdateParty();
    }

    public void sendUpdateParty()
    {
        if (party != null)
            updateEvents.add(new PartyEvent(party.getUserNames()));
        else
            updateEvents.add(new PartyEvent(null));

    }

    // SOCIAL

    public void addIgnore(String uniqueID)
    {
        if (!ignoreUniqueIDs.add(uniqueID))
            return;
        sendUpdateIgnore();
        Gdx.app.log("STATUS", "Added Ignore.");
    }

    public void removeIgnore(String uniqueID)
    {
        ignoreUniqueIDs.add(uniqueID);
        sendUpdateIgnore();
    }

    public void addFriend(String uniqueID)
    {
        Gdx.app.log("STATUS", "Attempting to add friend.");
        if (!playerData.friendsUniqueIDs.add(uniqueID))
            return;
        sendUpdateFriends();
        Gdx.app.log("STATUS", "Added friend.");
    }

    public void removeFriend(String uniqueID)
    {
        playerData.friendsUniqueIDs.remove(uniqueID);
        sendUpdateFriends();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof UserIdentity)
            return ((UserIdentity) o).connectionID == this.connectionID;
        return false;
    }

}
