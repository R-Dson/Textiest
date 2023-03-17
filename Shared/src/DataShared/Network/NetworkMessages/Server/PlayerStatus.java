package DataShared.Network.NetworkMessages.Server;

public class PlayerStatus {

    public String objectName;
    public int objectID;
    public InteractObjectType interactObjectType;

    public enum InteractObjectType
    {
        NONE,
        TREE,
        ORE
    }

}
