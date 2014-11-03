package com.jashlaviu.asteroids;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.jashlaviu.asteroids.background.Background;
import com.jashlaviu.asteroids.bonus.BonusObject;
import com.jashlaviu.asteroids.gameobjects.Asteroid;
import com.jashlaviu.asteroids.gameobjects.GameObject;
import com.jashlaviu.asteroids.gameobjects.Ship;
import com.jashlaviu.asteroids.gameobjects.Shoot;

public class AsteroidsScreen extends ScreenAdapter{

	private int level, startLevel, startingAsteroids;
	private long lastShootTime, nextShootTime, score;
	private float generalVolume, bonusChance;
		
	private ArrayList<Shoot> shoots;
	private ArrayList<Asteroid> asteroids, asteroidsTemporal;
	private ArrayList<BonusObject> bonusObjects;
	
	private AsteroidsGame game;
	private Ship ship;
	private Gui gui;
	private Background background;
	
	private Texture shipSheet, shootTexture, asteroidsSheet;
	private Texture starBack, singleAsteroidTexture, protectionTexture;
	private TextureAtlas destructionAtlas;
	
	private TextureRegion[] singleAsteroidRegion;
	private TextureRegion bonusRegion;
	private Array<AtlasRegion> destructionRegions;
	
	private OrthographicCamera camera;
	
	private Sound shootSound, explosionSound, dieSound, levelSound, bonusSound;
	
	public AsteroidsScreen(AsteroidsGame game){
		this.game = game;		
		Gdx.input.setInputProcessor(new InputHandler(this));
	
		shootTexture = new Texture(Gdx.files.internal("data/graphic/shoot.png"));		
		shipSheet = new Texture(Gdx.files.internal("data/graphic/shipSheet2.png"));
		protectionTexture = new Texture(Gdx.files.internal("data/graphic/protection.png"));
		singleAsteroidTexture = new Texture(Gdx.files.internal("data/graphic/singleAsteroid.png"));
		starBack = new Texture(Gdx.files.internal("data/graphic/star.png"));
		
		destructionAtlas = new TextureAtlas(Gdx.files.internal("data/graphic/destructionAtlas.atlas"));
		destructionRegions = destructionAtlas.getRegions();
		
		Texture bonusTexture = new Texture(Gdx.files.internal("data/graphic/bonus.png"));		
		bonusRegion = new TextureRegion(bonusTexture);
		
		singleAsteroidRegion = new TextureRegion[1];
		singleAsteroidRegion[0] = new TextureRegion(singleAsteroidTexture);
		
		shootSound = Gdx.audio.newSound(Gdx.files.internal("data/sound/shoot3.wav"));
		explosionSound = Gdx.audio.newSound(Gdx.files.internal("data/sound/explosion1.wav"));
		dieSound = Gdx.audio.newSound(Gdx.files.internal("data/sound/die2.wav"));
		levelSound = Gdx.audio.newSound(Gdx.files.internal("data/sound/levelup.wav"));
		bonusSound = Gdx.audio.newSound(Gdx.files.internal("data/sound/bonus.wav"));
		
		ship = new Ship(this, shipSheet, protectionTexture);		
		shoots = new ArrayList<Shoot>();
		asteroids = new ArrayList<Asteroid>();
		asteroidsTemporal = new ArrayList<Asteroid>();
		bonusObjects = new ArrayList<BonusObject>();
		gui = new Gui(this);		
		background = new Background(starBack);
		
		camera = new OrthographicCamera(800, 600);
		camera.position.set(camera.viewportWidth/2, camera.viewportHeight/2, 0f);
		
		lastShootTime = TimeUtils.millis();
		nextShootTime = 200; //In milliseconds
		
		startLevel = 0;	
		startingAsteroids = 3;		
		
		generalVolume = 0.2f;	
		bonusChance = 0.07f;
		//bonusChance = 0.7f;
	}
	
