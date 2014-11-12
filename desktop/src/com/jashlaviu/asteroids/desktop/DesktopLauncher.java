package com.jashlaviu.asteroids.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.jashlaviu.asteroids.AsteroidsGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 800;
		config.height = 600;		
		config.resizable = false;
		config.title = "JAshteroids";
		config.addIcon("data/graphic/asteroid_icon.png", FileType.Internal);
		new LwjglApplication(new AsteroidsGame(), config);
	}
}
