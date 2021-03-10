package Managers.Map;

import Components.Entities.PlayerEntity;
import Components.PlayerComponents.B2dBodyComponent;
import Data.FixedValues;
import Managers.Network.UserIdentity;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.vaniljstudio.server.ServerClass;

import java.util.ArrayList;
import java.util.Iterator;

public class Map implements Component {

    //public
    public MapType MapType;
    public String MapName;
    public World world;
    public int tileWidth, tileHeight, mapWidthInTiles, mapHeightInTiles, mapWidthInPixels, mapHeightInPixels;

    //private
    private TiledMap _TiledMap;
    private ArrayList<MapLayer> MapLayers = new ArrayList<>();
    private long LayerID = 0;

    //Info
    private Vector2 SpawnPoint;

    public void LoadMap(){
        AssetManager assetManager = ServerClass.AssetManager;
        world = new World(new Vector2(0, 0), true);
        assetManager.setLoader(TiledMap.class, new TmxMapLoader());
        String path = "Maps/" + MapName + ".tmx";
        assetManager.load(path, TiledMap.class);
        assetManager.finishLoading();
        _TiledMap = assetManager.get(path, TiledMap.class);
        MapProperties properties = _TiledMap.getProperties();

        tileHeight        = properties.get("tileheight", Integer.class);
        tileWidth        = properties.get("tilewidth", Integer.class);
        mapWidthInTiles   = properties.get("width", Integer.class);
        mapHeightInTiles  = properties.get("height", Integer.class);
        mapWidthInPixels  = mapWidthInTiles  * tileWidth;
        mapHeightInPixels = mapHeightInTiles * tileHeight;
        SetupMapCollision();
        setSpawnPoint();
    }

    private float timer = 0;
    public void Update(float delta){
        //TODO CHANGE
        timer += delta;

        while (timer >= FixedValues.UpdateFrequency30){
            world.step(FixedValues.UpdateFrequency30, 6, 2);

            for (MapLayer layer : MapLayers) {
                for (UserIdentity identity: layer.users) {

                }
            }

            timer -= FixedValues.UpdateFrequency30;
        }

        Iterator iterator = MapLayers.iterator();
        while (iterator.hasNext()){
            MapLayer layer = (MapLayer) iterator.next();
            if (layer.ToDestroy)
                iterator.remove();
        }
        for (MapLayer layer:MapLayers) {
            layer.Update(delta);
        }
    }

    public void MoveUserToNewMap(UserIdentity userIdentity, Map newMap){
        /*Body body = userIdentity.entity.getComponent(B2dBodyComponent.class).body;
        if (body != null)
            world.destroyBody(body);
        userIdentity.currentLayer.RemoveUserFromLayer(userIdentity);*/
        DestroyBodyOfUserIdentity(userIdentity);
        newMap.AssignUserToLayer(userIdentity);
    }

    public void DestroyBodyOfUserIdentity(UserIdentity userIdentity){
        Body body = userIdentity.entity.getComponent(B2dBodyComponent.class).body;
        if (body != null)
            world.destroyBody(body);
        userIdentity.currentLayer.RemoveUserFromLayer(userIdentity);
    }

    public void AssignUserToLayer(UserIdentity userIdentity){
        MapLayer assignedLayer = null;

        //So we always get the correct map
        userIdentity.currentMap = this;

        if (MapType == MapType.MULTI)
        {
            for (MapLayer layer : MapLayers) {
                if (layer != null && layer.users.size() <= FixedValues.MaxConnectionsToLayer){
                    assignedLayer = layer;
                    break;
                }
            }

            if (assignedLayer == null)
                assignedLayer = CreateNewLayer("-1");
            //TODO Set Lastmultilocation
            userIdentity.playerData.LastMultiLocation = MapName;
        }
        else if (MapType == MapType.SINGLE){
            assignedLayer = CreateNewLayer(userIdentity.UniqueID);
        }

        assert assignedLayer != null;
        assignedLayer.users.add(userIdentity);
        if (userIdentity.entity == null){
            userIdentity.entity = new PlayerEntity();

            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            if (userIdentity.playerData.PlayerPosition != null)
                bodyDef.position.set(userIdentity.playerData.PlayerPosition);
            else
                bodyDef.position.set(SpawnPoint);
            Body body = world.createBody(bodyDef);
            body.setFixedRotation(true);
            FixtureDef fd = new FixtureDef();

            PolygonShape groundBox = new PolygonShape();
            groundBox.setAsBox(tileWidth / 2f * FixedValues.TileScale, tileHeight / 2f * FixedValues.TileScale);
            fd.shape = groundBox;

            body.createFixture(fd);
            body.setLinearDamping(50f);

            userIdentity.entity.add(new B2dBodyComponent(body));
            ServerClass.Engine.addEntity(userIdentity.entity);

            groundBox.dispose();
        }

        userIdentity.currentLayer = assignedLayer;

    }

