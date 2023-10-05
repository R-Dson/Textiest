package Components.Entities;

import Managers.Map.Map;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

public class MapEntity extends Entity {

    public OrthographicCamera camera;
    // public OrthogonalTiledMapRenderer renderer;
    public Box2DDebugRenderer debugRenderer;

    public MapEntity(Map map){
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(30, 30 * (h / w));

        camera.position.set(camera.viewportWidth / 2f, 5*camera.viewportHeight / 2f, 0);
        camera.zoom += 3;
        //renderer = new OrthogonalTiledMapRenderer(map.getTiledMap(), 1/64f);

        debugRenderer = new Box2DDebugRenderer();
        add(map);
    }
}
