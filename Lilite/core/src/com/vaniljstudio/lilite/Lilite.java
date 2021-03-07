package com.vaniljstudio.lilite;

import Data.StaticValues;
import Managers.DataManager;
import Managers.Networking.GameClient;
import Managers.Scenes.LoginScene;
import Managers.Scenes.MainScene;
import Managers.Scenes.Scene;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class Lilite extends ApplicationAdapter {
	SpriteBatch batch;
	private float timer60;

	private GameClient _GameClient;
	private DataManager _DataManager;

	public static String uniqueID;
	public static Scene CurrentScene;
	
	@Override
	public void create () {
		//Initiate functions
		_GameClient = new GameClient();

		batch = new SpriteBatch();
		_DataManager = new DataManager();

		//Starts the client
		_GameClient.StartClient();

		CurrentScene = new LoginScene(_GameClient);
	}

	@Override
	public void render () {
		float delta = Gdx.graphics.getDeltaTime();
		timer60 += delta;

		//Update client
		if (CurrentScene instanceof MainScene)
			_GameClient.Update(delta);

		//Updates the scene on a fixed value
		if (timer60 >= StaticValues.updateFrequency){
			if (CurrentScene != null)
				CurrentScene.update(delta);
			timer60 -= StaticValues.updateFrequency;
		}

		//Probably not gonna render here.
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//Begin then send batch to every sub function
		batch.begin();
		//render scene
		if (CurrentScene != null)
			CurrentScene.render(batch);

		//End after every function have rendered
		batch.end();
	}
	
	@Override
	public void dispose () {
		CurrentScene.dispose();
		batch.dispose();
	}
}
