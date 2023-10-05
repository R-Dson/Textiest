package Managers.Threads;

import Managers.EntityManager;
import Managers.Map.MapManager;
import Managers.SQLManager;

public class EntityThread implements Runnable {


    private final MapManager mapManager;
    private final SQLManager sqlManager;
    public EntityThread(MapManager mapManager, SQLManager sqlManager)
    {
        super();
        this.mapManager = mapManager;
        this.sqlManager = sqlManager;
    }

    @Override
    public void run() {
        EntityManager.UpdateAssign(mapManager, sqlManager);
    }
}