    public MapLayer CreateNewLayer(String ID){
        MapLayer mapLayer;
        if (ID.equals("-1"))
            mapLayer = new MapLayer(this.MapName, LayerID);
        else
            mapLayer = new MapLayer(this.MapName, LayerID, ID);
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

    public void SetupMapCollision(){
        com.badlogic.gdx.maps.MapLayer layer = _TiledMap.getLayers().get("Collision");
        TiledMapTileLayer tl = (TiledMapTileLayer)layer;

        for (int y = 0; y < mapHeightInTiles; y++) {
            for (int x = 0; x < mapWidthInTiles; x++) {
                TiledMapTileLayer.Cell cell = tl.getCell(x, y);
                if (cell != null){
                    BodyDef groundbodyDef = new BodyDef();
                    groundbodyDef.type = BodyDef.BodyType.StaticBody;
                    groundbodyDef.position.set((tileWidth * x + tileWidth/2)* FixedValues.TileScale, (tileHeight * y + tileHeight/2)* FixedValues.TileScale);
                    Body groundBody = world.createBody(groundbodyDef);
                    groundBody.setFixedRotation(true);
                    PolygonShape groundBox = new PolygonShape();
                    groundBox.setAsBox(tileWidth/2 * FixedValues.TileScale, tileHeight/2 * FixedValues.TileScale);
                    FixtureDef fd = new FixtureDef();
                    fd.shape = groundBox;
                    groundBody.createFixture(fd);

                    groundBox.dispose();
                }
            }
        }

        com.badlogic.gdx.maps.MapLayer collayer = _TiledMap.getLayers().get("CustomCollision");
        TiledMapTileLayer tlcustom = (TiledMapTileLayer)collayer;
        for (int y = 0; y < mapHeightInTiles; y++) {
            for (int x = 0; x < mapWidthInTiles; x++) {
                TiledMapTileLayer.Cell cell = tlcustom.getCell(x,y);
                if (cell != null){
                    MapObjects objects = cell.getTile().getObjects();
                    for (MapObject object: objects) {
                        BodyDef bodyDef = new BodyDef();
                        bodyDef.type = BodyDef.BodyType.StaticBody;
                        bodyDef.position.set((tileWidth * x + tileWidth/2)* FixedValues.TileScale, (tileHeight * y + tileHeight/2)* FixedValues.TileScale);
                        Body Body = world.createBody(bodyDef);
                        Body.setFixedRotation(true);
                        Shape shape;

                        if (object instanceof RectangleMapObject)
                        {
                            shape = new PolygonShape();
                            ((PolygonShape) shape).setAsBox(tileWidth/2 * FixedValues.TileScale, tileHeight/2* FixedValues.TileScale);
                        }
                        else if(object instanceof EllipseMapObject){
                            EllipseMapObject eo = (EllipseMapObject)object;
                            shape = new CircleShape();
                            float average = (eo.getEllipse().height/2 + eo.getEllipse().width/2)/2;
                            ((CircleShape)shape).setRadius(average * FixedValues.TileScale);
                        }
                        else
                        {
                            shape = new PolygonShape();
                        }
                        FixtureDef fd = new FixtureDef();
                        fd.shape = shape;

                        Body.createFixture(fd);

                        shape.dispose();
                    }
                }
            }
        }


    }
    //TODO Collision map
    //TODO Set spawn point

    public ArrayList<UserIdentity> GetAllUsers(){
        ArrayList<UserIdentity> identities = new ArrayList<>();
        for (MapLayer layer:MapLayers) {
            identities.addAll(layer.users);
        }
        return identities;
    }

    //GET SET

    public TiledMap get_TiledMap() {
        return _TiledMap;
    }

    public ArrayList<MapLayer> getMapLayers() {
        return MapLayers;
    }
}
