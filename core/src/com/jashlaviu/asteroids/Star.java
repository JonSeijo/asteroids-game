package com.jashlaviu.asteroids;

import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Star{
	
	private Random randomGen;
	private Texture texture;
	private Vector2 direction, position;	
	private float speed;
	private float scale;
	
	public Star(Texture texture, Vector2 direction, float speed){
		this.texture = texture;
		this.direction = direction;		
		this.speed = speed;
		
		randomGen = new Random();		
		scale = (float) ((randomGen.nextGaussian() + 1f) * 0.6f);
			
		position = new Vector2(MathUtils.random(0, 795), MathUtils.random(0, 595));
	}
	
	public void update(float delta, SpriteBatch batch){
		position.x += speed * delta * direction.x;
		position.y += speed * delta * direction.y;
		
		crossScreenUpdate();

		batch.draw(texture, position.x, position.y, 8*scale, 8*scale);
	}

	private void crossScreenUpdate() {
		if(position.x > 804)
			position.x = -4*scale;
		else if(position.x + 4*scale < 0)
			position.x = 804;		

		if(position.y > 604)
			position.y = -4*scale;
		else if(position.y + 4*scale < 0)
			position.y = 604;		
	}

}
