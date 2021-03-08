package Managers.Scenes;

import Data.PlayerData;
import Data.StaticValues;
import Data.UpdatePackageToServer;
import Data.updatePackageToServerDummy;
import Managers.Animation.Animation;
import Managers.CameraManager;
import Managers.DataManager;
import Managers.InputManager;
import Managers.Map.MapManager;
import Managers.PlayerManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.kryonet.Client;

public class MainScene extends Scene {

    private MapManager _MapManager;
    private CameraManager _CameraManager;
    private OrthogonalTiledMapRenderer _Renderer;
    private InputManager _InputManager;
    private Client _Client;
    private Animation _Animation;

    public MainScene(Client client){
        super();
        _Client = client;
    }

    @Override
    public void render(SpriteBatch batch) {
        _CameraManager.render(batch);
        if (_CameraManager.get_Camera() != null){
            //Vector2 position = PlayerManager.playerData.PlayerPosition;
            //_CameraManager.get_Camera().position.lerp(new Vector3(position.x, position.y, 0), 0.1f);
            _Renderer.setView(_CameraManager.get_Camera());
            _Renderer.render();
            if (_Animation != null && _Animation.GetCurrentTextureRegion() != null){
                stage.getBatch().begin();
                Vector2 position = PlayerManager.playerData.PlayerPosition;
                TextureRegion tr = _Animation.GetCurrentTextureRegion();
                Vector3 pos = new Vector3(PlayerManager.playerData.PlayerPosition.x, PlayerManager.playerData.PlayerPosition.y, 0);
                _CameraManager.get_Camera().project(pos);
                stage.getBatch().draw(tr, pos.x - tr.getRegionWidth() / 2f, pos.y - tr.getRegionHeight() / 2f, 0, 0, tr.getRegionWidth(), tr.getRegionHeight(), 1, 1, 0);
                stage.getBatch().end();
            }
        }
    }

    private float timer = 0;

    @Override
    public void update(float delta) {
        _InputManager.Update(delta);
        _Animation.Update(delta);
        timer += delta;
        if (timer > StaticValues.updateFrequency)
        {
            UpdatePackageToServer updatePackageToServer = new UpdatePackageToServer();

            //move dummy values to package object
            updatePackageToServer.inputsAsIntegers = updatePackageToServerDummy.inputsAsIntegers;

            //Send data
            _Client.sendTCP(updatePackageToServer);

            //clear the current values in the dummy
            updatePackageToServerDummy.inputsAsIntegers.clear();

            timer -= StaticValues.updateFrequency;
        }

    }

    @Override
    public void create(){
        //Independent new functions
        _MapManager = new MapManager();
        _CameraManager = new CameraManager();
        _InputManager = new InputManager();
        _Animation = new Animation(DataManager._textureManager.GenerateAnimationTextures(PlayerManager.playerData.playerTextureName));
        Gdx.input.setInputProcessor(_InputManager);
        //create functions
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                _MapManager.create("Maps/map.tmx");
                _CameraManager.create(_MapManager);
                //Dependent new functions
                _Renderer = new OrthogonalTiledMapRenderer(_MapManager.get_TiledMap(), 1/64f);
            }
        });


    }

    @Override
    public void dispose() {
        _MapManager.dispose();
    }
}
