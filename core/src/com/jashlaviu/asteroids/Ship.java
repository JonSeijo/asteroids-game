package com.jashlaviu.asteroids;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
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
	
	private boolean moving, alive;
	private float speed, accel;
	
	private Vector2 velocity;	
	
	private Animation shipAnimation, destructionAnimation;
	private Texture protectionTex;
	private TextureRegion[] shipAnimationFrames;
	private TextureRegion normalShipFrame, currentShipFrame;
	private float shipAnimationTime, stateTime;
	
	private int lives, initialLives;
	private float respawningTime, maxRespawnTime;
	private boolean isRespawning;
	
	private Sound accelSound;	
	
	public Ship(AsteroidsScreen game, TextureRegion[][] shipSheet, Texture protectionTex){
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
		
		destructionAnimation = new Animation(0.1f, game.getDestructionRegions());
				
		accelSound = Gdx.audio.newSound(Gdx.files.internal("data/sound/accel.wav"));
		
		this.protectionTex = protectionTex;
	
		rotationAmount = 4.5f;
		initialLives = 4;
		
		sprite.setOrigin(32, 8);
	}
	
	public Ship(AsteroidsScreen game, Texture shipSheetTexture, Texture protectionTexture){
		this(game, TextureRegion.split(shipSheetTexture, 48, 16), protectionTexture);
	}
	
	public void update(float delta, SpriteBatch batch){
		checkRespawn(delta);
		rotate();
		updateAnimation(delta);
		move(delta);
		
		if(isRespawning && alive)			
			batch.draw(protectionTex, position.x - 24, position.y - 32);	
		
		if(alive)
			sprite.draw(batch);
		else{
			TextureRegion destructionFrame = destructionAnimation.getKeyFrame(stateTime += delta);	
			batch.draw(destructionFrame, sprite.getX() + sprite.getWidth()/2 - 32, 
					sprite.getY() + sprite.getHeight()/2 - 32);
			
			if(destructionAnimation.isAnimationFinished(stateTime))				
				restartShip();				
		}
			
	}
	
	private void checkRespawn(float delta){
		if(isRespawning){
			respawningTime += delta;
			if(respawningTime >= maxRespawnTime){  //seconds of invulnerability
				respawningTime = 0;
				isRespawning = false;
			}
		}
	}
	
	public void move(float delta){
		crossScreenUpdate();
		
		if(moving){
			accelSound.play(0.1f);
			if(accel <= 200) accel += 40;	
			if(speed <= 350) speed += accel * delta;
			
			sprite.setRegion(currentShipFrame);
			updateDirection(sprite.getRotation());
		}			
		else{
			accelSound.stop();
			sprite.setRegion(normalShipFrame);
			
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
	
	
	public void restartShip(){		
		accel = 0;
		speed = 0;		
		position.x = wWidth/2;
		position.y = wHeight/2;
		sprite.setCenter(position.x, position.y);	
		isRespawning = true;
		maxRespawnTime = 2;
		alive = true;
	}
	
	private void updateAnimation(float delta){
		stateTime += delta;
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
	
	public void setShield(float duration){
		isRespawning = true;
		maxRespawnTime = duration;
	}
	
	public void rotate(){				
		sprite.rotate(rotationAmount * rotationSide);
	}
	
	public void destroy(){
		alive = false;
		stateTime = 0;
		lives--;
		isRespawning = true;
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
	
	public int getLives(){
		return lives;
	}
	
	public void newGame(){
		lives = initialLives;
		restartShip();
	}
	
	public void dispose(){
		accelSound.dispose();
	}
	
	public void addLife(){
		lives++;
	}

}
