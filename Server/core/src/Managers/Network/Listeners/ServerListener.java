package Managers.Network.Listeners;

import DataShared.Network.NetworkMessages.*;
import DataShared.Network.NetworkMessages.Client.ConnectionEstablished;
import DataShared.Network.NetworkMessages.Client.CreationRequest;
import DataShared.Network.NetworkMessages.Client.LoginRequest;
import DataShared.Network.NetworkMessages.Server.ChangeScene;
import DataShared.Network.NetworkMessages.Server.LoginError;
import DataShared.Network.NetworkMessages.Server.LoginResult;
import DataShared.Player.PlayerData;
import Managers.*;
import Managers.Network.UserIdentity;

import com.badlogic.gdx.utils.Json;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import DataShared.Network.NetworkMessages.Client.RegisterRequest;
import com.vaniljstudio.server.ServerClass;


import java.util.UUID;


public class ServerListener implements Listener {
    private final static Json json = new Json();

    private enum FunctionName {
        ConnectionEstablished{
            @Override
            void action(ServerListener sl, Connection conn, NetworkPackage np) {
                sl.connectionEstablished(conn);
            }
        },
        LoginRequest{
            @Override
            void action(ServerListener sl, Connection conn, NetworkPackage np) {
                LoginRequest loginRequest = ServerListener.json.fromJson(LoginRequest.class, np.jsonParameters);
                sl.loginRequest(conn, loginRequest);
            }
        },
        RegisterRequest{
            @Override
            void action(ServerListener sl, Connection conn, NetworkPackage np) {
                RegisterRequest loginRequest = ServerListener.json.fromJson(RegisterRequest.class, np.jsonParameters);
                sl.registerRequest(conn, loginRequest);
            }
        },
        UpdatePackageToServer{
            @Override
            void action(ServerListener sl, Connection conn, NetworkPackage np) {

            }
        },
        CreationRequest{
            @Override
            void action(ServerListener sl, Connection conn, NetworkPackage np) {

            }
        };

        abstract void action(ServerListener sl, Connection conn, NetworkPackage np);
    }

    private final SQLManager sqlManager;
    public ServerListener()
    {
        super();
        sqlManager = ServerClass.getController().getSQLManager();
    }

    @Override
    public void connected(Connection connection) {

    }

    @Override
    public void disconnected(Connection connection) {
        UserIdentity identity = EntityManager.getUserIdentityByConnectID(connection.getID());
        if (identity == null)
            return;
        String jsonString = json.toJson(identity.playerData);
        sqlManager.UpdateData(identity.UserName, jsonString);
        EntityManager.RemoveUserIdentity(identity);
    }

    @Override
    public void received(Connection connection, Object obj) {

        if (obj instanceof NetworkPackage networkPackage)
        {
            FunctionName function;
            try{
                function = FunctionName.valueOf(networkPackage.fName);
            }
            catch (IllegalArgumentException e){
                Logger.log(Logger.ErrorLevel.CRITICAL, "failed to find function with name " + networkPackage.fName);
                return;
            }
            try{
                function.action(this, connection, networkPackage);
            }catch (Exception e)
            {
                String error = "failed to run function " + networkPackage.fName + ".";
                if (networkPackage.jsonParameters != null)
                    error += "\nJson string: " + networkPackage.jsonParameters;
                Logger.log(Logger.ErrorLevel.CRITICAL, error);
            }
        }
    }





    public void connectionEstablished(Connection connection){
        String uniqueConnectID = UUID.randomUUID().toString();
        System.out.println("New connection. ID: " + connection.getID() + ", Unique ID: " + uniqueConnectID);

        //Reply
        ConnectionEstablished ce = new ConnectionEstablished();
        ce.UniqueConnectID = uniqueConnectID;
        connection.sendTCP(ce);
        ChangeScene changeScene = new ChangeScene();
        changeScene.sceneName = SceneNameEnum.LoginScene;
        connection.sendTCP(changeScene);
    }

    public void loginRequest(Connection connection, LoginRequest request){

        String encPsw = sqlManager.requestUsernameExist(request.Username);

        //null if username doesn't exist
        if (encPsw != null){
            boolean equal = BcryptManager.verify(request.Password, encPsw);

            if (equal) {

                //Get CharacterData from SQL
                String data = sqlManager.requestData(request.Username);

                PlayerData dataSQL = json.fromJson(PlayerData.class, data);
                dataSQL.UserName = request.Username;
                dataSQL.UniqueConnectID = request.UniqueConnectID;

                LoginResult result = new LoginResult();

                result.data = dataSQL;
                result.result = LoginEnum.SUCCESS;

                //Change client scene
                ChangeScene changeScene = new ChangeScene();
                //assign to a map and layer
                UserIdentity identity = new UserIdentity(request.UniqueConnectID, connection.getID());
                identity.UserName = dataSQL.UserName;
                identity.playerData = dataSQL;

                //EntityManager.addUserIdentityList(identity);
                EntityManager.addIdentityToBeAssigned(identity);
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

    public void registerRequest(Connection connection, RegisterRequest request){
        //TODO Encrypt user side later
        String encrypt = BcryptManager.Encrypt(request.Password);

        PlayerData playerData = new PlayerData();
        PlayerManager.GenerateData(playerData);

        String jsonString = json.toJson(playerData);
        sqlManager.addData(request.Username, encrypt, jsonString);

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
