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
    ImmutableArray<Entity> entities;

    public void LoadMaps(){
        MapList = FileManager.LoadMapsFromFile();
        for (Map map :MapList) {
            map.LoadMap();
            MapEntity MapEntity = new MapEntity(map);
            //map.mapEntity = MapEntity;
            //TODO Only render collision tiles
            //ServerClass.EntityManager.EntityList.add(MapEntity);
            ServerClass.Engine.addEntity(MapEntity);
        }
        entities = ServerClass.Engine.getEntitiesFor(Family.all(Map.class).get());
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        for (Entity entity : entities) {
            if (entity instanceof MapEntity)
            {
                MapEntity me = (MapEntity) entity;
                Map map = me.getComponent(Map.class);
                map.Update(deltaTime);
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
