package Managers.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTable;

public abstract class Scene {
    public Stage stage;
    public VisTable table;

    public Scene(){
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        if (!VisUI.isLoaded())
            VisUI.load();
        table = new VisTable(true);
        table.setFillParent(true);
        create();
        stage.addActor(table);

        stage.setDebugAll(true);
    }

    public abstract void create();

    public abstract void render(SpriteBatch batch);

    public abstract void update(float delta);

    public abstract void dispose();

}
