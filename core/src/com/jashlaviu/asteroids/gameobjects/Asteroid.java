package com.jashlaviu.asteroids.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.jashlaviu.asteroids.AsteroidsScreen;
import com.jashlaviu.asteroids.bonus.BonusObject;

public class Asteroid{
	
	public static final float SIZE_BIG = 2.2f; 
	public static final float SIZE_MEDIUM = 1f; 
	public static final float SIZE_SMALL = 0.6f; 
	
	private float scale, rotation, rotationNormal;
	private float slowCount;
	private boolean slowed;
	
	private Sprite sprite;
	private Vector2 position, direction;
	private int wWidth, wHeight;
	private float speed, speedNormal;
	
	private TextureRegion singleAsteroidRegion;
	private Rectangle bounds;
		
	public Asteroid(AsteroidsScreen game, float scale, float directionx, float directiony, float positionx, float positiony){
		
		singleAsteroidRegion = game.getAsteroidRegion("asteroid", MathUtils.random(1, 3));
		
		sprite = new Sprite(singleAsteroidRegion);		
		position = new Vector2();
		direction = new Vector2();
		
		wWidth = Gdx.graphics.getWidth();
		wHeight = Gdx.graphics.getHeight();		


		position.x = positionx;
		position.y = positiony;
		
		direction.x = directionx;
		direction.y = directiony;
		direction.nor();
		
		speedNormal = speed = MathUtils.random(100 + 11*game.getLevel(), 150 + 11*game.getLevel());
		
		bounds = new Rectangle();
		
		this.scale = scale;
		
		sprite.setOrigin(32*scale, 32*scale);	
		sprite.setBounds(position.x, position.y, singleAsteroidRegion.getRegionWidth() * scale, 
				singleAsteroidRegion.getRegionHeight() * scale);
			
		bounds.width = sprite.getBoundingRectangle().width * .80f;
		bounds.height = sprite.getBoundingRectangle().height * .80f;	
		
		rotationNormal = rotation = 2f;
		sprite.rotate(MathUtils.random(180));
	
	}
	
	public Asteroid(AsteroidsScreen game, float scale){
		this(game, scale, MathUtils.random(-1f,1f), MathUtils.random(-1f,1f),
				MathUtils.random(0, 790),  MathUtils.random(0, 590));
	}
	
	public void update(float delta, SpriteBatch batch){			
		position.x += speed * delta * direction.x;
		position.y += speed * delta * direction.y;
		
		updateBounds();		
		crossScreenUpdate2();
		
		if(slowed) if((slowCount += delta) >= BonusObject.SLOW_AST_DURATION) normalizeAsteroid();		
		
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

	public void slowAsteroid(){
		slowed = true;
		slowCount = 0;
		speed *= .3f;
		rotation *= .5f;
	}
	
	public void normalizeAsteroid(){
		slowed = false;
		speed = speedNormal;
		rotation = rotationNormal;
	}
	
	private void crossScreenUpdate2(){
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
