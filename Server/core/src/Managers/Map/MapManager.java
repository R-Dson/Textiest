package Managers.Map;

import Components.Entities.MapEntity;
import Data.FixedValues;
import Managers.EntityManager;
import Managers.FileManager;
import Managers.Network.UserIdentity;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.assets.AssetManager;
import com.vaniljstudio.server.ServerClass;

import java.util.ArrayList;

public class MapManager extends EntitySystem {
    public ArrayList<Map> MapList = new ArrayList<>();
    public ArrayList<MapEntity> mapEntities = new ArrayList<>();

    public void LoadMaps(){
        MapList = FileManager.LoadMapsFromFile();
        for (Map map :MapList) {
            map.LoadMap();
            MapEntity MapEntity = new MapEntity(map);
            mapEntities.add(MapEntity);
            //TODO: Only render collision tiles
            ServerClass.Engine.addEntity(MapEntity);
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        for (MapEntity entity : mapEntities) {
            if (entity != null)
            {
                Map map = entity.getComponent(Map.class);
                map.Update(deltaTime);
            }
        }
    }

    public Map GetMapByName(String name){
        return MapList.stream().filter(map -> map.MapName.equals(name)).findFirst().get();
    }

    public void AssignLogin(UserIdentity userIdentity){
        String map;
        if (userIdentity.playerData.LastMultiLocation == null) map = FixedValues.DefaultMap;
        else map = userIdentity.playerData.LastMultiLocation;
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
        userIdentity.RemoveUserIdentity();
    }
}
