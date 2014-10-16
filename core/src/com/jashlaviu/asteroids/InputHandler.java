package com.jashlaviu.asteroids;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;

public class InputHandler implements InputProcessor{
	
	private AsteroidsScreen game;
	
	public InputHandler(AsteroidsScreen game){
		this.game = game;		
	}

	@Override
	public boolean keyDown(int keycode) {
		Ship ship = game.getShip();
		
		if(keycode == Keys.UP){
			ship.setMoving(true);
		}
		
		if(keycode == Keys.RIGHT){	
			ship.setRotationSide(Ship.RIGHT);			
		}
		
		if(keycode == Keys.LEFT){
			ship.setRotationSide(Ship.LEFT);			
		}
		
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		Ship ship = game.getShip();
		
		if(keycode == Keys.UP){
			ship.setMoving(false);
		}
		
		if(keycode == Keys.RIGHT)
			ship.setRotationSide(Ship.NOTHING);	
		
		else if(keycode == Keys.LEFT)
			ship.setRotationSide(Ship.NOTHING);	
		
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
