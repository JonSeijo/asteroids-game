package com.jashlaviu.asteroids;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class AsteroidsGame extends Game {
	public SpriteBatch batch;
	public static Preferences preferences;
	public static Music backMusic;
	public static int mMute, sMute;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		preferences = Gdx.app.getPreferences("jashlaviu.asteroids.preferences");			
		
		backMusic = Gdx.audio.newMusic(Gdx.files.internal("data/sound/SpaceFighterLoop_modified.wav"));
		
		mMute = 1;
		sMute = 1;
		
		backMusic.setLooping(true);
		backMusic.setVolume(0.3f);
		
		this.setScreen(new StartScreen(this));
	}
	
	public static void toggleMusicMute(){
		mMute = (mMute == 0 ? 1 : 0);
	}
	
	public static void toggleSoundMute(){
		sMute = (sMute == 0 ? 1 : 0);
	}
	

	@Override
	public void render () {
		super.render();
	}
	
	public void dispose(){
		batch.dispose();
		backMusic.dispose();
		super.dispose();
	}
}
