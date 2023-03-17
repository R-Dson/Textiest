package Managers.Networking;

import DataShared.Network.NetworkMessages.*;
import DataShared.Network.NetworkMessages.Client.ConnectionEstablished;
import DataShared.Network.NetworkMessages.Client.CreationRequest;
import DataShared.Network.NetworkMessages.Client.LoginRequest;
import DataShared.Network.NetworkMessages.Server.*;
import Managers.OtherPlayerManager;
import Managers.PlayerManager;
import Managers.Scenes.ConnectionScene;
import Managers.Scenes.LoginScene;
import Managers.Scenes.MainScene;
import Managers.Scenes.SceneChangeRunnable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.vaniljstudio.textiest.Textiest;

import java.io.IOException;

public class GameClient {
    
    private Client client;
    private Kryo kryo;

    public static boolean ConnectedToServer = false;


    public void StartClient() {

        InitClient();
        Register();

        AddListeners();

        //send confirmation after
        this.client.sendTCP(new ConnectionEstablished());
    }

    private void Register(){
        kryo = client.getKryo();
        DataShared.Network.NetworkManager.Register(kryo);
    }

    private void InitClient(){
        client = new Client();
        client.start();

    }

    private float AttemptTimer = 0;
    private float UpdateTimer = 0;

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

    public static int ConnectionAttempts = 0;

    public boolean AttemptConnection(){

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
            client.connect(5000, "localhost", 54555);
            ConnectionEstablished connectionEstablished = new ConnectionEstablished();
            connectionEstablished.text = "Connected";
            client.sendTCP(connectionEstablished);
            return true;
        }
        catch (IOException e)
        {
            //e.printStackTrace();
            return false;
        }
    }

    private void AddListeners(){
        this.client.addListener(new ClientListener());
    }

    class ClientListener implements Listener {
        @Override
        public void connected(Connection connection) {
            ConnectedToServer = true;
        }

        @Override
        public void disconnected(Connection connection) {
            ConnectedToServer = false;
        }

        @Override
        public void received(Connection connection, Object obj) {

            if (obj instanceof ConnectionEstablished){
                SetUpID((ConnectionEstablished) obj);
            }
            else if (obj instanceof LoginError){
                loginError(connection, obj);
            }
            else if (obj instanceof UpdatePackage)
            {


                if (Textiest.CurrentScene instanceof MainScene)
                {
                    MainScene ms = (MainScene) Textiest.CurrentScene;

                    UpdatePackage pkg = (UpdatePackage) obj;
                    if (pkg.receiverData != null) {
                        PlayerManager.playerData = pkg.receiverData;
                        ms.updateData();
                    }
                    OtherPlayerManager.OtherPlayerData = pkg.otherPlayers;



                    ms.updateUsers(OtherPlayerManager.OtherPlayerData);
                    ms.addMessages(pkg.newMessages);
                    ms.updateStatus(pkg.playerStatus);

                    if (pkg.changeMapFromServer != null)
                        ms.updateLocation(pkg.changeMapFromServer);

                    if (pkg.updateObjects != null)
                        ms.updateObjects(pkg.updateObjects.sentWorldObjects);
                    //if (pkg.updateFriends != null)

                }
            }
            else if(obj instanceof LoginResult){
                LoginResult Lresult = (LoginResult)obj;

                if(Lresult.result == LoginEnum.SUCCESS){
                    PlayerManager.playerData = Lresult.data;
                }
                else if (Lresult.result == LoginEnum.FAIL){
                    //Error wrong info
                }
            }
            else if (obj instanceof ChangeScene){
                ChangeScene cs = (ChangeScene)obj;
                System.out.println("Changing scene to: " + cs.sceneName);
                Runnable runnable = new SceneChangeRunnable(cs.sceneName, client);
                Gdx.app.postRunnable(runnable);

                if (Textiest.CurrentScene instanceof MainScene)
                    ((MainScene) Textiest.CurrentScene).updateLocation();
            }
            else if (obj instanceof CreationRequest){
                CreationRequest cr = (CreationRequest)obj;
                PlayerManager.playerData = cr.playerData;

                LoginRequest request = new LoginRequest();
                request.Username = "testasssa";
                request.Password = "testasssa";
                client.sendTCP(request);
            }


        }

        private void loginError(Connection connection, Object obj){
            LoginError error = (LoginError)obj;
            System.out.println("Error: " + error.ErrorMessage + " Error type: " + error.ErrorEnum);
        }

        private void SetUpID(ConnectionEstablished ce){
            Textiest.uniqueID = ce.text;
        }
    }

    public Client getClient() {
        return client;
    }
}
