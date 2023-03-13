package Managers;

import Data.StaticValues;
import Managers.Animation.AnimationTextures;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class TextureManager {
    public TextureAtlas CharacterTextureFullFront, CharacterTextureFullSide, CharacterTextureFullBack;

    public TextureManager(){

    }

    public void LoadTextures(){
        CharacterTextureFullFront = new TextureAtlas(Gdx.files.internal(StaticValues.FullFront));
        CharacterTextureFullBack = new TextureAtlas(Gdx.files.internal(StaticValues.FullBack));
        CharacterTextureFullSide = new TextureAtlas(Gdx.files.internal(StaticValues.FullSide));

    }

    public AnimationTextures GenerateAnimationTextures(String textureName){
        TextureAtlas.AtlasRegion ff = CharacterTextureFullFront.findRegion(textureName);
        TextureAtlas.AtlasRegion fs = CharacterTextureFullSide.findRegion(textureName);
        TextureAtlas.AtlasRegion fb = CharacterTextureFullBack.findRegion(textureName);

        AnimationTextures at = new AnimationTextures();
        at.FullBack = fb;
        at.FullFront = ff;
        at.FullSide = fs;
        return at;
    }

    public void Dispose(){
        CharacterTextureFullFront.dispose();
        CharacterTextureFullBack.dispose();
        CharacterTextureFullSide.dispose();
    }
}
