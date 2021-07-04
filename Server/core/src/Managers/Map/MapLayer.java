package Managers.Map;

import Components.Entities.MapEntity;
import Components.Entities.PlayerEntity;
import Components.PlayerComponents.B2dBodyComponent;
import Data.FixedValues;
import Managers.Network.UserIdentity;
import Managers.PlayerManager;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.vaniljstudio.server.ServerClass;

import java.util.HashMap;

public class MapLayer {
    public String MapName;
    public Map map;
    public long LayerID;
    public String CreatorID;

    public World world;

    public HashMap<Integer, UserIdentity> users = new HashMap<>();
    public boolean ToDestroy = false;


    private TiledMap tiledMap;
    private float totalTime = 0;
    private float timer = 0;

    public MapLayer(Map map, String MapName, long LayerID, String CreatorID){
        this.MapName = MapName;
        this.LayerID = LayerID;
        this.CreatorID = CreatorID;
        this.map = map;
        tiledMap = map.getTiledMap();
        world = new World(new Vector2(0, 0), true);
    }

    /*public MapLayer(String MapName, long LayerID){
        this.MapName = MapName;
        this.LayerID = LayerID;
        world = new World(new Vector2(0, 0), true);
    }*/

    public void Update(float delta){
        totalTime += delta;
        timer += delta;
        if (users.size() > 0)
            totalTime = 0;
        //If there's no activity for 15 min, destroy layer
        if (totalTime > 15 * 60 * 1000){
            ToDestroy = true;
        }

        while (timer >= FixedValues.UpdateFrequency30){
            world.step(FixedValues.UpdateFrequency30, 6, 2);

            for (UserIdentity identity: users.values()) {
                identity.Update(delta);
            }

            timer -= FixedValues.UpdateFrequency30;
        }
    }

    public void render (float delta, MapEntity mapEntity){
        if (map.MapName.equals("map") && mapEntity.debugRenderer != null && world != null && mapEntity.camera != null){
            mapEntity.renderer.setView(mapEntity.camera);
            mapEntity.renderer.render(new int[]{0,1,2,3,4});
            mapEntity.debugRenderer.render(world, mapEntity.camera.combined);
        }
    }

    public void AddUserToLayer(UserIdentity userIdentity){
        userIdentity.currentLayer = this;
        users.put(userIdentity.connectionID, userIdentity);
        CreateBody(userIdentity);
        SetupMapCollision();
    }

    public void CreateBody(UserIdentity userIdentity){
        if (userIdentity.entity == null){
            userIdentity.entity = new PlayerEntity();

            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            Vector2 position = PlayerManager.GetPosition(userIdentity.playerData);
            if (position != null)
                bodyDef.position.set(position);
            else
                bodyDef.position.set(this.map.getSpawnPoint());
            Body body = world.createBody(bodyDef);
            body.setFixedRotation(true);
            FixtureDef fd = new FixtureDef();

            PolygonShape groundBox = new PolygonShape();
            groundBox.setAsBox(map.tileWidth / 2f * FixedValues.TileScale, map.tileHeight / 2f * FixedValues.TileScale);
            fd.shape = groundBox;

            body.createFixture(fd);
            body.setLinearDamping(50f);

            userIdentity.entity.add(new B2dBodyComponent(body));
            ServerClass.Engine.addEntity(userIdentity.entity);

            groundBox.dispose();
        }
    }

    public void MoveUserToLayer(UserIdentity userIdentity, MapLayer newLayer){
        if (newLayer == null) return;
        users.remove(userIdentity);
        newLayer.AddUserToLayer(userIdentity);
    }

    public void RemoveUserFromLayerID(Integer connectID){
        users.remove(connectID);
    }

    public void RemoveUserFromLayer(UserIdentity userIdentity){
        Body body = userIdentity.entity.getComponent(B2dBodyComponent.class).body;
        if (body != null)
            world.destroyBody(body);
        userIdentity.entity = null;
        userIdentity.currentMap = null;
        RemoveUserFromLayerID(userIdentity.connectionID);
    }

    public void SetupMapCollision(){
        com.badlogic.gdx.maps.MapLayer layer = tiledMap.getLayers().get("Collision");
        TiledMapTileLayer tl = (TiledMapTileLayer)layer;

        for (int y = 0; y < map.mapHeightInTiles; y++) {
            for (int x = 0; x < map.mapWidthInTiles; x++) {
                TiledMapTileLayer.Cell cell = tl.getCell(x, y);
                if (cell != null){
                    BodyDef groundbodyDef = new BodyDef();
                    groundbodyDef.type = BodyDef.BodyType.StaticBody;
                    groundbodyDef.position.set((map.tileWidth * x + map.tileWidth/2f)* FixedValues.TileScale, (map.tileHeight * y + map.tileHeight/2f)* FixedValues.TileScale);
                    Body groundBody = world.createBody(groundbodyDef);
                    groundBody.setFixedRotation(true);
                    PolygonShape groundBox = new PolygonShape();
                    groundBox.setAsBox(map.tileWidth/2f * FixedValues.TileScale, map.tileHeight/2f * FixedValues.TileScale);
                    FixtureDef fd = new FixtureDef();
                    fd.shape = groundBox;
                    groundBody.createFixture(fd);

                    groundBox.dispose();
                }
            }
        }

        com.badlogic.gdx.maps.MapLayer collayer = tiledMap.getLayers().get("CustomCollision");
        TiledMapTileLayer tlcustom = (TiledMapTileLayer)collayer;
        for (int y = 0; y < map.mapHeightInTiles; y++) {
            for (int x = 0; x < map.mapWidthInTiles; x++) {
                TiledMapTileLayer.Cell cell = tlcustom.getCell(x,y);
                if (cell != null){
                    MapObjects objects = cell.getTile().getObjects();
                    for (MapObject object: objects) {
                        BodyDef bodyDef = new BodyDef();
                        bodyDef.type = BodyDef.BodyType.StaticBody;
                        bodyDef.position.set((map.tileWidth * x + map.tileWidth/2f)* FixedValues.TileScale, (map.tileHeight * y + map.tileHeight/2f)* FixedValues.TileScale);
                        Body Body = world.createBody(bodyDef);
                        Body.setFixedRotation(true);
                        Shape shape;

                        if (object instanceof RectangleMapObject)
                        {
                            shape = new PolygonShape();
                            ((PolygonShape) shape).setAsBox(map.tileWidth/2f * FixedValues.TileScale, map.tileHeight/2f * FixedValues.TileScale);
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

    @Override
    public boolean equals(Object o) {
        return (o instanceof MapLayer) && ((MapLayer) o).LayerID == (LayerID);
    }

}
