package com.jashlaviu.asteroids;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Gui {
	
	private float shootTimerWidth, shootTimerLen;
	private boolean showFps;
	
	private AsteroidsScreen game;
	private Texture shootTimer, hearth, levelRect;
	private TextureRegion levelBar;
	
	private BitmapFont font;
	
	public Gui(AsteroidsScreen game){
		this.game = game;
		
		shootTimer = new Texture(Gdx.files.internal("data/graphic/gui_shootTimer1.png"));
		hearth = new Texture(Gdx.files.internal("data/graphic/hearth.png"));
		levelRect = new Texture(Gdx.files.internal("data/graphic/levelGui.png"));
		levelBar = new TextureRegion(levelRect, levelRect.getWidth(), 10);		
		
		font = new BitmapFont();
		
		shootTimerLen = shootTimerWidth = shootTimer.getWidth();		
	}
	
	public void update(float delta, SpriteBatch batch){
		updateShootTimer();
		batch.draw(shootTimer, 10, 10, shootTimerLen, 15);
		
		font.draw(batch, "Score: " + Long.toString(game.getScore()), 10, 50);
		font.draw(batch, "HI-Score: " + Long.toString(AsteroidsGame.preferences.getLong("hiscore")), 130, 50);
		
		for(int i = 0; i < game.getLives(); i++){
			batch.draw(hearth, 10 + i * 40, 560);
		}
		
		for(int i = 0; i < game.getLevel(); i++){
			batch.draw(levelBar, 750, 575 - i*20);
		}
		
		if(showFps){
			font.setColor(1, 1, 1, 1);
			font.draw(batch, "FPS: " + Float.toString(Gdx.graphics.getFramesPerSecond()), 10, 540);
		}
	}
	
	public void dispose(){
		shootTimer.dispose();
		hearth.dispose();
		levelRect.dispose();
	}
	
	private void updateShootTimer(){
		float perc = game.getShootTimePerc();
		if(perc < 1f)
			shootTimerLen = shootTimerWidth * perc;			
		else
			shootTimerLen = shootTimerWidth;
				
	}
	
	public void toggleFpsCounter(){
		showFps = !showFps;
	}
	
}
