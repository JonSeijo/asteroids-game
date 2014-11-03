package com.jashlaviu.asteroids;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Asteroid{
	
	public static final float SIZE_BIG = 2.2f; 
	public static final float SIZE_MEDIUM = 1f; 
	public static final float SIZE_SMALL = 0.6f; 
	
	private float scale, rotation;
	
	private Sprite sprite;
	private Vector2 position, direction;
	private int wWidth, wHeight;
	private float speed, speedNormal;
	
	private TextureRegion singleAsteroidRegion;
	private Rectangle bounds;
	
	public Asteroid(TextureRegion[] asteroidsFrames, float scale, float directionx, float directiony){
		
		sprite = new Sprite(asteroidsFrames[0]);		
		position = new Vector2();
		direction = new Vector2();
		
		wWidth = Gdx.graphics.getWidth();
		wHeight = Gdx.graphics.getHeight();		
		singleAsteroidRegion = asteroidsFrames[0];

		position.x = MathUtils.random(0, 790);
		position.y = MathUtils.random(0, 590);
		
		direction.x = directionx;
		direction.y = directiony;
		direction.nor();
		
		//speed= 210;
		speedNormal = speed = MathUtils.random(150, 200);
		
		bounds = new Rectangle();
		
		this.scale = scale;
		
		sprite.setOrigin(32*scale, 32*scale);	
		sprite.setBounds(position.x, position.y, singleAsteroidRegion.getRegionWidth() * scale, 
				singleAsteroidRegion.getRegionHeight() * scale);
			
		bounds.width = sprite.getBoundingRectangle().width * .80f;
		bounds.height = sprite.getBoundingRectangle().height * .80f;	
		
		rotation = 2;
		sprite.rotate(MathUtils.random(180));
	
	}
	
	public Asteroid(TextureRegion[] asteroidsFrames, float scale){
		this(asteroidsFrames, scale, MathUtils.random(-1f,1f), MathUtils.random(-1f,1f));
	}
	
	public void update(float delta, SpriteBatch batch){			
		position.x += speed * delta * direction.x;
		position.y += speed * delta * direction.y;
		
		updateBounds();
		
		crossScreenUpdate2();	
		
		sprite.setCenter(position.x, position.y);	
		sprite.setOrigin(32*scale, 32*scale);		
		
		sprite.rotate(rotation);		
		sprite.draw(batch);
	}
	
	private void updateBounds(){
		Vector2 center = new Vector2();
		sprite.getBoundingRectangle().getCenter(center);
		bounds.setCenter(center);
	}
	
	public void crossScreenUpdate2(){
		if(sprite.getX() > wWidth)
			position.x = -sprite.getWidth()/2;
		else if(sprite.getX() + sprite.getWidth() < 0)
			position.x = wWidth + sprite.getWidth()/2;
		

		if(sprite.getY() > wHeight)
			position.y = -sprite.getWidth()/2;
		else if(sprite.getY() + sprite.getWidth() < 0)
			position.y = wHeight + (sprite.getHeight())/2;
			
		sprite.setCenter(position.x, position.y);		
	}

	
	public Rectangle getBounds() {
		return bounds;
	}

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}

	public float getScale(){
		return scale;
	}
	
	public Vector2 getDirection(){
		return direction;
	}
	
	public Vector2 getPosition(){
		return position;
	}
	
	public void setPosition(Vector2 newPosition){
		position.set(newPosition);
	}
	
	public void setDirection(Vector2 direction){
		this.direction.x = direction.x;
		this.direction.y = direction.y;
	}
	
	public Rectangle getRect(){
		return sprite.getBoundingRectangle();
	}
	
	public void setNormalSpeed(float speed){
		this.speedNormal = speed;
	}
	public float getSpeed(){
		return speed;
	}
	
}
