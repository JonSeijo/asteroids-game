package com.jashlaviu.asteroids;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class AsteroidsScreen extends ScreenAdapter{

	private ArrayList<Shoot> shoots;
	private AsteroidsGame game;
	private Ship ship;
	
	private Texture shootTexture;
	
	public AsteroidsScreen(AsteroidsGame game){
		this.game = game;		
		ship = new Ship();
		
		shoots = new ArrayList<Shoot>();		
		shootTexture = new Texture(Gdx.files.internal("shoot.png"));
		
		Gdx.input.setInputProcessor(new InputHandler(this));
	}
	
	public void render(float delta){
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		game.batch.begin();
		
		ship.update(delta, game.batch);
		updateShoots(delta, game.batch);		
		
		game.batch.end();
	}
	
	private void updateShoots(float delta, SpriteBatch batch){
		for(Shoot s : shoots){
			s.update(delta, batch);
		}
	}
	
	public void disparar(){
		shoots.add(new Shoot(ship, shootTexture));
	}
	
	public void dispose(){
		ship.dispose();
	}
	
	public Ship getShip(){
		return ship;
	}
	
}
