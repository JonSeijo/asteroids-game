package com.jashlaviu.asteroids;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class Asteroid extends GameObject{
	
	private Animation animation;
	
	private TextureRegion currentFrame;
	private float animationTime, stateTime;
	
	public Asteroid(TextureRegion[] asteroidsFrames){
		super(asteroidsFrames[0]);
		animationTime = MathUtils.random(.30f, .60f);		
		animation = new Animation(animationTime, asteroidsFrames);
		
		position.x = (MathUtils.random(2) == 0) ? MathUtils.random(0, 250) : MathUtils.random(550, 750);
		position.y = (MathUtils.random(2) == 0) ? MathUtils.random(0, 200) : MathUtils.random(400, 600);
		
		direction.x = MathUtils.random(-1,1);
		direction.y = MathUtils.random(-1,1);
		
		speed = animationTime * 150;
	}
	
	public void update(float delta, SpriteBatch batch){	

		position.x += speed * delta * direction.x;
		position.y += speed * delta * direction.y;
		crossScreenUpdate();			
		
		stateTime += delta;
		currentFrame = animation.getKeyFrame(stateTime, true);	
		
		batch.draw(currentFrame, position.x, position.y);
	}

	
	
}
