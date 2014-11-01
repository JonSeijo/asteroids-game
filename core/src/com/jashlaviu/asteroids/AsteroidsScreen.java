package com.jashlaviu.asteroids;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

public class AsteroidsScreen extends ScreenAdapter{

	private int level, startLevel, startingAsteroids;
	private long lastShootTime, nextShootTime, score;
		
	private ArrayList<Shoot> shoots;
	private ArrayList<Asteroid> asteroids, asteroidsTemporal;
	private AsteroidsGame game;
	private Ship ship;
	private Gui gui;
	private Background background;
	
	private Texture shipSheet, shootTexture, asteroidsSheet;
	private Texture starBack, singleAsteroidTexture, protectionTexture;
	private TextureRegion[] singleAsteroidRegion;
	
	private Sound shootSound, explosionSound, dieSound, levelSound;
	
	public AsteroidsScreen(AsteroidsGame game){
		this.game = game;		
	
		shootTexture = new Texture(Gdx.files.internal("data/graphic/shoot.png"));		
		shipSheet = new Texture(Gdx.files.internal("data/graphic/shipSheet.png"));
		protectionTexture = new Texture(Gdx.files.internal("data/graphic/protection.png"));
		singleAsteroidTexture = new Texture(Gdx.files.internal("data/graphic/singleAsteroid.png"));
		starBack = new Texture(Gdx.files.internal("data/graphic/star.png"));
		
		singleAsteroidRegion = new TextureRegion[1];
		singleAsteroidRegion[0] = new TextureRegion(singleAsteroidTexture);
		
		shootSound = Gdx.audio.newSound(Gdx.files.internal("data/sound/shoot3.wav"));
		explosionSound = Gdx.audio.newSound(Gdx.files.internal("data/sound/explosion1.wav"));
		dieSound = Gdx.audio.newSound(Gdx.files.internal("data/sound/die2.wav"));
		levelSound = Gdx.audio.newSound(Gdx.files.internal("data/sound/levelup.wav"));	
		
		ship = new Ship(shipSheet, protectionTexture);		
		shoots = new ArrayList<Shoot>();
		asteroids = new ArrayList<Asteroid>();
		asteroidsTemporal = new ArrayList<Asteroid>();
		gui = new Gui(this);
		
		background = new Background(starBack);

		lastShootTime = TimeUtils.millis();
		nextShootTime = 200; //In milliseconds
		
		Gdx.input.setInputProcessor(new InputHandler(this));
		
		startLevel = 0;	
		startingAsteroids = 3;
		
		score = -100;
	}
	
	public void render(float delta){
		
		game.batch.begin();		
		
		background.update(delta, game.batch);
		updateAsteroids(delta, game.batch);
		updateShoots(delta, game.batch); //Handles asteroid destruction
		ship.update(delta, game.batch);
		gui.update(delta, game.batch);		
		
		game.batch.end();	
		
		checkGameOver();
		checkLevelComplete();
	}
	

	private void updateShoots(float delta, SpriteBatch batch){		
		for(Shoot sh : shoots){
			sh.update(delta, batch);
		}
		
		Iterator<Shoot> iter = shoots.iterator();
		while(iter.hasNext()){	
			Shoot shoot = iter.next();
			if(asteroidCollision(shoot)){
				destroyAsteroid(shoot);
				explosionSound.play();
				score += 10;
				iter.remove();
			}
			else if(shoot.isDistanceReach()){
				iter.remove();
			}
		}
	}
	
	public void destroyAsteroid(GameObject object){
		Iterator<Asteroid> asterIter = asteroids.iterator();
		while(asterIter.hasNext()){
			Asteroid ast = asterIter.next();
			if(ast.getBounds().overlaps(object.getBounds())){
				createAsteroidDivision(ast);
				asterIter.remove();
				break;
			}
		}
		
		for(Asteroid astTemp : asteroidsTemporal){
			asteroids.add(astTemp);
		}
		asteroidsTemporal.clear();
	}
	
