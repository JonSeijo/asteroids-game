package com.jashlaviu.asteroids.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Shoot extends GameObject{
	
	private float distanceLimit, distance;

	public Shoot(Ship ship, Texture texture){
		super(texture);
		
		position.set(ship.getCenterX()+8, ship.getCenterY());
			
		updateDirection(ship.getRotationAngle());
		
		sprite.setCenter(position.x, position.y);
		sprite.setRotation(ship.getRotationAngle());
		
		speed = 600;
		distanceLimit = 48;
	}
	
	public void update(float delta, SpriteBatch batch){
		crossScreenUpdate();
		distance++;
		
		position.x += direction.x * speed * delta;
		position.y += direction.y * speed * delta;
		
		sprite.setCenter(position.x, position.y);
		
		sprite.draw(batch);
	}
	
	public boolean isDistanceReach(){
		return distance >= distanceLimit;	
	}
	
	public void dispose(){
		
	}
	
}
