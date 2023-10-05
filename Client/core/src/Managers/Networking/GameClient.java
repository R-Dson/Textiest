package Managers.Networking;

import Data.Events.received.UpdateOtherUsersEvent;
import DataShared.Network.NetworkMessages.*;
import DataShared.Network.NetworkMessages.Client.ConnectionEstablished;
import DataShared.Network.NetworkMessages.Client.CreationRequest;
import DataShared.Network.NetworkMessages.Client.LoginRequest;
import DataShared.Network.NetworkMessages.Server.*;
import DataShared.Network.UpdatePackageToServer;
import Managers.ClientListener;
import Managers.OtherPlayerManager;
import Managers.PlayerManager;
import Managers.Scenes.ConnectionScene;
import Managers.Scenes.LoginScene;
import Managers.Scenes.MainScene;
import Managers.Scenes.SceneChangeRunnable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Timer;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.vaniljstudio.textiest.Textiest;

import java.io.IOException;
import java.util.ArrayList;

public class GameClient extends Client{
    private final Json json = new Json();

    public GameClient()
    {
        super();
        SetupClient();
    }

    public void SetupClient() {
        // Starting
        this.start();

        // Register
        DataShared.Network.NetworkManager.Register(this.getKryo());

        // Listeners
        AddListeners();

        //send confirmation after
        SendTCP(ConnectionEstablished.class.getName(), new ConnectionEstablished());
    }

    public void SendTCP(String fName, Object params)
    {
        NetworkPackage np = new NetworkPackage();
        np.fName = fName;
        np.jsonParameters = json.toJson(params);
        this.sendTCP(np);
    }

    public static int ConnectionAttempts = 0;

    public boolean AttemptConnection(){
        Client client = this;
        Timer.Task task = new Timer.Task(){
            boolean connected = false;
            @Override
            public void run() {
                if(Textiest.CurrentScene instanceof ConnectionScene)
                    ((ConnectionScene) Textiest.CurrentScene).userLabel.setText("Connecting...");

                if (!connected){
                    ConnectionAttempts++;
                    Gdx.app.log("NETWORK_ERROR", "Attempting to connect! Total attempts: " + ConnectionAttempts);
                    connected = Connect();

                    if (!connected && ConnectionAttempts > 3){
                        if(Textiest.CurrentScene instanceof ConnectionScene)
                            ((ConnectionScene) Textiest.CurrentScene).userLabel.setText("Failed Connecting.");

                        Gdx.app.log("STATUS", "Connection status: " + client.isConnected() + " After " + ConnectionAttempts + "Attempts.");
                        this.cancel();
                    }
                }
                else{
                    if(Textiest.CurrentScene instanceof ConnectionScene)
                        ((ConnectionScene) Textiest.CurrentScene).userLabel.setText("Connected!");
                    Gdx.app.log("STATUS", "Connection status: " + client.isConnected() + " After " + ConnectionAttempts + "Attempts.");
                    this.cancel();
                }
            }
        };

        Gdx.app.log("STATUS", "Attempting to connect to server.");
        Timer.schedule(task, 5, 1, 1);
        return client.isConnected();
    }

    private boolean Connect(){
        try{
            this.connect(5000, "localhost", 54555);
            ConnectionEstablished connectionEstablished = new ConnectionEstablished();
            connectionEstablished.UniqueConnectID = "Connected";
            SendTCP(ConnectionEstablished.class.getName(), connectionEstablished);
            return true;
        }
        catch (IOException e)
        {
            //e.printStackTrace();
            return false;
        }
    }

    private void AddListeners(){
        this.addListener(new ClientListener());
    }
}
