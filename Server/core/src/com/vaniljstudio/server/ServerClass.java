package com.vaniljstudio.server;

import Components.Entities.MapEntity;
import Data.Credential;
import DataShared.Ability.ActionManager;
import Managers.EntityManager;
import Managers.FileManager;
import Managers.Items.ItemManager;
import Managers.Map.Map;
import Managers.Map.MapLayer;
import Managers.Map.MapManager;
import Managers.Network.GameServer;
import Managers.SQLManager;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ServerClass extends ApplicationAdapter {
	SpriteBatch batch;

	public static GameServer GameServer;
	public static SQLManager SQLManager;
	public static ItemManager ItemManager;
	public static ActionManager ActionManager;
	public static AssetManager AssetManager;
	public static EntityManager EntityManager;
	public static MapManager MapManager;
	public static Engine Engine;

	//private
	private float EntityUpdateTimer = 0;

	@Override
	public void create () {
		batch = new SpriteBatch();

		//Loading database credentials
		Credential credential = FileManager.LoadCredentialFile();

		//Initiating managers
		ItemManager = new ItemManager();
		ActionManager = new ActionManager();
		AssetManager = new AssetManager();
		EntityManager = new EntityManager();
		MapManager = new MapManager();
		Engine = new Engine();

		SQLManager = new SQLManager(credential);

		//setups
		ItemManager.LoadItems();
		//ActionManager.LoadActions();
		MapManager.LoadMaps();
		Engine.addSystem(MapManager);

		//Start server
		CreateServer();
	}
	private void CreateServer(){
		GameServer = new GameServer();
		GameServer.startServer();
	}

	@Override
	public void render () {
		float delta = Gdx.graphics.getDeltaTime();
		Gdx.gl.glClearColor(0, 0, 0.5f, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		GameServer.Update(delta);

		Engine.update(delta);

		/*EntityUpdateTimer += delta;
		if (EntityUpdateTimer > 1/10f){
			//EntityManager.Update(EntityUpdateTimer);
			EntityUpdateTimer -= 1/10f;
		}

		for (MapEntity mapEntity : MapManager.mapEntities) {
			//Map map = mapEntity.getComponent(Map.class);
			/*for (MapLayer mapLayer : map.getMapLayers()) {
				mapLayer.render(delta, mapEntity);
			}
		}*/
		batch.begin();

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
