package Managers.Scenes;

import Data.StaticValues;
import Data.UpdatePackageToServer;
import Data.updatePackageToServerDummy;
import Managers.CameraManager;
import Managers.InputManager;
import Managers.Map.MapManager;
import Managers.PlayerManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.kryonet.Client;

public class MainScene extends Scene {

    private MapManager _MapManager;
    private CameraManager _CameraManager;
    private OrthogonalTiledMapRenderer _Renderer;
    private InputManager _InputManager;
    private Client _Client;

    public MainScene(Client client){
        super();
        _Client = client;
    }

    @Override
    public void render(SpriteBatch batch) {
        _CameraManager.render(batch);
        if (_CameraManager.get_Camera() != null){
            _Renderer.setView(_CameraManager.get_Camera());
            _Renderer.render();
        }
    }

    private float timer = 0;

    @Override
    public void update(float delta) {
        _InputManager.Update(delta);
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
