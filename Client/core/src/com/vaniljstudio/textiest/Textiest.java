package com.vaniljstudio.textiest;

import Data.StaticValues;
import Managers.DataManager;
import Managers.Managers;
import Managers.Networking.GameClient;
import Managers.Networking.NetworkManager;
import Managers.Scenes.ConnectionScene;
import Managers.Scenes.MainScene;
import Managers.Scenes.Scene;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class Textiest extends ApplicationAdapter {
	SpriteBatch batch;
	private float timer60;

	public static String uniqueConnectID;
	public static Scene CurrentScene;
	public static float INIT_WIDTH, INIT_HEIGHT;

	public static Managers managers;
	
	@Override
	public void create () {
		//Initiate functions
		batch = new SpriteBatch();
		managers = new Managers();


		CurrentScene = new ConnectionScene(managers);
	}

	@Override
	public void render () {
		float delta = Gdx.graphics.getDeltaTime();
		timer60 += delta;

		//Update client
		if (CurrentScene instanceof MainScene)
			managers.getNetworkManager().Update(delta);

		//Updates the scene on a fixed value
		while (timer60 >= StaticValues.updateFrequency){
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
		try {

		}
		catch (IllegalStateException | NullPointerException e)
		{

		}

		//End after every function have rendered
		batch.end();
	}
	
	@Override
	public void dispose () {
		CurrentScene.dispose();
		batch.dispose();
	}

	@Override
	public void resize (int width, int height) {
		CurrentScene.resize(width, height);
	}
}
