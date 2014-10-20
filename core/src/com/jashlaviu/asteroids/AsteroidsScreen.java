package com.jashlaviu.asteroids;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class AsteroidsScreen extends ScreenAdapter{

	private ArrayList<Shoot> shoots;
	private AsteroidsGame game;
	private Ship ship;
	
	private Texture shipTexture, shootTexture;
	
	public AsteroidsScreen(AsteroidsGame game){
		this.game = game;		
	
		shootTexture = new Texture(Gdx.files.internal("shoot.png"));
		shipTexture = new Texture(Gdx.files.internal("ship.png"));
		
		ship = new Ship(shipTexture);		
		shoots = new ArrayList<Shoot>();	
		
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
		
		for(Shoot sh : shoots){
			sh.update(delta, batch);
		}
		
		Iterator<Shoot> iter = shoots.iterator();
		while(iter.hasNext()){			
			if(iter.next().isDistanceReach()){
				iter.remove();
			}
		}
		

	}
	
	public void disparar(){
		shoots.add(new Shoot(ship, shootTexture));
	}
	
	public void dispose(){
		shipTexture.dispose();
		shootTexture.dispose();
	}
	
	public Ship getShip(){
		return ship;
	}
	
}
