package Managers.Networking;

import DataShared.Network.UpdatePackageToServer;
import Managers.Scenes.LoginScene;
import Managers.ThreadController;
import com.esotericsoftware.kryonet.Client;
import com.vaniljstudio.textiest.Textiest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Semaphore;

public class NetworkManager {
    private final GameClient client;
    private final Semaphore updatePackageSem = new Semaphore(0);
    private final ArrayList<UpdatePackageToServer> updatePackagesToServer = new ArrayList<>();
    private float UpdateTimer = 0;

    public NetworkManager(){
        client = new GameClient();
        client.isConnected();

        ThreadController.InitNetworkUpdateThread(this);
    }

    public synchronized void ThreadUpdater() throws InterruptedException
    {
        updatePackageSem.acquire();
        Iterator<UpdatePackageToServer> it = updatePackagesToServer.iterator();
        while (it.hasNext()){
            UpdatePackageToServer next = it.next();
            client.SendTCP(UpdatePackageToServer.class.getName(), next);
            it.remove();
        }
    }

    public void Update(float delta){
        UpdateTimer += delta;
        if (UpdateTimer > 1/20f){
            //TODO: add reconnect then disconnect completely
            if (!client.isConnected())
            {
                client.close();
                Textiest.CurrentScene = new LoginScene();
            }
            UpdateTimer -= 1/20f;
        }
    }

    public synchronized void AddUpdatePackage(UpdatePackageToServer updatePackageToServer)
    {
        updatePackagesToServer.add(updatePackageToServer);
        updatePackageSem.release();
    }

    public boolean CheckConnection(){
        if (client == null) return false;
        if (client.isConnected()) return true;
        return client.AttemptConnection();
    }

    public GameClient getClient() {
        return client;
    }
}
