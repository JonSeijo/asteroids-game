package com.jashlaviu.asteroids;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;

public class Gui {
	
	private float shootTimerWidth, shootTimerLen;
	
	private AsteroidsScreen game;
	private Texture shootTimer;
	
	public Gui(AsteroidsScreen game){
		this.game = game;
		
		shootTimer = new Texture(Gdx.files.internal("gui_shootTimer1.png"));
		shootTimerLen = shootTimerWidth = shootTimer.getWidth();		
	}
	
	public void update(float delta, SpriteBatch batch){
		updateShootTimer();
		batch.draw(shootTimer, 10, 10, shootTimerLen, 15);
	}
	
	public void dispose(){
		shootTimer.dispose();
	}
	
	private void updateShootTimer(){
		float perc = game.getShootTimePerc();
		if(perc < 1f)
			shootTimerLen = shootTimerWidth * perc;			
		else
			shootTimerLen = shootTimerWidth;
				
	}
	
}
