package Managers.Scenes;

import DataShared.Network.NetworkMessages.SceneNameEnum;
import com.esotericsoftware.kryonet.Client;
import com.vaniljstudio.lilite.Lilite;

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
                Lilite.CurrentScene = new MainScene(_client);
                break;
            case LoginScene:
                Lilite.CurrentScene = new LoginScene();
                break;
            case CharacterCreationScene:
                Lilite.CurrentScene = new CharacterCreationScene(_client);
                break;
        }
    }
}
