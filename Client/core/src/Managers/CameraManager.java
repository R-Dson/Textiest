package Managers;

import Managers.Map.MapManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class CameraManager {

    private OrthographicCamera _Camera;

    public void create(){
        //TODO: Change to width and height

        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();
        _Camera = new OrthographicCamera(width, height);
        _Camera.update();
    }

    final float speed = 0.25f, ispeed = 1.0f - speed;

    public void render(SpriteBatch batch){
        if (_Camera != null){
            //Vector3 cameraPosition = _Camera.position;
            //cameraPosition.scl(ispeed);
            _Camera.update();
        }
    }

    public void dispose(){

    }

    public OrthographicCamera get_Camera() {
        return _Camera;
    }
}
