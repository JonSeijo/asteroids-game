package com.jashlaviu.asteroids;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class StartScreen implements Screen {
	
	private AsteroidsGame game;
	private SpriteBatch batch;
	
	public StartScreen(AsteroidsGame game){
		this.game = game;
		batch = game.batch;
	}

	private Texture background;
	
	@Override
	public void render(float delta) {
		if(Gdx.input.isKeyJustPressed(Keys.ENTER))
			game.setScreen(new AsteroidsScreen(game));
		
		batch.begin();		
		batch.draw(background, 0, 0);		
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		background = new Texture(Gdx.files.internal("data/graphic/start.png"));

	}

	@Override
	public void hide() {
		background.dispose();

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
