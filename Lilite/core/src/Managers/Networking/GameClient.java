package Managers.Networking;

import Data.PlayerData;
import Data.UpdatePackage;
import Data.UpdatePackageToServer;
import Managers.Items.EquipmentItem;
import Managers.Networking.NetworkingMessages.*;
import Managers.OtherPlayerManager;
import Managers.PlayerManager;
import Managers.Scenes.CharacterCreationScene;
import Managers.Scenes.LoginScene;
import Managers.Scenes.MainScene;
import Managers.Scenes.SceneChangeRunnable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;
import java.util.ArrayList;

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
        kryo.register(ConnectionEstablished.class);
        kryo.register(LoginRequest.class);
        kryo.register(LoginError.class);
        kryo.register(ErrorEnum.class);
        kryo.register(RegisterRequest.class);
        kryo.register(PlayerData.class);
        kryo.register(UpdatePackage.class);
        kryo.register(ArrayList.class);
        kryo.register(EquipmentItem[].class);
        kryo.register(Vector2.class);
        kryo.register(LoginResult.class);
        kryo.register(LoginEnum.class);
        kryo.register(UpdatePackageToServer.class);
        kryo.register(SceneNameEnum.class);
        kryo.register(CreationRequest.class);
        kryo.register(ChangeScene.class);
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
                com.vaniljstudio.lilite.Lilite.CurrentScene = new LoginScene();
            }
            UpdateTimer -= 1/20f;
        }
    }

    public boolean AttempConnection(){
        boolean connected = Connect();
        int attempts = 0;

        while (!connected && attempts < 5){
            AttemptTimer += Gdx.graphics.getDeltaTime();
            if(AttemptTimer > 3){
                attempts++;
                Gdx.app.log("NETWORK_ERROR", "Attempting to connect! Total attempts: " + attempts);
                connected = Connect();
                AttemptTimer -= 3;
            }

        }
        return connected;
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
            e.printStackTrace();
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
                UpdatePackage pkg = (UpdatePackage)obj;
                PlayerManager.playerData = pkg.recieverData;
                OtherPlayerManager.OtherPlayerData = pkg.OtherPlayers;
            }
            else if(obj instanceof LoginResult){
                LoginResult Lresult = (LoginResult)obj;

                if(Lresult.result == LoginEnum.SUCCESS){
                    PlayerManager.playerData = Lresult.data;

                    //if (Lresult.sceneName == SceneNameEnum.CharacterCreationScene)
                        //com.skylight.game.Client.CurrentScene = new CharacterCreationScene(client);
                    //else if (Lresult.sceneName == SceneNameEnum.MainScene)
                    //   com.skylight.game.Client.CurrentScene = new MainScene(client);
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
            }

        }

        private void loginError(Connection connection, Object obj){
            LoginError error = (LoginError)obj;
            System.out.println("Error: " + error.ErrorMessage + " Error type: " + error.ErrorEnum);
        }

        private void SetUpID(ConnectionEstablished ce){
            com.vaniljstudio.lilite.Lilite.uniqueID = ce.text;
            //TODO Change later to username text


        }
    }

    public Client getClient() {
        return client;
    }
}
