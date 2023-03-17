package DataShared.Network.NetworkMessages.Server;

import DataShared.Player.PlayerData;
import java.util.ArrayList;

public class UpdatePackage {
    //All info in the update from server to user goes here
    public PlayerData receiverData;
    public ArrayList<PlayerData> otherPlayers;
    public ChatMessages newMessages;
    public ChangeMapFromServer changeMapFromServer;
    public PlayerStatus playerStatus;
    public UpdateObjects updateObjects;
    public UpdateParty updateParty;
    public UpdateFriends updateFriends;
    public UpdateIgnore updateIgnore;
}
