package Managers.Threads;

import Managers.Map.MapManager;
import Managers.SQLManager;

public class ThreadManager {

    private final EntityThread entityThread;
    private final MapThread mapThread;
    private final NetworkThread networkThread;

    public ThreadManager(MapManager mapManager, SQLManager sqlManager)
    {
        entityThread = new EntityThread(mapManager, sqlManager);
        mapThread = new MapThread();
        networkThread = new NetworkThread();
    }

    public void InitAllThreads()
    {
        InitEntityThread();
        InitMapThread();
        InitNetworkThread();
    }

    public void InitEntityThread()
    {
        entityThread.run();
    }

    private void InitMapThread()
    {
        mapThread.run();
    }

    private void InitNetworkThread()
    {
        networkThread.run();
    }

}
