package com.jashlaviu.asteroids;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Shoot extends GameObject{
	
	private Vector2 initialPosition;	
	private float distanceLimit, distance;

	public Shoot(Ship ship, Texture texture){
		super(texture);
		
		position.set(ship.getCenterX(), ship.getCenterY());
		initialPosition = new Vector2(position);
		
		updateDirection(ship.getRotationAngle());
		
		sprite.setCenter(position.x, position.y);
		sprite.setRotation(ship.getRotationAngle());
		
		speed = 500;
		distanceLimit = 80;
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
