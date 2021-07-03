package Managers.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.vaniljstudio.lilite.Lilite;

public class ConnectionScene extends Scene{
    public VisLabel userLabel;
    @Override
    public void create() {
        userLabel = new VisLabel("Connecting...");
        table.add(userLabel);
        table.setDebug(true);

        CheckConnection();
    }

    private boolean CheckConnection(){
        if (Lilite.GameClient.getClient() == null) return false;
        if (Lilite.GameClient.getClient().isConnected()) return true;
        return Lilite.GameClient.AttemptConnection();
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
}
