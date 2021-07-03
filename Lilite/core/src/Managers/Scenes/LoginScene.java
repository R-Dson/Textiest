package Managers.Scenes;

import Data.StaticValues;
import DataShared.Network.NetworkMessages.LoginRequest;
import DataShared.Network.NetworkMessages.RegisterRequest;
import Managers.Networking.GameClient;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.vaniljstudio.lilite.Lilite;

public class LoginScene extends Scene {

    private Sprite background;
    private Texture texture;
    private GameClient client;

    @Override
    public void create() {
        client = Lilite.GameClient;

        final VisLabel userLabel = new VisLabel("Username");
        VisLabel pswLabel = new VisLabel("Password");

        final VisTextField username = new VisTextField();
        final VisTextField password = new VisTextField();

        VisTextButton login = new VisTextButton("Login");
        VisTextButton register = new VisTextButton("Register");
        //boolean isConnected = CheckConnection();
        login.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (true){
                    LoginRequest request = new LoginRequest();
                    String usernameText = username.getText();
                    String passwordText = password.getText();
                    request.Password = "test";
                    request.Username = "t69";
                    //request.Username = "t";
                    //request.Password = "t";
                    request.UniqueID = com.vaniljstudio.lilite.Lilite.uniqueID;
                    client.getClient().sendTCP(request);
                }
            }
        });

        register.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (client.getClient().isConnected()){
                    RegisterRequest req = new RegisterRequest();
                    req.Password = "test";
                    req.Username = "t69";
                    client.getClient().sendTCP(req);
                    //TODO Change later to username text

                    /*LoginRequest request = new LoginRequest();
                    request.Password = "test";
                    request.Username = "tre4";
                    request.UniqueID = com.vaniljstudio.lilite.Lilite.uniqueID;*/
                    //client.getClient().sendTCP(request);
                }
                else{
                    CheckConnection();
                }
            }
        });

        table.add(userLabel);
        table.add(pswLabel);
        table.row();
        table.add(username);
        table.add(password);
        table.row();
        table.add(login);
        table.row();
        table.add(register);

        table.setDebug(true);

        texture = new Texture(Gdx.files.internal(StaticValues.WallpaperPath));
        background = new Sprite(texture);
    }

    private boolean CheckConnection(){
        if (client.getClient() == null) return false;
        if (client.getClient().isConnected()) return true;
        return client.AttemptConnection();
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.enableBlending();
        //batch.draw(texture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        background.draw(batch);
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
        texture.dispose();
    }
}
