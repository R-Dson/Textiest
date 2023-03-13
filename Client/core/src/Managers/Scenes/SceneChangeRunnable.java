package Managers.Scenes;

import DataShared.Network.NetworkMessages.SceneNameEnum;
import com.esotericsoftware.kryonet.Client;
import com.vaniljstudio.textiest.Textiest;

public class SceneChangeRunnable implements Runnable{
    private SceneNameEnum sceneNameEnum;
    private Client _client;

    public SceneChangeRunnable(SceneNameEnum changeScene, Client client){
        sceneNameEnum = changeScene;
        _client = client;
    }

    @Override
    public void run() {
        switch (sceneNameEnum){
            case MainScene:
                Textiest.CurrentScene = new MainScene(_client);
                break;
            case LoginScene:
                Textiest.CurrentScene = new LoginScene();
                break;
        }
    }
}
