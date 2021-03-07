package Managers.Map;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class MapManager {
    private TiledMap _TiledMap;
    private AssetManager _AssetManager;
    public int tileWidth, tileHeight, mapWidthInTiles, mapHeightInTiles, mapWidthInPixels, mapHeightInPixels;

    public void create(String path){
        _AssetManager = new AssetManager();
        _AssetManager.setLoader(TiledMap.class, new TmxMapLoader());
        changeMap(path);
    }

    public void changeMap(String path){
        _AssetManager.load(path, TiledMap.class);
        _AssetManager.finishLoading();
        _TiledMap = _AssetManager.get(path, TiledMap.class);

        MapProperties properties = _TiledMap.getProperties();

        tileHeight        = properties.get("tileheight", Integer.class);
        mapWidthInTiles   = properties.get("width", Integer.class);
        mapHeightInTiles  = properties.get("height", Integer.class);
        mapWidthInPixels  = mapWidthInTiles  * tileWidth;
        mapHeightInPixels = mapHeightInTiles * tileHeight;
    }

    public void render(SpriteBatch batch){

    }

    public void dispose(){
        _AssetManager.dispose();
    }

    public TiledMap get_TiledMap() {
        return _TiledMap;
    }

}
