package Managers;

import Managers.Network.UserIdentity;
import com.badlogic.ashley.core.Entity;

import java.util.ArrayList;
import java.util.List;

public class EntityManager {

    public List<Entity> EntityList = new ArrayList<>();

    /*public void Update(float delta){
        for (UEntity entity: EntityList) {
            entity.Update(delta);
        }
    }*/

    public void RemoveUserIdentity(UserIdentity identity){
        identity.currentLayer.RemoveUserFromLayer(identity);
        EntityList.remove(identity);
    }

    public Entity getEntityByUniqueID(final String uniqueID){
        for (Entity e: EntityList) {
            if (e instanceof UserIdentity) {
                if (((UserIdentity)e).UniqueID.equals(uniqueID))
                    return e;
            }

        }
        return null;
    }

    public UserIdentity getUserIdentityByConnectID(final int connectID){
        for (Entity e: EntityList) {
            if (e instanceof UserIdentity) {
                if (((UserIdentity)e).connectionID == connectID)
                    return (UserIdentity)e;
            }

        }
        return null;
    }
}
