package DataShared.Network;

import DataShared.Network.NetworkMessages.Client.ChangeMapFromClient;
import DataShared.Network.NetworkMessages.Client.InteractObjectRequest;
import DataShared.Network.NetworkMessages.Client.SendMessage;

import java.util.ArrayList;

public class UpdatePackageToServer {
    //All info in the update from client to server goes here
    public ArrayList<Integer> inputsAsIntegers = new ArrayList<>();
    public ArrayList<SendMessage> messages;
    public ChangeMapFromClient changeMapFromClient;
    public InteractObjectRequest interactObjectRequest;
}
