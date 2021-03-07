package Managers.Map;

import Components.Entities.MapEntity;
import Data.FixedValues;
import Managers.EntityManager;
import Managers.FileManager;
import Managers.Network.UserIdentity;
import com.badlogic.gdx.assets.AssetManager;
import com.vaniljstudio.server.ServerClass;

import java.util.ArrayList;

public class MapManager {
    public ArrayList<Map> MapList = new ArrayList<>();

    public void LoadMaps(){
        MapList = FileManager.LoadMapsFromFile();
        for (Map map :MapList) {
            map.LoadMap();
            MapEntity MapEntity = new MapEntity(map);
            map.mapEntity = MapEntity;
            //TODO Only render collision tiles
            ServerClass.EntityManager.EntityList.add(MapEntity);
        }
    }

    public Map GetMapByName(String name){
        return MapList.stream().filter(map -> map.MapName.equals(name)).findFirst().get();
    }

    public void AssignLogin(UserIdentity userIdentity){
        String map;
        if (userIdentity.playerData.LastMultiLocation == null)
            map = FixedValues.DefaultMap;
        else
            map = userIdentity.playerData.LastMultiLocation;
        Map foundMap;
        foundMap = GetMapByName(map);
        foundMap.AssignUserToLayer(userIdentity);
    }

    public void MoveLoginToMap(UserIdentity userIdentity, Map newMap){
        userIdentity.currentMap.MoveUserToNewMap(userIdentity, newMap);
    }

    public UserIdentity GetUserByConnectionID(int connectionID){
        for (Map map: MapList) {
            for (MapLayer layer: map.getMapLayers()) {
                for (UserIdentity userIdentity : layer.users) {
                    if (connectionID == userIdentity.connectionID)
                        return userIdentity;
                }
            }
        }
        return null;
    }

    public UserIdentity GetUserByUniqueID(String uniqueID){
        for (Map map: MapList) {
            for (MapLayer layer: map.getMapLayers()) {
                for (UserIdentity userIdentity : layer.users) {
                    if (uniqueID.equals(userIdentity.UniqueID))
                        return userIdentity;
                }
            }
        }
        return null;
    }

    public void RemoveUserByConnectionID(int connectionID){
        UserIdentity userIdentity = GetUserByConnectionID(connectionID);
        userIdentity.RemoveUserIdentity();
    }

}