	public void render(float delta){
		
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		ScreenShaker.update(camera);
		
		game.batch.begin();			

		background.update(delta, game.batch);
		updateAsteroids(delta, game.batch);
		updateShoots(delta, game.batch); //Handles asteroid destruction
		updateBonusObjects(delta, game.batch);
		ship.update(delta, game.batch);
		gui.update(delta, game.batch);		
		
		game.batch.end();	
		
		checkGameOver();
		checkLevelComplete();
	}
	
	private void updateBonusObjects(float delta, SpriteBatch batch){
		Iterator<BonusObject> bonusIterator = bonusObjects.iterator();
		while(bonusIterator.hasNext()){
			BonusObject bonus = bonusIterator.next();
			bonus.update(delta, batch);
			
			if(bonus.isOverlaping(ship)){
				applyBonus(bonus.getType());
				bonusIterator.remove();
			}			
			else if(bonus.isDistanceReached())
				bonusIterator.remove();
			
		}		
	}
	
	public void applyBonus(int bonus){	
		bonusSound.play(generalVolume);		
		if(bonus == BonusObject.BONUS_LIFE)
			ship.addLife();
		else if(bonus == BonusObject.BONUS_SHIELD)
			ship.setShield();
		else if(bonus == BonusObject.BONUS_SLOW_AST){
			for(Asteroid ast : asteroids)
				ast.slowAsteroid();
		}
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
				explosionSound.play(generalVolume+0.1f);
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
				ScreenShaker.shakeScreen(4, new Vector3(camera.position), 10* ast.getScale()/2);
				createAsteroidDivision(ast);
				createRandomBonus();
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
			float speed = asteroid.getSpeed();
			
			 //This excecutes only if asteroid was shoot and if needs to be resized.
			if(scale == Asteroid.SIZE_BIG) scale = Asteroid.SIZE_MEDIUM;  //If it was big, now it'll be medium
			else scale = Asteroid.SIZE_SMALL;							//If it was medium, now it'll be small			

			Vector2 direction1 = new Vector2(direction);
			direction1.rotate(25f);
			
			Vector2 direction2 = new Vector2(direction);
			direction2.rotate(335f);
				
			Asteroid asteroid1 = new Asteroid(singleAsteroidRegion, scale, direction1.x, direction1.y);	
			asteroid1.setPosition(position1);	
			asteroid1.setNormalSpeed(speed);
			asteroidsTemporal.add(asteroid1);
			
			Asteroid asteroid2 = new Asteroid(singleAsteroidRegion, scale, direction2.x, direction2.y);
			asteroid2.setPosition(position2);
			asteroid2.setNormalSpeed(speed);
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
	
	public void createRandomBonus(){
		float r = MathUtils.random(1f);
		if(r < bonusChance){
			bonusObjects.add(new BonusObject(this, MathUtils.random(2)));
		}
	}
		
	public void makeShoot(){
		if((TimeUtils.timeSinceMillis(getLastShootTime())) > nextShootTime){ //If 300 milliseconds passed since last shoot, shoot again
			shoots.add(new Shoot(ship, shootTexture));			
			lastShootTime = TimeUtils.millis();
			shootSound.play(generalVolume);
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
		score += level*100;
		createAsteroids(level);
		ship.addLife();
		ship.restartShip();	
		
		levelSound.play(generalVolume);
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
		bonusSound.dispose();
		ship.dispose();
	}	
	
	public void newGame(){
		resetObjects();
		score = -100;
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
				dieSound.play(generalVolume);
				ScreenShaker.shakeScreen(10, new Vector3(camera.position), 30f);
				ship.destroy();
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
		
	public float getGeneralVolume(){
		return generalVolume;
	}
	
	public TextureRegion getBonusRegion(){
		return bonusRegion;
	}
	
	public Array<AtlasRegion> getDestructionRegions(){
		return destructionRegions;
	}
	
	public void toggleFpsCounter(){
		gui.toggleFpsCounter();
	}

	
}
