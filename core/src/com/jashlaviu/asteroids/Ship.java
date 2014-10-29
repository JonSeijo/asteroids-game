package com.jashlaviu.asteroids;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Ship extends GameObject{
	
	public static final int LEFT = 1;	
	public static final int RIGHT = -1;
	public static final int NOTHING = 0;
	
	private float wWidth, wHeight;
	private int rotationSide;
	private float rotationAmount;	
	
	private boolean moving;
	private float speed, accel;
	
	private Vector2 velocity;	
	
	private Animation shipAnimation, respawnAnimation;
	private TextureRegion[] shipAnimationFrames, shipRespawnFrames;
	private TextureRegion normalShipFrame, respawnFrameNormal, currentShipFrame;
	private float shipAnimationTime, stateTime;
	
	private int lives;
	private float respawningTime;
	private boolean isRespawning;
	
	
	public Ship(TextureRegion[][] shipSheet){
		super(shipSheet[0][0]);		
		normalShipFrame = shipSheet[0][0];
		
		velocity = new Vector2();
		
		wWidth = Gdx.graphics.getWidth();
		wHeight = Gdx.graphics.getHeight();
		
		// Set up fire animation		
		shipAnimationFrames = new TextureRegion[3];		
	
		for(int i = 1; i < 4; i++){
			shipAnimationFrames[i-1] = shipSheet[i][0];
		}		
		shipAnimationTime = 0.25f;
		shipAnimation = new Animation(shipAnimationTime, shipAnimationFrames);		
		
		restartShip();
		rotationAmount = 4;
		lives = 3;
	}
	
	public Ship(Texture shipSheetTexture){
		this(TextureRegion.split(shipSheetTexture, 48, 16));
	}
	
	public void update(float delta, SpriteBatch batch){
		checkRespawn(delta);
		rotate();
		move(delta);
		updateAnimation(delta);
		
		sprite.draw(batch);	
	}
	
	private void checkRespawn(float delta){
		if(isRespawning){
			respawningTime += delta;
			if(respawningTime >= 2){  //seconds of invulnerability
				respawningTime = 0;
				isRespawning = false;
			}
		}
	}
	
	public void move(float delta){
		crossScreenUpdate();
		
		if(moving){
			if(accel <= 200) accel += 15;	
			if(speed <= 300) speed += accel * delta;
			
			sprite.setRegion(currentShipFrame);
			updateDirection(sprite.getRotation());			
		}			
		else{
			if(isRespawning()) sprite.setRegion(respawnFrameNormal);
			else sprite.setRegion(normalShipFrame);
			
			accel = 0;
			speed *= .98f;
			if(speed <= 0.1) speed = 0;			
		}
			// Gets the facing direction in a vector	
		velocity.x = calculateVelocity(direction.x, delta);
		velocity.y = calculateVelocity(direction.y, delta);
		
		position.add(velocity.x, velocity.y);
		sprite.setCenter(position.x, position.y);
	}
	

	public void lostLive() {
		lives--;
		restartShip();	
	}
	
	public void restartShip(){		
		accel = 0;
		speed = 0;		
		position.x = wWidth/2;
		position.y = wHeight/2;
		sprite.setCenter(position.x, position.y);	
		isRespawning = true;
	}
	
	private void updateAnimation(float delta){
		stateTime += delta;
		if(isRespawning())
			currentShipFrame = respawnAnimation.getKeyFrame(stateTime, true);	
		else
			currentShipFrame = shipAnimation.getKeyFrame(stateTime, true);		
	}
	
	public Rectangle getBounds(){
		Rectangle spriteBounds = sprite.getBoundingRectangle();
		Rectangle bounds = spriteBounds;
		bounds.width = spriteBounds.width - 16;
		bounds.x = spriteBounds.x + 16;
		
		return bounds;
	}
	
	private float calculateVelocity(float direction, float delta){
		return (float)(direction * speed * delta + 0.5 * accel * (delta * delta));
	}
	
	public void rotate(){				
		sprite.rotate(rotationAmount * rotationSide);
	}
	
	public boolean isRespawning(){
		return isRespawning;
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
	
	public void setRespawnAnimationSheet(Texture respAnimationSheet){
		TextureRegion[][] tmp = TextureRegion.split(respAnimationSheet, 48, 16);
		shipRespawnFrames = new TextureRegion[3];		
		
		for(int i = 1; i < 4; i++){
			shipRespawnFrames[i-1] = tmp[i][0];
		}
		
		respawnFrameNormal = tmp[0][0];
		respawnAnimation = new Animation(shipAnimationTime, shipRespawnFrames);		
	}
	
	public int getLives(){
		return lives;
	}
	
	public void newGame(){
		lives = 3;
		restartShip();
	}

}
