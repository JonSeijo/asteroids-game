package com.jashlaviu.asteroids;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class AsteroidsGame extends Game {
	public SpriteBatch batch;
	
	@Override
	public void create () {
		batch = new SpriteBatch();		
		this.setScreen(new AsteroidsScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
}
