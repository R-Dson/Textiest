package com.vaniljstudio.server;

import Managers.ManagerController;
import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ServerClass extends ApplicationAdapter {
	private SpriteBatch batch;
	private static ManagerController controller;
	private static Engine Engine;
	private boolean running;

	public ServerClass()
	{

		controller = new ManagerController();
		Engine = new Engine();

		Engine.addSystem(controller.getMapManager());

		running = true;
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		controller.setupManagers();

		// Create all threads
		controller.getThreadManager().InitAllThreads();
		
	}

	@Override
	public void render () {
		float delta = Gdx.graphics.getDeltaTime();

		Gdx.gl.glClearColor(0, 0, 0.5f, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


		Engine.update(delta);

		batch.begin();

		batch.end();
	}
	
	@Override
	public void dispose () {
		running = false;
		batch.dispose();
	}

	public boolean isRunning()
	{
		return running;
	}

	public static com.badlogic.ashley.core.Engine getEngine() {
		return Engine;
	}

	public static ManagerController getController() {
		return controller;
	}
}
