package DataShared.Network;

import DataShared.Network.NetworkMessages.Client.*;
import DataShared.Network.NetworkMessages.Client.Chat.InteractWithNPC;
import DataShared.Network.NetworkMessages.Client.Chat.SendMessage;

import java.util.ArrayList;

public class UpdatePackageToServer {
    //All info in the update from client to server goes here
    public ArrayList<Integer> inputsAsIntegers = new ArrayList<>();
    public ArrayList<SendMessage> messages;
    public ChangeMapFromClient changeMapFromClient;
    public InteractObjectRequest interactObjectRequest;
    public PartyInviteRequest partyInviteRequest;
    public AddUserRequest addUserRequest;
    public IgnoreUserRequest ignoreUserRequest;
    public InteractWithNPC interactWithNPC;
}
