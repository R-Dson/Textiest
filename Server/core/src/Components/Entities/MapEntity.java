package Components.Entities;

import Data.UEntity;
import Managers.Map.Map;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

public class MapEntity extends UEntity {

    public OrthographicCamera camera;
    public OrthogonalTiledMapRenderer renderer;
    public Map map;
    public Box2DDebugRenderer debugRenderer;

    public MapEntity(Map map){
        super();
        this.map = map;
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(30, 30 * (h / w));

        camera.position.set(camera.viewportWidth / 2f, 5*camera.viewportHeight / 2f, 0);
        camera.zoom += 3;
        renderer = new OrthogonalTiledMapRenderer(map.get_TiledMap(), 1/64f);

        debugRenderer = new Box2DDebugRenderer();

        //TODO set which layer to render
    }

    @Override
    public void Update(float delta) {
        map.Update(delta);
        camera.update();
        if (camera != null && map.world != null)
            //debugRenderer.render(map.world, camera.combined);
        super.Update(delta);
    }
}
