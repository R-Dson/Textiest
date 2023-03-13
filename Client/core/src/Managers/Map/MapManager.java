package Managers.Map;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class MapManager {
    private TiledMap _TiledMap;
    public int tileWidth, tileHeight, mapWidthInTiles, mapHeightInTiles, mapWidthInPixels, mapHeightInPixels;

    public void create(String path){
        changeMap(path);
    }

    public void changeMap(String path){

    }

    public void render(SpriteBatch batch){

    }


    public TiledMap get_TiledMap() {
        return _TiledMap;
    }

}
