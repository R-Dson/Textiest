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
        boolean b = CheckConnection();
        if (b)
            userLabel.setText("Connected.");
        else
            userLabel.setText("Failed Connection");
    }

    private boolean CheckConnection(){
        if (Lilite.GameClient.getClient() == null) return false;
        if (Lilite.GameClient.getClient().isConnected()) return true;
        return Lilite.GameClient.AttempConnection();
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.enableBlending();
        batch.disableBlending();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void dispose() {
        VisUI.dispose();
    }
}