	private void createAsteroidDivision(Asteroid asteroid) {		
		float scale = asteroid.getScale();		
		
		//Only create new asteroids if are NOT small.
		if(scale != Asteroid.SIZE_SMALL){
			Vector2 position1 = new Vector2(asteroid.getPosition());
			Vector2 position2 = new Vector2(asteroid.getPosition());
			
			Vector2 direction = asteroid.getDirection();
			
			 //This excecutes only if asteroid was shoot and if needs to be resized.
			if(scale == Asteroid.SIZE_BIG) scale = Asteroid.SIZE_MEDIUM;  //If it was big, now it'll be medium
			else scale = Asteroid.SIZE_SMALL;							//If it was medium, now it'll be small			

			Vector2 direction1 = new Vector2(direction);
			direction1.rotate(25f);
			
			Vector2 direction2 = new Vector2(direction);
			direction2.rotate(335f);
				
			Asteroid asteroid1 = new Asteroid(singleAsteroidRegion, scale, direction1.x, direction1.y);	
			asteroid1.setPosition(position1);		
			asteroidsTemporal.add(asteroid1);
			
			Asteroid asteroid2 = new Asteroid(singleAsteroidRegion, scale, direction2.x, direction2.y);
			asteroid2.setPosition(position2);		
			asteroidsTemporal.add(asteroid2);
		}	
	}

	public boolean asteroidCollision(GameObject obj){		
		Iterator<Asteroid> iter = asteroids.iterator();
		while(iter.hasNext()){		// Asteroid rectangle  collides with   object rectangle
			if(iter.next().getBounds().overlaps(obj.getBounds())){
				return true;
			}
		}		
		return false;
	}
		
	public void disparar(){
		if((TimeUtils.timeSinceMillis(getLastShootTime())) > nextShootTime){ //If 300 milliseconds passed since last shoot, shoot again
			shoots.add(new Shoot(ship, shootTexture));			
			lastShootTime = TimeUtils.millis();
			shootSound.play();
		}
	}
	
	private void updateAsteroids(float delta, SpriteBatch batch) {
		for(Asteroid ast : asteroids){
			ast.update(delta, batch);
		}		
	}
	
	
	public void createAsteroids(int amount){
		for(int i = 0; i < amount + startingAsteroids; i++){
			asteroids.add(new Asteroid(singleAsteroidRegion, Asteroid.SIZE_BIG));
		}
	}

	
	public void nextLevel(){
		level++;
		score += 100;
		createAsteroids(level);
		ship.addLife();
		ship.restartShip();	
		
		levelSound.play();
	}
	
	public void gameOver(){
		game.setScreen(new GameOverScreen(this, game));
	}

	
	public void dispose(){
		shipSheet.dispose();
		protectionTexture.dispose();
		shootTexture.dispose();
		asteroidsSheet.dispose();
		gui.dispose();		
		starBack.dispose();
				
		shootSound.dispose();
		dieSound.dispose();
		levelSound.dispose();
		explosionSound.dispose();
		ship.dispose();
	}	
	
	public void newGame(){
		System.out.println("Starting new game\n\n");
		resetObjects();
		
		level = startLevel;
		nextLevel();
	}
	
	private void resetObjects(){
		asteroids.clear();
		shoots.clear();	
		ship.newGame();
	}
	
	private void checkLevelComplete(){
		if(asteroids.isEmpty()){
			nextLevel();
		}
	}
	
	private void checkGameOver(){
		if(asteroidCollision(ship)){
			if(!ship.isRespawning()){
				dieSound.play();
				ship.lostLive();
				if(ship.getLives() <= 0){
					gameOver();
				}
			}
		}				
	}
	
	public void show(){
		newGame();
	}
	
	public float getShootTimePerc(){
		return (float)TimeUtils.timeSinceMillis(lastShootTime) / nextShootTime;		
	}
	
	public Ship getShip(){
		return ship;
	}

	public long getLastShootTime() {
		return lastShootTime;
	}
	
	public float getNextShootTime(){
		return nextShootTime;
	}
	
	public SpriteBatch getBatch(){
		return game.batch;
	}
	
	public int getLives(){
		return ship.getLives();
	}
	
	public int getLevel(){
		return level;
	}
	
	public long getScore(){
		return score;
	}
		
}
