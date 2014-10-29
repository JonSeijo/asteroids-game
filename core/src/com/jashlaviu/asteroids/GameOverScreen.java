package com.jashlaviu.asteroids;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameOverScreen extends ScreenAdapter{

	private float timer;
	
	private Texture gameOverTexture;	
	private AsteroidsGame game;
	private AsteroidsScreen mainScreen;
	private SpriteBatch batch;
	
	public GameOverScreen(AsteroidsScreen mainScreen, AsteroidsGame game) {
		System.out.println("Game over");		
		this.game = game;	
		this.mainScreen = mainScreen;
		batch = game.batch;
		
		gameOverTexture = new Texture(Gdx.files.internal("gameOver.png"));
	}
	
	public void render(float delta){		
		timer += delta;
		
		if(timer > 2)
			if(Gdx.input.isKeyJustPressed(Keys.ANY_KEY)){
				game.setScreen(mainScreen);			
			}
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();		
		batch.draw(gameOverTexture, 70, 300);		
		batch.end();		
	}
	
	public void dispose(){
		gameOverTexture.dispose();
	}
	

}
