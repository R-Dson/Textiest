package Managers;

import Data.UEntity;
import Managers.Network.UserIdentity;

import java.util.ArrayList;
import java.util.List;

public class EntityManager {

    public List<UEntity> EntityList = new ArrayList<>();

    public void Update(float delta){
        for (UEntity entity: EntityList) {
            entity.Update(delta);
        }
    }

    public UEntity getEntityByUniqueID(final String uniqueID){
        for (UEntity e: EntityList) {
            if (e instanceof UserIdentity) {
                if (((UserIdentity)e).UniqueID.equals(uniqueID))
                    return e;
            }

        }
        return null;
    }

    public UserIdentity getEntityByConnectID(final int connectID){
        for (UEntity e: EntityList) {
            if (e instanceof UserIdentity) {
                if (((UserIdentity)e).connectionID == connectID)
                    return (UserIdentity)e;
            }

        }
        return null;
    }
}
