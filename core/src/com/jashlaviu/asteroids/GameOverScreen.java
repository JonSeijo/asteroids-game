package com.jashlaviu.asteroids;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameOverScreen extends ScreenAdapter{

	private float timer;
	private long score;
	
	private Texture gameOverTexture;	
	private AsteroidsGame game;
	private AsteroidsScreen mainScreen;
	private BitmapFont font;
	private SpriteBatch batch;
	
	public GameOverScreen(AsteroidsScreen mainScreen, AsteroidsGame game) {
		this.game = game;	
		this.mainScreen = mainScreen;		
		batch = game.batch;
		
		font = new BitmapFont();
		
		score = mainScreen.getScore();		
		gameOverTexture = mainScreen.getGameOverTexture();
	}
	
	public void render(float delta){		
		timer += delta;	

		if(Gdx.input.isKeyJustPressed(Keys.ENTER))
			game.setScreen(mainScreen);			
			
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();		
		batch.draw(gameOverTexture, 145, 400);
		
		font.setColor(1, 1, 1, 1);	
		font.draw(batch, "SCORE:   \n" + Long.toString(score), 270, 250);
		font.draw(batch, "HI-SCORE:   " + Long.toString(AsteroidsGame.preferences.getLong("hiscore")), 430, 250);
		
		font.draw(batch, "<Press ENTER to restart>", 310, 50);
		
		batch.end();		
	}
	
	public void dispose(){
		gameOverTexture.dispose();
	}
	

}
