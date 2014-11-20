package com.jashlaviu.asteroids;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.jashlaviu.asteroids.gameobjects.Ship;

public class InputHandler implements InputProcessor{
	
	private AsteroidsScreen game;
	private boolean isShiftPressed;
	
	public InputHandler(AsteroidsScreen game){
		this.game = game;		
	}

	@Override
	public boolean keyDown(int keycode) {
		Ship ship = game.getShip();
		
		if (keycode == Keys.CONTROL_LEFT || keycode == Keys.CONTROL_RIGHT){
			if(game.getLives() > 0)
				game.setShooting(true);	
		}
			
		if(keycode == Keys.W || keycode == Keys.UP)
			ship.setMoving(true);
		
		if(keycode == Keys.A || keycode == Keys.LEFT)
			ship.setRotationSide(Ship.LEFT);
		
		if(keycode == Keys.D || keycode == Keys.RIGHT)
			ship.setRotationSide(Ship.RIGHT);	
		
		if(keycode == Keys.SHIFT_LEFT || keycode == Keys.SHIFT_RIGHT)
			isShiftPressed = true;
		
		if(keycode == Keys.R && isShiftPressed)
			game.setHiScore(0);
		
		if(keycode == Keys.ESCAPE)
			game.togglePause();
		
		if(keycode == Keys.TAB)
			game.toggleFpsCounter();
		
		
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		Ship ship = game.getShip();
		
		if(keycode == Keys.W || keycode == Keys.UP)
			ship.setMoving(false);
				
		if(keycode == Keys.A || keycode == Keys.LEFT)
			ship.setRotationSide(Ship.NOTHING);	
		
		if(keycode == Keys.D  || keycode == Keys.RIGHT)
			ship.setRotationSide(Ship.NOTHING);
		
		if(keycode == Keys.M){
			AsteroidsGame.toggleMusicMute();
			AsteroidsGame.backMusic.setVolume(0.3f * AsteroidsGame.mMute);
		}
		
		if(keycode == Keys.N){
			AsteroidsGame.toggleSoundMute();
		}
		
		if(keycode == Keys.SHIFT_LEFT || keycode == Keys.SHIFT_RIGHT)
			isShiftPressed = false;
		
		if (keycode == Keys.CONTROL_LEFT || keycode == Keys.CONTROL_RIGHT){
			game.setShooting(false);	
		}
		
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
