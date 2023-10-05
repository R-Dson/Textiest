package Managers;

import DataShared.Network.NetworkMessages.Client.ConnectionEstablished;
import DataShared.Network.NetworkMessages.Client.CreationRequest;
import DataShared.Network.NetworkMessages.Client.LoginRequest;
import DataShared.Network.NetworkMessages.LoginEnum;
import DataShared.Network.NetworkMessages.Server.ChangeScene;
import DataShared.Network.NetworkMessages.Server.LoginError;
import DataShared.Network.NetworkMessages.Server.LoginResult;
import DataShared.Network.NetworkMessages.Server.UpdatePackage;
import Managers.Scenes.MainScene;
import Managers.Scenes.SceneChangeRunnable;
import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.vaniljstudio.textiest.Textiest;

public class ClientListener implements Listener {
    public static boolean ConnectedToServer = false;
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
            if (Textiest.CurrentScene instanceof MainScene ms)
            {
                UpdatePackage pkg = (UpdatePackage) obj;

                if (pkg.receiverData != null) {
                    PlayerManager.playerData = pkg.receiverData;
                    ms.updateData();
                }

                ms.updateUsers(pkg.otherPlayers);
                ms.addMessages(pkg.newMessages);
                ms.updateStatus(pkg.playerStatus);
                ms.updateFriends(pkg.updateFriends);
                ms.updateIgnore(pkg.updateIgnore);
                ms.updateLocation(pkg.changeMapFromServer);
                ms.updateNPCs(pkg.updateNPCs);

                if (pkg.updateObjects != null)
                    ms.updateObjects(pkg.updateObjects.sentWorldObjects);

            }
        }
        else if(obj instanceof LoginResult Result){

            if(Result.result == LoginEnum.SUCCESS){
                PlayerManager.playerData = Result.data;
            }
            else if (Result.result == LoginEnum.FAIL){
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
        Textiest.uniqueConnectID = ce.UniqueConnectID;
    }
}
