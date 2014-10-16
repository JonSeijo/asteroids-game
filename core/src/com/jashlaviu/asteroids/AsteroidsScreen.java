package com.jashlaviu.asteroids;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;

public class AsteroidsScreen extends ScreenAdapter{

	private AsteroidsGame game;
	private Ship ship;
	
	public AsteroidsScreen(AsteroidsGame game){
		this.game = game;		
		ship = new Ship();
		
		Gdx.input.setInputProcessor(new InputHandler(this));
	}
	
	public void render(float delta){
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		game.batch.begin();
		
		ship.update(delta, game.batch);
		
		game.batch.end();
	}
	
	public void dispose(){
		ship.dispose();
	}
	
	public Ship getShip(){
		return ship;
	}
	
}
