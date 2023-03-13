package Managers;

import Managers.Network.UserIdentity;
import com.badlogic.ashley.core.Entity;

import java.util.HashMap;

public class EntityManager {

    //ConnectionID and UserIdentity
    public static HashMap<Integer, UserIdentity> EntityList = new HashMap<>();

    /*public void Update(float delta){
        for (UEntity entity: EntityList) {
            entity.Update(delta);
        }
    }*/

    public static void RemoveUserIdentity(UserIdentity identity){
        //identity.currentLayer.RemoveUserFromLayer(identity);
        identity.RemoveUserIdentityFromLayer();
        EntityList.remove(identity);
    }

    public static UserIdentity getEntityByUniqueID(final String uniqueID){
        for (UserIdentity e: EntityList.values())
            if (e != null)
                if (e.UniqueID.equals(uniqueID))
                    return e;
        return null;
    }

    public static UserIdentity getUserIdentityByConnectID(final int connectID){
        return EntityList.get(connectID);
        /*for (Entity e: EntityList) {
            if (e instanceof UserIdentity) {
                if (((UserIdentity)e).connectionID == connectID)
                    return (UserIdentity)e;
            }

        }
        return null;*/
    }
}
