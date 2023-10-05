package Managers.Network;

import Managers.Network.Listeners.ServerListener;
import Managers.SQLManager;
import com.esotericsoftware.kryonet.Server;
import com.vaniljstudio.server.ServerClass;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NetworkManager {
    private Server server;
    private boolean running;

    public void startNetworkServer(){

        InitServer();
        Register();
        AddListeners();
        server.start();
        running = true;
    }

    private void InitServer() {
        server = new Server();
        //TODO: Change this
        try{
            server.bind(54555);
        }
        catch (IOException e){
            Logger.getLogger(GameServer.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void Register(){
        DataShared.Network.NetworkManager.Register(server.getKryo());
    }

    private void AddListeners(){
        ServerListener serverListener = new ServerListener();

        server.addListener(serverListener);
    }

    public Server getServer() {
        return server;
    }

    public boolean isRunning()
    {
        return running;
    }

    public void stopServer()
    {
        this.server.stop();
        running = false;
    }
}
