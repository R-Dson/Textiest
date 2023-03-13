package Data.Objects;

import Managers.Network.UserIdentity;

import java.util.Random;

public class ObjectActivity {

    private WorldObject worldObject;
    private boolean isBusy;
    private UserIdentity userIdentity;
    private Random random = new Random();
    private float timePassed = 0;
    public ObjectActivity(WorldObject worldObject, UserIdentity userIdentity){
        this.worldObject = worldObject;
        this.userIdentity = userIdentity;
        userIdentity.setObjectActivity(this);
        isBusy = true;
    }

    public boolean Update(float delta)
    {
        if (!worldObject.getUsable())
            return false;

        timePassed += delta;
        if (timePassed > 500)
        {
            timePassed -= 0.5;

            if (random.nextFloat() > 0.5)
            {
                if (worldObject instanceof Tree)
                {
                    Tree tree = (Tree) worldObject;
                    if (tree.getUsable())
                        return tree.ChopLog(userIdentity);

                }
                else if (worldObject instanceof Ore)
                {
                    Ore ore = (Ore) worldObject;
                    if (ore.getUsable())
                        return ore.mineOre(userIdentity);
                }
            }

        }
        return false;
    }

    public void removeObjectFromUserIdentity()
    {
        userIdentity.setObjectActivity(null);
    }

    public WorldObject getWorldObject() {
        return worldObject;
    }
}
