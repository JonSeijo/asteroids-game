package com.jashlaviu.asteroids.background;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Background {
	/**
	 * The background includes the stars and its movement.
	 */
	
	private float speed;
	
	private ArrayList<Star> stars;	
		
	public Background(Texture starTex){			
		stars = new ArrayList<Star>();
		
		speed = 50;
		
		Vector2 starDirection = new Vector2(MathUtils.random(-1f,1f), MathUtils.random(-1f,1f));		
		int starAmount = 100;
		for(int i = 0; i < starAmount; i++){
			stars.add(new Star(starTex, starDirection, speed));
		}
	}
	
	public void update(float delta, SpriteBatch batch){
		Gdx.gl.glClearColor(0/255f, 0/255f, 10/255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);	
		
		for(Star star: stars){
			star.update(delta, batch);
		}
		
	}
	
	
	
}
