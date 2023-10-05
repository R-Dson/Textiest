package Managers.Thread;

import Managers.Networking.NetworkManager;

public class NetworkUpdateThread implements Runnable{

    private final NetworkManager networkManager;
    public NetworkUpdateThread(NetworkManager networkManager)
    {
        this.networkManager = networkManager;
    }

    @Override
    public void run() {
        while (true)
        {
            try {
                networkManager.ThreadUpdater();
            }
            catch (InterruptedException exception)
            {

            }
        }
    }
}
