package com.jashlaviu.asteroids;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class GameObject {
	
	protected Vector2 position, direction;
	protected Sprite sprite;
	
	protected float speed;
	protected float wWidth, wHeight;
	
	public GameObject(TextureRegion texture){		
		sprite = new Sprite(texture);		
		position = new Vector2();
		direction = new Vector2();
		
		wWidth = Gdx.graphics.getWidth();
		wHeight = Gdx.graphics.getHeight();		
	}
	
	public GameObject(Texture texture){		
		this(new TextureRegion(texture));
	}
	
	public void updateDirection(float rotation){
		direction.x = (float)MathUtils.cosDeg(rotation);
		direction.y = (float)MathUtils.sinDeg(rotation);
		direction.nor();
	}
	
	public void crossScreenUpdate(){
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
	
	public void update(float delta, SpriteBatch batch) {
		
	}
	

}















