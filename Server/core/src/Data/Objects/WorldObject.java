package Data.Objects;

import Managers.Map.MapLayer;
import Managers.Network.UserIdentity;

import java.util.Random;

public class WorldObject {
    private String objectName;
    private final int objectID;
    private final int respawnTimerMillis;
    private boolean isUsable;
    private float timer = 0;
    private final MapLayer mapLayer;

    public WorldObject(MapLayer mapLayer, int ID, int respawnTimerMillis)
    {
        this.mapLayer = mapLayer;
        this.objectID = ID;
        this.respawnTimerMillis = respawnTimerMillis;
        this.isUsable = true;
    }

    public void Update(float delta)
    {
        if (isUsable)
        {
            if (timer != 0)
                timer = 0;
            return;
        }

        timer += delta;

        if (getTimer() >= this.getRespawnTimerMillis()) {
            mapLayer.sendObjectLayerUpdate();
            reset();
        }
    }

    public void reset() { }

    public void activity(UserIdentity userIdentity, ObjectActivity objectActivity) { }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public boolean getUsable()
    {
        return isUsable;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setUsable(boolean usable) {
        isUsable = usable;
    }

    public int getObjectID() {
        return objectID;
    }

    public int getRespawnTimerMillis() {
        return respawnTimerMillis;
    }

    public float getTimer() {
        return timer;
    }

    public static WorldObjectEnum getRandomObjectType()
    {
        Random random = new Random();
        int typeInt = random.ints(WorldObjectEnum.minValue, WorldObjectEnum.maxValue+1).findFirst().getAsInt();
        return WorldObject.WorldObjectEnum.GetValue(typeInt);
    }

    public MapLayer getMapLayer() {
        return mapLayer;
    }

    public enum WorldObjectEnum {
        NOTHING(0),
        TREE(1),
        ROCK(2);

        private final int value;
        public static final int minValue = 0;
        public static final int maxValue = 2;

        private WorldObjectEnum(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
        
        public boolean Compare(int i)
        {
            return value == i;
        }

        public static WorldObjectEnum GetValue(int _id)
        {
            for (WorldObjectEnum value : WorldObjectEnum.values()) {
                if (value.Compare(_id))
                    return value;
            }

            return WorldObjectEnum.NOTHING;
        }
    }
}
