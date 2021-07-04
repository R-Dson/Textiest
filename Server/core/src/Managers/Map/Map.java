package Managers.Map;

import Data.FixedValues;
import Managers.Network.UserIdentity;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.vaniljstudio.server.ServerClass;

import java.util.ArrayList;

public class Map implements Component {

    //public
    public MapType MapType;
    public String MapName;
    public int tileWidth, tileHeight, mapWidthInTiles, mapHeightInTiles, mapWidthInPixels, mapHeightInPixels;

    //private
    private TiledMap _TiledMap;
    private ArrayList<MapLayer> MapLayers = new ArrayList<>();
    private long LayerID = 0;

    //Info
    private Vector2 SpawnPoint;

    private final static String tileHeightS = "tileheight";
    private final static String tileWidthS = "tilewidth";
    private final static String WidthS = "width";
    private final static String HeightS = "height";

    public void LoadMap(){
        AssetManager assetManager = ServerClass.AssetManager;
        //world = new World(new Vector2(0, 0), true);
        assetManager.setLoader(TiledMap.class, new TmxMapLoader());
        String path = "Maps/" + MapName + ".tmx";
        assetManager.load(path, TiledMap.class);
        assetManager.finishLoading();
        _TiledMap = assetManager.get(path, TiledMap.class);
        MapProperties properties = _TiledMap.getProperties();

        tileHeight        = properties.get(tileHeightS, Integer.class);
        tileWidth        = properties.get(tileWidthS, Integer.class);
        mapWidthInTiles   = properties.get(WidthS, Integer.class);
        mapHeightInTiles  = properties.get(HeightS, Integer.class);
        mapWidthInPixels  = mapWidthInTiles  * tileWidth;
        mapHeightInPixels = mapHeightInTiles * tileHeight;

        setSpawnPoint();
    }

    private float timer = 0;
    public void Update(float delta){
        //TODO CHANGE
        timer += delta;

        while (timer >= FixedValues.UpdateFrequency30){
            MapLayers.removeIf(layer -> layer.ToDestroy);

            timer -= FixedValues.UpdateFrequency30;
        }

        for (MapLayer layer:MapLayers) {
            layer.Update(delta);
        }
    }

    public void MoveUserToNewMap(UserIdentity userIdentity, Map newMap){
        RemoveUserIdentity(userIdentity);
        newMap.AssignUserToLayer(userIdentity);
    }

    public void RemoveUserIdentity(UserIdentity userIdentity){
        userIdentity.RemoveUserIdentity();
    }

    public void AssignUserToLayer(UserIdentity userIdentity){
        MapLayer assignedLayer = null;

        //So we always get the correct map
        userIdentity.currentMap = this;

        if (MapType == Managers.Map.MapType.MULTI)
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
        else if (MapType == Managers.Map.MapType.SINGLE){
            assignedLayer = CreateNewLayer(userIdentity.UniqueID);
        }

        assignedLayer.AddUserToLayer(userIdentity);
    }

    public MapLayer CreateNewLayer(String ID){
        MapLayer mapLayer = new MapLayer(this, this.MapName, LayerID, ID);
        MapLayers.add(mapLayer);
        LayerID++;
        return mapLayer;
    }

    public void setSpawnPoint(){
        com.badlogic.gdx.maps.MapLayer layer = _TiledMap.getLayers().get("Objects");
        MapObjects objects = layer.getObjects();
        RectangleMapObject obj = (RectangleMapObject)objects.get("Spawn");
        SpawnPoint = new Vector2(obj.getRectangle().getX(),obj.getRectangle().getY());
    }

    public Vector2 getSpawnPoint() {
        return SpawnPoint;
    }

    //TODO Collision map

    public ArrayList<UserIdentity> GetAllUsers(){
        ArrayList<UserIdentity> identities = new ArrayList<>();
        for (MapLayer layer:MapLayers) {
            identities.addAll(layer.users.values());
        }
        return identities;
    }

    //GET SET

    public TiledMap getTiledMap() {
        return _TiledMap;
    }

    public ArrayList<MapLayer> getMapLayers() {
        return MapLayers;
    }
}
