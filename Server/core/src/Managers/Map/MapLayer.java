package Managers.Map;

import Data.FixedValues;
import Data.Objects.ObjectActivity;
import Data.Objects.Ore;
import Data.Objects.Tree;
import Data.Objects.WorldObject;
import DataShared.Network.NetworkMessages.Client.SendMessage;
import Managers.Chat.ChatMessage;
import Managers.ChatManager;
import Managers.Network.UserIdentity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

public class MapLayer {
    public String MapName;
    public Map map;
    public long LayerID;
    public String CreatorID;
    private ChatManager chatManager;
    public HashMap<Integer, UserIdentity> users = new HashMap<>();
    public boolean ToDestroy = false;
    private float totalTime = 0;
    private float timer = 0;
    private ArrayList<WorldObject> layerObjects;
    private ArrayList<ObjectActivity> objectActivities;
    private boolean updatedObjects;

    public MapLayer(Map map, String MapName, long LayerID, String CreatorID){
        this.MapName = MapName;
        this.LayerID = LayerID;
        this.CreatorID = CreatorID;
        this.map = map;
        chatManager = new ChatManager();
        layerObjects = new ArrayList<>();
        objectActivities = new ArrayList<>();
        updatedObjects = false;

        generateLayerObjects();
    }

    public void Update(float delta){
        totalTime += delta;
        timer += delta;
        if (users.size() > 0)
            totalTime = 0;
        //If there's no activity for 15 min, destroy layer
        if (totalTime > 15 * 60 * 1000){
            ToDestroy = true;
        }
        updatedObjects = false;

        while (timer >= FixedValues.UpdateFrequency5){
            //world.step(FixedValues.UpdateFrequency30, 6, 2);
            chatManager.addMessagesToUsers(users.values());

            layerObjects.forEach(lo -> lo.Update(delta));

            boolean anyUpdate = updatedObjects;
            Iterator it = objectActivities.iterator();
            while (it.hasNext())
            {

                ObjectActivity objectActivity = (ObjectActivity) it.next();

                // update object
                if (!anyUpdate)
                    anyUpdate = objectActivity.Update(delta);

                if (!objectActivity.getWorldObject().getUsable()) {
                    objectActivity.removeObjectFromUserIdentity();
                    it.remove();
                }
            }
            updatedObjects = anyUpdate;

            // always last
            users.values().forEach(userIdentity -> userIdentity.Update(delta));
            timer -= FixedValues.UpdateFrequency5;
        }
    }

    private void generateLayerObjects()
    {
        int maxNumberOfTotalWorldObjects = -1;
        switch (map.MapSize) {
            case SMALL:
                maxNumberOfTotalWorldObjects = 5;
                break;
            case MEDIUM:
                maxNumberOfTotalWorldObjects = 25;
                break;
            case LARGE:
                maxNumberOfTotalWorldObjects = 50;
                break;
            case GIGA:
                maxNumberOfTotalWorldObjects = 100;
                break;
        }

        Random random = new Random();
        int numObj = random.ints(1, maxNumberOfTotalWorldObjects+1).findFirst().getAsInt();
        for (int i = 0; i < numObj; i++) {

            switch (WorldObject.getRandomObjectType()) {
                case NOTHING:
                    break;
                case TREE:
                    Tree tree = new Tree(this, i, 1000, 1, 5);
                    tree.initTree(map.TreeTypes);
                    layerObjects.add(tree);
                    break;
                case ROCK:
                    Ore rock = new Ore(this, i, 1000, 1, 5);
                    rock.initOre(map.OreTypes);
                    layerObjects.add(rock);
                    break;
            }

        }
    }

    public void setUpdatedObjects(boolean updatedObjects)
    {
        this.updatedObjects = updatedObjects;
    }

    public void BeginInteractWithObject(UserIdentity userIdentity, int objectID)
    {
        WorldObject object = getObjectByID(objectID);

        if (object == null)
            return;

        objectActivities.add(new ObjectActivity(object, userIdentity));
    }

    public WorldObject getObjectByID(int objectID)
    {
        return layerObjects.stream().filter(lo -> lo.getObjectID() == objectID).findFirst().get();
    }

    public boolean getUpdatedObjects()
    {
        return updatedObjects;
    }

    public void addChatMessage(SendMessage message, UserIdentity userIdentity)
    {
        ChatMessage cm = new ChatMessage(message.Message, userIdentity, message.chatEnum);
        chatManager.appendChatMessage(cm);

    }

    public void AddUserToLayer(UserIdentity userIdentity){
        userIdentity.currentLayer = this;
        users.put(userIdentity.connectionID, userIdentity);
    }
/*
    public void MoveUserToLayer(UserIdentity userIdentity, MapLayer newLayer){
        if (newLayer == null) return;
        users.remove(userIdentity);
        newLayer.AddUserToLayer(userIdentity);
    }*/

    public void RemoveUserFromLayerID(Integer connectID){
        users.remove(connectID);
    }

    public void RemoveUserFromLayer(UserIdentity userIdentity){
        userIdentity.currentMap = null;
        RemoveUserFromLayerID(userIdentity.connectionID);
    }

    public ArrayList<WorldObject> getLayerObjects() {
        return layerObjects;
    }

    /*
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
        */
    @Override
    public boolean equals(Object o) {
        return (o instanceof MapLayer) && ((MapLayer) o).LayerID == (LayerID);
    }

}
