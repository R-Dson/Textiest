package Managers.Scenes;

import Managers.CameraManager;
import Managers.Managers;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.vaniljstudio.textiest.Textiest;

public class ConnectionScene extends Scene{
    public VisLabel userLabel;
    Managers managers;

    public ConnectionScene(Managers managers)
    {
        super();
        this.managers = managers;

        CameraManager cameraManager = managers.getCameraManager();
        ExtendViewport viewport = new ExtendViewport((int)cameraManager.get_Camera().viewportWidth, (int)cameraManager.get_Camera().viewportHeight);
        stage.setViewport(viewport);
        stage.getViewport().update((int)cameraManager.get_Camera().viewportWidth, (int)cameraManager.get_Camera().viewportHeight, true);

    }

    @Override
    public void create() {
        managers.setCameraManager(new CameraManager());
        managers.getCameraManager().create();

        userLabel = new VisLabel("Connecting...");
        table.add(userLabel);
        table.setDebug(true);

        managers.getNetworkManager().CheckConnection();
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.enableBlending();
        batch.disableBlending();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    float timer = 0;
    @Override
    public void update(float delta) {
        timer += delta;
        if (timer > 12)
        {

            timer -= 12;
        }
    }

    @Override
    public void dispose() {
        VisUI.dispose();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
}
