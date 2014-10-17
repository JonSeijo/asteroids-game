package com.jashlaviu.asteroids;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Ship {
	
	public static final int LEFT = 1;	
	public static final int RIGHT = -1;
	public static final int NOTHING = 0;
	
	private float wWidth, wHeight;
	private int rotationSide;
	private float rotationAmount;	
	
	private boolean moving;
	private float speed, accel;
	
	private Vector2 position, direction, velocity;	
	private Texture texture;
	private Sprite sprite;
	
	
	
	public Ship(){
		texture = new Texture(Gdx.files.internal("ship.png"));
		
		wWidth = Gdx.graphics.getWidth();
		wHeight = Gdx.graphics.getHeight();
		
		position = new Vector2();
		position.x = wWidth / 2 + texture.getWidth();
		position.y = wHeight / 2 + texture.getHeight();	
		
		direction = new Vector2();
		velocity = new Vector2();
		rotationAmount = 4;
		
		sprite = new Sprite(texture);
		sprite.setCenter(position.x, position.y);
	
	}
	
	public void update(float delta, SpriteBatch batch){	
		rotate();
		move(delta);
		
		sprite.draw(batch);		
	}
	
	public void move(float delta){
		if(moving){
			if(accel <= 200) accel += 20;	
			if(speed <= 300) speed += accel * delta;
			
			direction.x = (float)MathUtils.cosDeg(sprite.getRotation());
			direction.y = (float)MathUtils.sinDeg(sprite.getRotation());
			direction.nor();
		}			
		else{
			accel = 0;
			speed *= .98f;
		}
			// Gets the facing direction in a vector	
		velocity.x = calculateVelocity(direction.x, delta);
		velocity.y = calculateVelocity(direction.y, delta);
		
		position.add(velocity.x, velocity.y);
		sprite.setCenter(position.x, position.y);
	}
	
	private float calculateVelocity(float direction, float delta){
		return (float)(direction * speed * delta + 0.5 * accel * (delta * delta));
	}
	
	public void rotate(){				
		sprite.rotate(rotationAmount * rotationSide);
	}
	
	public void dispose(){
		texture.dispose();
	}
	
	public void setRotationSide(int direction){
		rotationSide = direction;
	}
	
	public void setMoving(boolean moving){
		this.moving = moving;
	}
	
	public float getCenterX(){
		return sprite.getX() + sprite.getWidth()/2;
	}
	
	public float getCenterY(){
		return sprite.getY() + sprite.getHeight()/2;
	}
	
	public float getRotationAngle(){
		return sprite.getRotation();
	}
	
}
