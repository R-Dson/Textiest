package Managers.Animation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animation {
    //Properties
    private float _freq;
    private int _textureWidth;
    private final int textureCount;
    private Direction _direction;
    private boolean _directionChanged;
    private boolean _isMoving;

    private final AnimationTextures _animationTexture;
    private TextureRegion _TextureRegion;
    private float _Scale;

    public Animation(AnimationTextures animationTexture){
        _animationTexture = animationTexture;
        setFrequency(0.33f);
        setTextureWidth(16);
        setDirection(Direction.DOWN);
        textureCount = animationTexture.FullBack.getRegionWidth() / _textureWidth;

        _animationTexture.BackSplits = _animationTexture.FullBack.split(_textureWidth, _textureWidth);
        _animationTexture.SideSplits = _animationTexture.FullSide.split(_textureWidth, _textureWidth);
        _animationTexture.FrontSplits = _animationTexture.FullFront.split(_textureWidth, _textureWidth);
        TextureToCurrent(textureCounter, _animationTexture.FrontSplits);
    }

    public Animation(AnimationTextures animationTexture, float frequency){
        _animationTexture = animationTexture;
        setFrequency(frequency);
        setTextureWidth(16);
        setDirection(Direction.DOWN);
        textureCount = animationTexture.FullBack.getRegionWidth() / _textureWidth;

        _animationTexture.BackSplits = _animationTexture.FullBack.split(_textureWidth, _textureWidth);
        _animationTexture.SideSplits = _animationTexture.FullSide.split(_textureWidth, _textureWidth);
        _animationTexture.FrontSplits = _animationTexture.FullFront.split(_textureWidth, _textureWidth);
    }

    public Animation(AnimationTextures animationTexture, float frequency, int textureWidth){
        _animationTexture = animationTexture;
        setFrequency(frequency);
        setTextureWidth(textureWidth);
        setDirection(Direction.DOWN);
        textureCount = animationTexture.FullBack.getRegionWidth() / _textureWidth;

        _animationTexture.BackSplits = _animationTexture.FullBack.split(_textureWidth, _textureWidth);
        _animationTexture.SideSplits = _animationTexture.FullSide.split(_textureWidth, _textureWidth);
        _animationTexture.FrontSplits = _animationTexture.FullFront.split(_textureWidth, _textureWidth);
    }

    private int textureCounter = 0;
    private float timer;
    public void Update(float delta){
        timer += delta;

        if (timer >= _freq){
            //Update
            if (_animationTexture != null)
            {


                if (_directionChanged || textureCounter >= textureCount)
                    textureCounter = 0;

                //flip texture
                if (_directionChanged && _direction == Direction.LEFT)
                    _animationTexture.FullSide.flip(false, false);
                else if (_directionChanged && _direction == Direction.RIGHT)
                    _animationTexture.FullSide.flip(true, false);

                if (!_isMoving)
                    textureCounter = 0;

                if (_directionChanged)
                    _directionChanged = false;

                switch (_direction){
                    case UP:
                        TextureToCurrent(textureCounter, _animationTexture.BackSplits);
                        break;
                    case DOWN:
                        TextureToCurrent(textureCounter, _animationTexture.FrontSplits);
                        break;
                    case LEFT:
                    case RIGHT:
                        TextureToCurrent(textureCounter, _animationTexture.SideSplits);
                        break;
                }
            }

            timer -= _freq;
            textureCounter++;
        }
    }

    private void TextureToCurrent(int count, TextureRegion[][] regions){
        TextureRegion textureRegion = regions[0][count];
        _TextureRegion = textureRegion;
    }

    public void setFrequency(float frequencyInSeconds){
        _freq = frequencyInSeconds;
    }

    public void setTextureWidth(int textureWidth){
        _textureWidth = textureWidth;
    }

    public void setDirection(Direction direction){
        if (direction != _direction)
            _directionChanged = true;
        _direction = direction;
    }

    public TextureRegion GetCurrentTextureRegion(){
        return _TextureRegion;
    }

    public Direction get_direction() {
        return _direction;
    }

    public float get_Scale() {
        return _Scale;
    }

    public void set_Scale(float _Scale) {
        this._Scale = _Scale;
    }

    public boolean is_isMoving() {
        return _isMoving;
    }

    public void set_isMoving(boolean _isMoving) {
        this._isMoving = _isMoving;
    }
}
