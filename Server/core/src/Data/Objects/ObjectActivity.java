package Data.Objects;

import Managers.Network.UserIdentity;

import java.util.Random;

public class ObjectActivity {
    private final static float OBJECT_UPDATE_TIME = 0.5f;

    private final WorldObject worldObject;
    private final UserIdentity userIdentity;
    private final Random random = new Random();
    private float updateTimer = 0;
    public ObjectActivity(WorldObject worldObject, UserIdentity userIdentity){
        this.worldObject = worldObject;
        this.userIdentity = userIdentity;
        userIdentity.setObjectActivity(this);
    }

    public boolean Update(float delta)
    {
        if (!worldObject.getUsable())
            return false;

        updateTimer += delta;
        if (updateTimer > OBJECT_UPDATE_TIME)
        {
            updateTimer -= OBJECT_UPDATE_TIME;

            if (random.nextFloat() > 0.9)
                worldObject.activity(userIdentity, this);
        }

        return false;
    }

    public void removeObjectActivityFromUserIdentity()
    {
        userIdentity.setObjectActivity(null);
    }

    public WorldObject getWorldObject() {
        return worldObject;
    }
}
