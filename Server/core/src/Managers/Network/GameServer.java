package Managers.Network;

import DataShared.Network.NetworkMessages.*;
import DataShared.Network.UpdatePackageToServer;
import DataShared.Player.PlayerData;
import Managers.*;
import Managers.Map.MapManager;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Timer;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.vaniljstudio.server.ServerClass;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameServer {

    private Server server;
    private Kryo kryo;
    private SQLManager SQLManager;
    private MapManager MapManager;
    private EntityManager EntityManager;

    private ArrayList<UserIdentity> ToBeAssigned = new ArrayList<>();

    public void startServer(){
        SQLManager = ServerClass.SQLManager;
        MapManager = ServerClass.MapManager;
        EntityManager = ServerClass.EntityManager;

        InitServer();
        Register();
        AddListeners();
        server.start();

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
        //get kryo
        kryo = server.getKryo();
        DataShared.Network.NetworkManager.Register(kryo);
    }

    private void AddListeners(){
        ServerListener listener = new ServerListener();
        server.addListener(listener);
    }
    
    class ServerListener implements Listener {

        @Override
        public void connected(Connection connection) {

        }

        @Override
        public void disconnected(Connection connection) {
            UserIdentity identity = EntityManager.getUserIdentityByConnectID(connection.getID());
            if (identity != null){
                String jsonString = json.toJson(identity.playerData);
                SQLManager.UpdateData(identity.UserName, jsonString);
                EntityManager.RemoveUserIdentity(identity);
            }
        }

        @Override
        public void received(Connection connection, Object obj) {
            if (obj instanceof ConnectionEstablished)
            {
                connectionEstablished(connection, (ConnectionEstablished)obj);
            }
            else if(obj instanceof LoginRequest){
                loginRequest(connection, (LoginRequest)obj);
            }
            else if (obj instanceof RegisterRequest) {
                registerRequest(connection, (RegisterRequest)obj);
            }
            else if (obj instanceof UpdatePackageToServer){
                UpdatePackageToServer updatePackageToServer = (UpdatePackageToServer)obj;
                UserIdentity ui = EntityManager.getUserIdentityByConnectID(connection.getID());
                RecievedManager.AddMovementRequest(ui, updatePackageToServer.inputsAsIntegers);
            }
            else if (obj instanceof CreationRequest){
                CreationRequest cr = (CreationRequest)obj;

                UserIdentity identity = EntityManager.getUserIdentityByConnectID(connection.getID());
                identity.playerData.playerTextureName = cr.playerData.playerTextureName;
                //identity.playerData.playerTextureID = cr.playerData.playerTextureID;
                identity.playerData.Name = identity.UserName + "." + cr.playerData.Name;

                identity.playerData.doneCharacterCreation = true;

                String jsonString = json.toJson(identity.playerData);
                SQLManager.UpdateData(identity.UserName, jsonString);
                ChangeScene changeScene = new ChangeScene();
                changeScene.sceneName = SceneNameEnum.MainScene;
                connection.sendTCP(changeScene);

            }
            else if (obj instanceof AssignRequest)
            {

            }

        }

        private void loginRequest(Connection connection, LoginRequest request){

            String encPsw = SQLManager.requestUsernameExist(request.Username);

            //null if username doesn't exist
            if (encPsw != null){
                boolean equal = BcryptManager.verify(request.Password, encPsw);

                if (equal) {

                    //Get CharacterData from SQL
                    String data = SQLManager.requestData(request.Username);

                    Json json = new Json();
                    PlayerData dataSQL = json.fromJson(PlayerData.class, data);
                    dataSQL.UserName = request.Username;

                    LoginResult result = new LoginResult();

                    result.data = dataSQL;
                    result.result = LoginEnum.SUCCESS;

                    //Change client scene
                    ChangeScene changeScene = new ChangeScene();
                    //assign to a map and layer
                    UserIdentity identity = new UserIdentity(request.UniqueID, connection.getID());
                    identity.UserName = request.Username;
                    identity.playerData = dataSQL;
                    ToBeAssigned.add(identity);

                    if (result.data.doneCharacterCreation)
                    {
                        changeScene.sceneName = SceneNameEnum.MainScene;
                    }
                    else {
                        changeScene.sceneName = SceneNameEnum.CharacterCreationScene;
                    }

                    connection.sendTCP(changeScene);
                    connection.sendTCP(result);
                    //Else not registered

                }
                else
                {
                    //Error if wrong password
                    LoginError error = new LoginError();
                    error.ErrorEnum = ErrorEnum.Invalid_Password;
                    error.ErrorMessage = "Error invalid password.";
                    connection.sendTCP(error);
                }
            }
            else{
                LoginError error = new LoginError();
                error.ErrorEnum = ErrorEnum.Invalid_Username;
                error.ErrorMessage = "Error invalid username.";
                connection.sendTCP(error);
            }

        }

        private void connectionEstablished(Connection connection, ConnectionEstablished obj){
            String uniqueID = UUID.randomUUID().toString();
            System.out.println("New connection. ID: " + connection.getID() + ", Unique ID: " + uniqueID);

            //Reply
            ConnectionEstablished ce = new ConnectionEstablished();
            ce.text = uniqueID;
            connection.sendTCP(ce);
            ChangeScene changeScene = new ChangeScene();
            changeScene.sceneName = SceneNameEnum.LoginScene;
            connection.sendTCP(changeScene);
        }

        private final Json json = new Json();
        private void registerRequest(Connection connection, RegisterRequest request){
            //TODO Encrypt user side later
            String encrypt = BcryptManager.Encrypt(request.Password);

            PlayerData playerData = new PlayerData();
            PlayerManager.GenerateData(playerData);

            String jsonString = json.toJson(playerData);
            SQLManager.addData(request.Username, encrypt, jsonString);

            CreationRequest creationRequest = new CreationRequest();
            creationRequest.playerData = playerData;
            creationRequest.UserName = playerData.UserName;
            connection.sendTCP(creationRequest);

            /*Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    ChangeScene changeScene = new ChangeScene();
                    changeScene.sceneName = SceneNameEnum.CharacterCreationScene;
                    connection.sendTCP(changeScene);
                }
            }, 1.5f);*/

            /*try{
                Thread.sleep(1500);
            }
            catch (InterruptedException ignored)
            { }*/

        }

    }

    private float UTimer = 0;
    public void Update(float delta) {
        UTimer += delta;
        if (UTimer >= 0.25)
        {
            // Updates assignment to map/layer
            UpdateAssign(delta);

            // Updates from received data
            UpdateRecieved(delta);


            UTimer -= 0.25;
        }
    }

    Json json = new Json();
    private void UpdateAssign(float delta){
        if (ToBeAssigned.size() > 0)
        {
            Iterator<UserIdentity> iterator = ToBeAssigned.iterator();

            while (iterator.hasNext())
            {
                //TODO
                UserIdentity next = (UserIdentity)iterator.next();
                String data = SQLManager.requestData(next.UserName);
                next.playerData = json.fromJson(PlayerData.class, data);
                EntityManager.EntityList.put(next.connectionID, next);
                MapManager.AssignLogin(next);
                iterator.remove();
            }
        }
    }

    private void UpdateRecieved(float delta){
        while (RecievedManager.HasMovementRequest()){
            RecievedManager.MovementRequest request = RecievedManager.GetMovementRequest();
            InputManager.CalculateMovement(request.userIdentity, request.movementList);
        }
    }

    public Server getServer() {
        return server;
    }
}
