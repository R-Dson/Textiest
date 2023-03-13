package Managers.Map;

import Components.Entities.MapEntity;
import Data.FixedValues;
import Managers.FileManager;
import Managers.Network.UserIdentity;
import com.badlogic.ashley.core.EntitySystem;
import com.vaniljstudio.server.ServerClass;

import java.util.ArrayList;

public class MapManager extends EntitySystem {
    public ArrayList<Map> MapList = new ArrayList<>();
    public ArrayList<MapEntity> mapEntities = new ArrayList<>();

    public void LoadMaps(){
        MapList = FileManager.LoadMapsFromFile();
        for (Map map : MapList) {
            MapEntity MapEntity = new MapEntity(map);
            map.getConnectedMapsID().forEach(id -> {
                getMapByID(id).addConnectMap(map);
                map.addConnectMap(getMapByID(id));
            });
            mapEntities.add(MapEntity);
            ServerClass.Engine.addEntity(MapEntity);
        }

    }

    public Map getMapByID(int ID)
    {
        return MapList.stream().filter(map -> map.ID == ID).findFirst().get();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        for (MapEntity entity : mapEntities) {
            if (entity != null)
            {
                entity.getComponent(Map.class).Update(deltaTime);
                //map.Update(deltaTime);
            }
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
        Map foundMap = GetMapByName(map);
        foundMap.AssignUserToLayer(userIdentity);
    }

    public void MoveLoginToMap(UserIdentity userIdentity, Map newMap){
        userIdentity.currentMap.MoveUserToNewMap(userIdentity, newMap);
    }

    public UserIdentity GetUserByConnectionID(int connectionID){
        for (Map map: MapList) {
            for (MapLayer layer: map.getMapLayers()) {
                return layer.users.get(connectionID);
            }
        }
        return null;
    }

    public void RemoveUserByConnectionID(int connectionID){
        UserIdentity userIdentity = GetUserByConnectionID(connectionID);
        userIdentity.RemoveUserIdentityFromLayer();
    }
}
