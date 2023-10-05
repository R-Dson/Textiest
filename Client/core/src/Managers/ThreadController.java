package Managers;

import Managers.Networking.NetworkManager;
import Managers.Thread.NetworkUpdateThread;

public class ThreadController {
    private static NetworkUpdateThread networkUpdateThread;

    public static void InitNetworkUpdateThread(NetworkManager networkManager)
    {
        networkUpdateThread = new NetworkUpdateThread(networkManager);
        networkUpdateThread.run();
    }

    public static NetworkUpdateThread getNetworkUpdateThread() {
        return networkUpdateThread;
    }
}
