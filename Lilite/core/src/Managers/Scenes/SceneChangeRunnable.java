package Managers.Scenes;

import Managers.Networking.NetworkingMessages.SceneNameEnum;
import com.esotericsoftware.kryonet.Client;

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
            case CharacterCreationScene:
                com.vaniljstudio.lilite.Lilite.CurrentScene = new CharacterCreationScene(_client);
                break;
            case MainScene:
                com.vaniljstudio.lilite.Lilite.CurrentScene = new MainScene(_client);
                break;
        }
    }
}
