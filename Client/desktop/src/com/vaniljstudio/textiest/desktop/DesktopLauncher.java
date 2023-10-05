package com.vaniljstudio.textiest.desktop;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.vaniljstudio.textiest.Textiest;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Textiest textiest = new Textiest();
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		Graphics.DisplayMode dm = Lwjgl3ApplicationConfiguration.getDisplayMode();
		Textiest.INIT_HEIGHT = dm.height;
		Textiest.INIT_WIDTH = dm.width;

		config.setWindowedMode(dm.width * 2 / 3, dm.height * 2/ 3);
		new Lwjgl3Application(textiest, config);
	}
}
