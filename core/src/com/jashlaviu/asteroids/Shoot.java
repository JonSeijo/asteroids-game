package com.jashlaviu.asteroids;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Shoot {
	
	private Vector2 initialPosition;
	private Vector2 position;
	private Vector2 direction;
	private float speed;
	
	private float distanceLimit;
	
	private Sprite sprite;
	
	public Shoot(Ship ship, Texture texture){
		position = new Vector2(ship.getCenterX(), ship.getCenterY());
		direction = new Vector2();
		initialPosition = new Vector2(position);
		
		direction.x = (float)MathUtils.cosDeg(ship.getRotationAngle());
		direction.y = (float)MathUtils.sinDeg(ship.getRotationAngle());
		direction.nor();
		
		speed = 500;
		distanceLimit = 500;
		
		sprite = new Sprite(texture);
		sprite.setCenter(position.x, position.y);
		sprite.setRotation(ship.getRotationAngle());
	}
	
	public void update(float delta, SpriteBatch batch){
		position.x += direction.x * speed * delta;
		position.y += direction.y * speed * delta;
		
		sprite.setCenter(position.x, position.y);
		
		sprite.draw(batch);
	}
	
	public boolean isDistanceReach(){
		return Math.abs(position.len() - initialPosition.len()) > distanceLimit;	
	}
	
	public void dispose(){
		
	}
	
}
