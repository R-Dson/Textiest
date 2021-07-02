package com.vaniljstudio.server;

import Components.Entities.MapEntity;
import Data.Credential;
import DataShared.Ability.AbilityManager;
import Managers.EntityManager;
import Managers.FileManager;
import Managers.Items.ItemManager;
import Managers.Map.Map;
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
	public static DataShared.Ability.AbilityManager AbilityManager;
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
		AbilityManager = new AbilityManager();
		AssetManager = new AssetManager();
		EntityManager = new EntityManager();
		MapManager = new MapManager();
		Engine = new Engine();

		SQLManager = new SQLManager(credential);
		
		ItemManager.LoadItems();
		AbilityManager.LoadAbilities();
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
		Gdx.gl.glClearColor(0, 0, 0.5f, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		GameServer.Update(Gdx.graphics.getDeltaTime());

		Engine.update(Gdx.graphics.getDeltaTime());

		EntityUpdateTimer += Gdx.graphics.getDeltaTime();
		if (EntityUpdateTimer > 1/10f){
			//EntityManager.Update(EntityUpdateTimer);
			EntityUpdateTimer -= 1/10f;
		}

		batch.begin();
		//Map map = MapManager.GetMapByName("map");
		ImmutableArray<Entity> entities =  Engine.getEntitiesFor(Family.all(Map.class).get());

		for ( Entity entity: entities) {
			if (entity instanceof MapEntity){
				MapEntity mapEntity = (MapEntity) entity;
				Map map = mapEntity.getComponent(Map.class);
				if (map.MapName.equals("map") && mapEntity.debugRenderer != null && map.world != null && mapEntity.camera != null){
					mapEntity.renderer.setView(mapEntity.camera);
					mapEntity.renderer.render(new int[]{0,1,2,3,4});
					mapEntity.debugRenderer.render(map.world, mapEntity.camera.combined);
				}
				batch.end();
				break;
			}
		}


	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
