package Managers.Network;

import Data.PlayerData;
import Data.UpdatePackage;
import Data.UpdatePackageToServer;
import Managers.BcryptManager;
import Managers.EntityManager;
import Managers.InputManager;
import Managers.Items.EquipmentItem;
import Managers.Map.MapManager;
import Managers.Network.NetworkingMessages.*;
import Managers.SQLManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
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

        //add register
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
        kryo.register(AssignRequest.class);

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
            EntityManager.getEntityByConnectID(connection.getID());
        }

        @Override
        public void received(Connection connection, Object obj) {
            if (obj instanceof ConnectionEstablished)
            {
                connectionEstablished(connection, obj);
            }
            else if(obj instanceof LoginRequest){
                loginRequest(connection, obj);
            }
            else if (obj instanceof RegisterRequest) {
                registerRequest(connection, obj);
            }
            else if (obj instanceof UpdatePackageToServer){
                UpdatePackageToServer upts = (UpdatePackageToServer)obj;
                if (!upts.inputsAsIntegers.isEmpty()){
                    UserIdentity ui = EntityManager.getEntityByConnectID(connection.getID());
                    InputManager.CalculateMovement(ui, upts.inputsAsIntegers);
                }
            }
            else if (obj instanceof CreationRequest){

            }
            else if (obj instanceof AssignRequest)
            {

            }



        }

        private void loginRequest(Connection connection, Object obj){
            LoginRequest request = (LoginRequest)obj;
            String encPsw = SQLManager.requestUsernameExist(request.Username);

            //null if username doesn't exist
            if (encPsw != null){
                boolean equal = BcryptManager.verify(request.Password, encPsw);

                if (equal) {

                    //Get CharacterData from SQL
                    String data = SQLManager.requestData(request.Username);

                    Json json = new Json();
                    PlayerData dataSQL = json.fromJson(PlayerData.class, data);

                    LoginResult result = new LoginResult();
                    result.data = dataSQL;
                    result.result = LoginEnum.SUCCESS;

                    //Change client scene
                    ChangeScene changeScene = new ChangeScene();
                    changeScene.sceneName = SceneNameEnum.MainScene;
                    if (true)
                    {
                        //assign to a map and layer
                        UserIdentity identity = new UserIdentity(request.UniqueID, connection.getID());
                        identity.UserName = request.Username;
                        identity.playerData = dataSQL;
                        ToBeAssigned.add(identity);
                    }

                    //connection.sendTCP(changeScene);
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

        private void connectionEstablished(Connection connection, Object obj){
            String uniqueID = UUID.randomUUID().toString();
            System.out.println("New connection. ID: " + connection.getID() + ", Unique ID: " + uniqueID);

            //Reply
            ConnectionEstablished ce = new ConnectionEstablished();
            ce.text = uniqueID;
            connection.sendTCP(ce);
        }

        private Json json = new Json();
        private void registerRequest(Connection connection, Object obj){
            //TODO Encrypt user side later
            RegisterRequest request = (RegisterRequest) obj;
            String encrypt = BcryptManager.Encrypt(request.Password);

            PlayerData playerData = new PlayerData();
            playerData.GenerateData();

            String jsonString = json.toJson(playerData);
            SQLManager.addData(request.Username, encrypt, jsonString);
        }

    }

    private float UTimer = 0;
    public void Update(float delta) {
        UTimer += delta;
        if (UTimer >= 0.25)
        {
            if (ToBeAssigned.size() > 0)
            {
                Iterator iterator = ToBeAssigned.iterator();
                //Json json = new Json();

                while (iterator.hasNext())
                {
                    UserIdentity next = (UserIdentity)iterator.next();
                    //String data = _SQLManager.requestData(next.UserName);
                    //next.playerData = json.fromJson(PlayerData.class, data);
                    EntityManager.EntityList.add(next);
                    MapManager.AssignLogin(next);
                    iterator.remove();
                }
            }

            UTimer -= 0.25;
        }
    }

    public Server getServer() {
        return server;
    }
}