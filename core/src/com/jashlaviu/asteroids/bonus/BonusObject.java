package com.jashlaviu.asteroids.bonus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class BonusObject {
	
	protected Texture texture;
	protected Vector2 position, direction;
	protected Rectangle bounds;
	protected float width, height, wWidth, wHeight;
	protected float distance, maxTravelDistance, speed;	
	
	public BonusObject(Texture texture){
		this.texture = texture;		
		
		width = texture.getWidth();
		height = texture.getHeight();
		
		wWidth = Gdx.graphics.getWidth();
		wHeight = Gdx.graphics.getHeight();		
		
		position = new Vector2(-width, -height);
		direction = new Vector2(MathUtils.random(-1f, 1f), MathUtils.random(-1f, 1f));
		
		bounds = new Rectangle(position.x + width/2, position.y + height/2, width, height);
		
		speed = 100;
	}
	
	public void update(float delta, SpriteBatch batch){
		position.x += speed * delta *  direction.x;
		position.y += speed * delta *  direction.y;	
		
		batch.draw(texture, position.x, position.y);		
	}
	
	
	
	

}
