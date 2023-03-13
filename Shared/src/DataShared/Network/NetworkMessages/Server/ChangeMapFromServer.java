package DataShared.Network.NetworkMessages.Server;

import DataShared.MapType;

import java.util.ArrayList;

public class ChangeMapFromServer {
    public String MapName;
    public int ID;
    public MapType MapType;
    public ArrayList<Integer> connectedID;
    public ArrayList<String> connectedName;
    //public ArrayList<SentWorldObject> worldObjects;
}
