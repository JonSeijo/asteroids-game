package com.jashlaviu.asteroids;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Gui {
	
	private float shootTimerLen;
	private Texture shootTimer;
	
	public Gui(){
		shootTimer = new Texture(Gdx.files.internal("gui_shootTimer1.png"));
		shootTimerLen = 150;
	}
	
	public void update(float delta, SpriteBatch batch){
		updateShootTimer();
		batch.draw(shootTimer, 10, 10, shootTimerLen, 15);
	}
	
	public void dispose(){
		
	}
	
	private void updateShootTimer(){
		
	}
	
}
