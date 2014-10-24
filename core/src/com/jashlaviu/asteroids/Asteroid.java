package com.jashlaviu.asteroids;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Asteroid{
	
	private Animation animation;
	private Vector2 position;
	
	private TextureRegion currentFrame;
	private float animationTime, stateTime;
	
	public Asteroid(TextureRegion[] asteroidsFrames){		
		animationTime = MathUtils.random(.20f, .50f);		
		animation = new Animation(0.25f, asteroidsFrames);
		position = new Vector2(200, 200);	
	}
	
	public void update(float delta, SpriteBatch batch){	
		stateTime += delta;
		
		batch.draw(animation.getKeyFrame(stateTime, true), position.x, position.y);
	}

	
	
}
