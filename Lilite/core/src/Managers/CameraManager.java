package Managers;

import Managers.Map.MapManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class CameraManager extends OrthographicCamera {

    private OrthographicCamera _Camera;
    private Vector2 targetPosition = new Vector2(0,0);
    private Vector2 lastPosition = new Vector2(0,0);
    private float lerpA = 0;

    public void create(MapManager mapManager){
        //TODO: Change to width and height
        _Camera = new OrthographicCamera(1320f, 1180f);
        //_Camera.zoom -= 0.95;
        //TODO: Set position to character when logged in
        _Camera.position.x = 0;
        _Camera.position.y = 80;
    }
    final float speed=0.1f,ispeed=1.0f-speed;
    public void render(SpriteBatch batch){
        if (_Camera != null){
            Vector3 cameraPosition = _Camera.position;
            targetPosition = PlayerManager.playerData.PlayerPosition.cpy();

            //TODO: FIX CAMERA ASAP
            cameraPosition.scl(ispeed);
            targetPosition.scl(speed);
            cameraPosition.add(new Vector3(targetPosition.x, targetPosition.y, 0));
            _Camera.position.set(cameraPosition);
            System.out.println(targetPosition + " : " + cameraPosition);
        }
    }

    public void dispose(){

    }

    public OrthographicCamera get_Camera() {
        return _Camera;
    }
}
