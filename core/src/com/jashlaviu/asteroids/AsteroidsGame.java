package com.jashlaviu.asteroids;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class AsteroidsGame extends Game {
	public SpriteBatch batch;
	public static Preferences preferences;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		preferences = Gdx.app.getPreferences("jashlaviu.asteroids.preferences");
		this.setScreen(new AsteroidsScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	public void dispose(){
		batch.dispose();
		super.dispose();
	}
}
