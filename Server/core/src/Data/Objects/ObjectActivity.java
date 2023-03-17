package Data.Objects;

import Managers.Network.UserIdentity;

import java.util.Random;

public class ObjectActivity {

    private final WorldObject worldObject;
    private final UserIdentity userIdentity;
    private final Random random = new Random();
    private float timePassed = 0;
    public ObjectActivity(WorldObject worldObject, UserIdentity userIdentity){
        this.worldObject = worldObject;
        this.userIdentity = userIdentity;
        userIdentity.setObjectActivity(this);
    }

    public boolean Update(float delta)
    {
        if (!worldObject.getUsable())
            return false;

        timePassed += delta;
        if (timePassed > 500)
        {
            timePassed -= 500;

            if (random.nextFloat() > 0.5)
            {
                worldObject.activity(userIdentity, this);

            }

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
