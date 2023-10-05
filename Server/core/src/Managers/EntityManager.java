package Managers;

import DataShared.Player.PlayerData;
import Managers.Map.MapManager;
import Managers.Network.UserIdentity;
import com.badlogic.gdx.utils.Json;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class EntityManager {

    private static final HashMap<Integer, UserIdentity> EntityList = new HashMap<>(); //ConnectionID and UserIdentity
    private static final ArrayList<UserIdentity> ToBeAssigned = new ArrayList<>();
    private static final Semaphore addedUI = new Semaphore(0);
    private static final Json json = new Json();


     public synchronized static void RemoveUserIdentity(UserIdentity identity){
        identity.RemoveUserIdentityFromLayer();
        EntityList.remove(identity.connectionID);
    }

    public synchronized static UserIdentity getEntityByUniqueID(final String uniqueID){
        for (UserIdentity e: EntityList.values())
            if (e != null)
                if (e.playerData.UniqueUserID.equals(uniqueID))
                    return e;
        return null;
    }

    public synchronized static UserIdentity getUserIdentityByConnectID(final int connectID){
        return EntityList.get(connectID);
    }

    public synchronized static void addUserIdentityList(UserIdentity userIdentity) {
        EntityList.put(userIdentity.connectionID, userIdentity);
    }

    public static synchronized void UpdateAssign(MapManager mapManager, SQLManager sqlManager){

        while (true)
        {
            try {
                addedUI.acquire();
            }
            catch (Exception e)
            {
                Logger.log(Logger.ErrorLevel.CRITICAL, "Failed to Acquire Entity Thread.");
                continue;
            }

            if (ToBeAssigned.isEmpty())
                continue;

            Iterator<UserIdentity> iterator = ToBeAssigned.iterator();

            while (iterator.hasNext())
            {
                UserIdentity next = iterator.next();
                String data = sqlManager.requestData(next.UserName);
                next.playerData = json.fromJson(PlayerData.class, data);

                checkForNulls(next.playerData);

                Managers.EntityManager.addUserIdentityList(next);
                mapManager.AssignLogin(next);
                iterator.remove();
            }
        }
    }

    public static synchronized void addIdentityToBeAssigned(UserIdentity userIdentity) {
        ToBeAssigned.add(userIdentity);
        try {
            addedUI.release();
        }
        catch (Exception e)
        {
            Logger.log(Logger.ErrorLevel.CRITICAL, "Failed to Release Entity Thread.");
        }
    }

    private static void checkForNulls(PlayerData playerData)
    {
        if (playerData.UniqueUserID == null)
            playerData.UniqueUserID = UUID.randomUUID().toString();

        if (playerData.friendsUniqueIDs == null)
            playerData.friendsUniqueIDs = new HashSet<>();
    }
}
