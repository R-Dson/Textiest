package Managers.Map;

import Managers.Network.UserIdentity;

import java.util.ArrayList;

public class MapLayer {
    public String MapName;
    public long LayerID;
    public ArrayList<UserIdentity> users = new ArrayList<>();
    public String CreatorID;

    private float totalTime = 0;
    public boolean ToDestroy = false;

    public MapLayer(String MapName, long LayerID, String CreatorID){
        this.MapName = MapName;
        this.LayerID = LayerID;
        this.CreatorID = CreatorID;
    }

    public MapLayer(String MapName, long LayerID){
        this.MapName = MapName;
        this.LayerID = LayerID;
    }

    public void Update(float delta){
        totalTime += delta;
        if (users.size() > 0)
            totalTime = 0;
        //If there's no activity for 15 min, destroy layer
        if (totalTime> 15 * 60 * 1000)
            ToDestroy = true;
    }

    public void AddUserToLayer(UserIdentity userIdentity){
        users.add(userIdentity);
    }

    public void MoveUserToLayer(UserIdentity userIdentity, MapLayer newLayer){
        if (newLayer == null) return;
        users.remove(userIdentity);
        newLayer.AddUserToLayer(userIdentity);
    }

    public void RemoveUserFromLayerID(String ID){
        users.removeIf(userIdentity -> userIdentity.UniqueID.equals(ID));
    }

    public void RemoveUserFromLayer(UserIdentity userIdentity){
        userIdentity.entity = null;
        RemoveUserFromLayerID(userIdentity.UniqueID);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof MapLayer && ((MapLayer) o).LayerID == (LayerID)){
            return true;
        }
        return false;
    }
}
