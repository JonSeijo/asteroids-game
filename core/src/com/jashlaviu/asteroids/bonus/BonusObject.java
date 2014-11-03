package com.jashlaviu.asteroids.bonus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.jashlaviu.asteroids.AsteroidsScreen;
import com.jashlaviu.asteroids.gameobjects.Ship;

public class BonusObject {
	
	public static int BONUS_LIFE = 0;
	public static int BONUS_SHIELD = 1;
	public static int BONUS_SLOW_AST = 2;
	
	public static float SHIELD_DURATION = 6f;
	public static float SLOW_AST_DURATION = 6f;
	
	private int type;
	
	private Texture texture;
	private  Vector2 position, direction;
	private  Rectangle bounds;
	private float width, height, wWidth, wHeight;
	private  float distance, maxTravelDistance, speed;	
	
	public BonusObject(AsteroidsScreen game, int type){
		this.type = type;			
		texture = game.getBonusTexture(type);
				
		width = texture.getWidth();
		height = texture.getHeight();
		
		wWidth = Gdx.graphics.getWidth();
		wHeight = Gdx.graphics.getHeight();		
		
		position = new Vector2(-width, -height);
		direction = new Vector2(MathUtils.random(-1f, 1f), MathUtils.random(-1f, 1f));
		
		bounds = new Rectangle(position.x + width/2, position.y + height/2, width, height);
		
		speed = 200;
		maxTravelDistance = 500;
	}
	
	public void update(float delta, SpriteBatch batch){
		distance++;
		
		position.x += speed * delta *  direction.x;
		position.y += speed * delta *  direction.y;	
				
		updateBounds();
		crossScreenUpdate();
		
		batch.draw(texture, position.x, position.y);		
	}
	
	public void crossScreenUpdate(){
		if(bounds.x > wWidth)
			position.x = -width/2;
		else if(bounds.x + width < 0)
			position.x = wWidth + width/2;		

		if(bounds.y > wHeight)
			position.y = -height/2;
		else if(bounds.y + height < 0)
			position.y = wHeight + height/2;
		
	}
	
	public void updateBounds(){
		bounds.setCenter(position.x, position.y);
	}
	
	public boolean isDistanceReached(){
		return distance >= maxTravelDistance;
	}
	
	public int getType(){
		return type;
	}
	
	public boolean isOverlaping(Ship ship){
		return bounds.overlaps(ship.getBounds());
	}
	
	
	

}
