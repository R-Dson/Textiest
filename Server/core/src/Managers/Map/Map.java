package Managers.Map;

import Data.FixedValues;
import DataShared.Item.Material;
import DataShared.MapType;
import Managers.Network.UserIdentity;
import com.badlogic.ashley.core.Component;

import java.util.ArrayList;
import java.util.HashSet;

public class Map implements Component {

    //public
    public MapType MapType;
    public String MapName;
    public int ID;
    public ArrayList<Integer> ConnectedMapsID;
    public MapSize MapSize;

    //private
    private HashSet<Map> ConnectedMaps = new HashSet<>();
    private ArrayList<MapLayer> MapLayers = new ArrayList<>();
    private long LayerID = 0;
    public ArrayList<Material> TreeTypes;
    public ArrayList<Material> OreTypes;

    //Info

    public void Update(float delta){
        MapLayers.removeIf(layer -> layer.ToDestroy);

        MapLayers.forEach(ml -> ml.Update(delta));
    }

    public void MoveUserToNewMap(UserIdentity userIdentity, Map newMap){
        RemoveUserIdentity(userIdentity);
        newMap.AssignUserToLayer(userIdentity);
    }

    public void RemoveUserIdentity(UserIdentity userIdentity){
        userIdentity.RemoveUserIdentityFromLayer();
    }

    public void AssignUserToLayer(UserIdentity userIdentity){
        MapLayer assignedLayer = null;

        //So we always get the correct map
        userIdentity.currentMap = this;

        if (MapType == DataShared.MapType.MULTI)
        {
            for (MapLayer layer : MapLayers) {
                if (layer != null && layer.users.size() <= FixedValues.MaxConnectionsToLayer){
                    assignedLayer = layer;
                    break;
                }
            }

            if (assignedLayer == null)
                assignedLayer = CreateNewLayer(userIdentity.UniqueID);
            userIdentity.playerData.LastMultiLocation = MapName;
        }
        else if (MapType == DataShared.MapType.SINGLE){
            assignedLayer = CreateNewLayer(userIdentity.UniqueID);
        }

        if (assignedLayer != null)
            assignedLayer.AddUserToLayer(userIdentity);
    }

    public MapLayer CreateNewLayer(String ID){
        MapLayer mapLayer = new MapLayer(this, this.MapName, LayerID, ID);
        MapLayers.add(mapLayer);
        LayerID++;
        return mapLayer;
    }

    //TODO Collision map

    public ArrayList<UserIdentity> GetAllUsers(){
        ArrayList<UserIdentity> identities = new ArrayList<>();
        for (MapLayer layer : MapLayers) {
            identities.addAll(layer.users.values());
        }
        return identities;
    }

    public enum MapSize{
        SMALL,
        MEDIUM,
        LARGE,
        GIGA
    }

    //GET SET

    public HashSet<Map> getConnectedMaps()
    {
        return ConnectedMaps;
    }

    public ArrayList<String> getConnectedMapNames()
    {
        ArrayList<String> names = new ArrayList<>();
        ConnectedMaps.forEach(m -> names.add(m.MapName));
        return names;
    }

    public void addConnectMap(Map map)
    {
        ConnectedMaps.add(map);
    }

    public ArrayList<Integer> getConnectedMapsID()
    {
        return ConnectedMapsID;
    }

    public ArrayList<MapLayer> getMapLayers() {
        return MapLayers;
    }
}
