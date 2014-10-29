package com.jashlaviu.asteroids;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Asteroid extends GameObject{
	
	public static final float SIZE_BIG = 2f; 
	public static final float SIZE_MEDIUM = 1f; 
	public static final float SIZE_SMALL = 0.5f; 
	
	private float scale;
	
	private Animation animation;
	
	private TextureRegion currentFrame;
	private float animationTime, stateTime;

	private Rectangle trueBounds, bounds;
	private float boundsDiff;
	
	public Asteroid(TextureRegion[] asteroidsFrames, float scale, float directionx, float directiony){
		super(asteroidsFrames[0]);
//		animationTime = MathUtils.random(.30f, .50f);
		animationTime = .40f;
		animation = new Animation(animationTime, asteroidsFrames);
		currentFrame = animation.getKeyFrame(stateTime, true);
		
		position.x = (MathUtils.random(2) == 0) ? MathUtils.random(0, 300) : MathUtils.random(400, 750);
		position.y = (MathUtils.random(2) == 0) ? MathUtils.random(0, 250) : MathUtils.random(400, 600);
		
		direction.x = directionx;
		direction.y = directiony;
		direction.nor();
		
		speed = 50 / animationTime;
		
		trueBounds = new Rectangle();
		setBounds(new Rectangle());
		
		this.scale = scale;
		
		trueBounds.width = currentFrame.getRegionWidth() * scale;
		trueBounds.height = currentFrame.getRegionHeight() * scale;
		
		getBounds().width = trueBounds.width * .70f;
		getBounds().height = trueBounds.height * .70f;
		
		boundsDiff = (trueBounds.width - getBounds().width)/2;				
	}
	
	public Asteroid(TextureRegion[] asteroidsFrames, float scale){
		this(asteroidsFrames, scale, MathUtils.random(-1f,1f), MathUtils.random(-1f,1f));
	}
	
	public void update(float delta, SpriteBatch batch){			
		position.x += speed * delta * direction.x;
		position.y += speed * delta * direction.y;
		
		updateBounds();

		this.crossScreenUpdate();		
		
		stateTime += delta;
		currentFrame = animation.getKeyFrame(stateTime, true);
		
		batch.draw(currentFrame, trueBounds.x, trueBounds.y, trueBounds.width, trueBounds.height);
	}
	
	private void updateBounds(){
		trueBounds.x = position.x - trueBounds.width / 2;
		trueBounds.y = position.y - trueBounds.height / 2;
		
		getBounds().x = trueBounds.x + boundsDiff;
		getBounds().y = trueBounds.y + boundsDiff;
	}

	public void crossScreenUpdate(){
		if(trueBounds.x > wWidth)
			position.x = -trueBounds.width/2;
		else if(trueBounds.x + trueBounds.width < 0)
			position.x = wWidth + trueBounds.width/2;
		

		if(trueBounds.y > wHeight)
			position.y = -trueBounds.height/2;
		else if(trueBounds.y + trueBounds.height < 0)
			position.y = wHeight + (trueBounds.height)/2;
			
		updateBounds();		
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
	public float getAnimationTime(){
		return animation.getAnimationDuration();
	}
	
	public void setAnimationTime(float time){
		animation.setFrameDuration(time);
	}
	
	public Vector2 getDirection(){
		return direction;
	}
	
	public void setDirection(Vector2 direction){
		this.direction.x = direction.x;
		this.direction.y = direction.y;
	}
	
	
}
