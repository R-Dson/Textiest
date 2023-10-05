package Managers.Scenes;

import DataShared.Network.NetworkMessages.Client.LoginRequest;
import DataShared.Network.NetworkMessages.Client.RegisterRequest;
import Managers.CameraManager;
import Managers.Networking.GameClient;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.vaniljstudio.textiest.Textiest;

public class LoginScene extends Scene {

    private Sprite background;
    private Texture texture;
    private GameClient client;
    private CameraManager _CameraManager;

    public LoginScene ()
    {
        super();
        ExtendViewport viewport = new ExtendViewport((int) _CameraManager.get_Camera().viewportWidth, (int) _CameraManager.get_Camera().viewportHeight);
        stage.setViewport(viewport);
        stage.getViewport().update((int) _CameraManager.get_Camera().viewportWidth, (int) _CameraManager.get_Camera().viewportHeight, true);

    }
    @Override
    public void create() {

        _CameraManager = new CameraManager();
        _CameraManager.create();

        client = Textiest.client;

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
                    request.UniqueConnectID = Textiest.uniqueConnectID;
                    client.sendTCP(request);
                }
            }
        });

        register.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (client.isConnected()){
                    RegisterRequest req = new RegisterRequest();
                    req.Username = "testasssa";
                    req.Password = "testasssa";
                    client.sendTCP(req);
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
        //login.setWidth(username.getWidth());
        //register.setWidth(username.getWidth());
        table.add(userLabel);
        table.add(pswLabel);
        table.row();
        table.add(username);
        table.add(password);
        table.row();
        table.add(login).fillX();
        table.row();
        table.add(register).fillX();

        table.setDebug(true);

        //background = new Sprite(texture);
    }

    private boolean CheckConnection(){
        if (client == null) return false;
        if (client.isConnected()) return true;
        return client.AttemptConnection();
    }

    @Override
    public void render(SpriteBatch batch) {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void dispose() {
        VisUI.dispose();
        stage.dispose();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        //stage.getActors().forEach(actor -> actor.setScale(Lilite.INIT_WIDTH/2/width, Lilite.INIT_HEIGHT/2/height));
        //table.getChildren().forEach(actor -> actor.setScale(Lilite.INIT_WIDTH/2/width, Lilite.INIT_HEIGHT/2/height));
    }
}
