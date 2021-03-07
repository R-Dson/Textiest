package Managers.Scenes;

import Data.StaticValues;
import Managers.Animation.Animation;
import Managers.DataManager;
import Managers.Networking.NetworkingMessages.CreationRequest;
import Managers.PlayerManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.esotericsoftware.kryonet.Client;
import com.kotcrab.vis.ui.widget.*;

public class CharacterCreationScene extends Scene{
    private Animation animation;
    private VisTable CharacterSelect;
    private Client _client;

    private int CharacterSelected = 0;

    private final String[] characterNamesFemale = new String[]{
            StaticValues.adventurer_f1,
            StaticValues.adventurer_f2
    };
    private final String[] characterNamesMale = new String[]{
            StaticValues.adventurer_m1,
            StaticValues.wizard4
    };

    public CharacterCreationScene(Client client){
        _client = client;
        ChangeCharacter();
    }

    //true if female else male
    private boolean gender = true;

    @Override
    public void create() {
        CharacterSelect = new VisTable();

        final VisLabel userLabel = new VisLabel("Character Name");

        final VisTextField username = new VisTextField();
        final VisTextButton Gender = new VisTextButton("Gender");
        Gender.addListener(new ClickListener(){
            @Override
            public void clicked (InputEvent event, float x, float y) {
                gender = !gender;
                CharacterSelected = 0;
                ChangeCharacter();
            }
        });
        VisTextButton next = new VisTextButton(">");
        next.addListener(new ClickListener(){
            @Override
            public void clicked (InputEvent event, float x, float y) {
                ChangeCharacter();
            }
        });
        VisTextButton last = new VisTextButton("<");

        VisTextButton finish = new VisTextButton("Create");
        finish.addListener(new ClickListener(){
            @Override
            public void clicked (InputEvent event, float x, float y) {
                SendCreationRequest(username.getText());
            }
        });

        table.add(userLabel);
        table.add(username);
        CharacterSelect.add(last);
        CharacterSelect.add(next);
        CharacterSelect.add(Gender);
        table.add(CharacterSelect);
        table.row();
        table.add(finish);

    }

    private void SendCreationRequest(String characterName){
        String textureName;
        if (gender)
            textureName = characterNamesFemale[CharacterSelected];
        else
            textureName = characterNamesMale[CharacterSelected];
        PlayerManager.playerData.playerTextureName = textureName;
        PlayerManager.playerData.Name = characterName;

        CreationRequest request = new CreationRequest();
        request.playerData = PlayerManager.playerData;
        _client.sendTCP(request);
    }

    private void ChangeCharacter(){
        if (CharacterSelected >= characterNamesFemale.length)
            CharacterSelected = 0;
        if (gender)
            animation = new Animation(DataManager._textureManager.GenerateAnimationTextures(characterNamesFemale[CharacterSelected]));
        else
            animation = new Animation(DataManager._textureManager.GenerateAnimationTextures(characterNamesMale[CharacterSelected]));
        CharacterSelected++;
    }

    @Override
    public void render(SpriteBatch batch) {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        if (animation != null && animation.GetCurrentTextureRegion() != null){
            stage.getBatch().begin();
            TextureRegion tr = animation.GetCurrentTextureRegion();
            stage.getBatch().draw(tr, CharacterSelect.getX(), CharacterSelect.getY() + 50, 0, 0, tr.getRegionWidth(), tr.getRegionHeight(), 5, 5, 0);
            stage.getBatch().end();
        }
    }

    @Override
    public void update(float delta) {
        animation.Update(delta);

    }

    @Override
    public void dispose() {

    }
}
