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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
	private boolean isPaused, isShooting;
		
	private ArrayList<Shoot> shoots;
	private ArrayList<Asteroid> asteroids, asteroidsTemporal;
	private ArrayList<BonusObject> bonusObjects;
	
	private AsteroidsGame game;
	private Ship ship;
	private Gui gui;
	private Background background;
	
	private Texture shipSheet, shootTexture, asteroidsSheet, bonusTexture, gameOverTexture;
	private Texture starBack, protectionTexture, pausedTexture;
	private TextureAtlas destructionAtlas, asteroidAtlas;
	
	private TextureRegion bonusRegion;
	private Array<AtlasRegion> destructionRegions;
	
	private OrthographicCamera camera;
	
	private Sound shootSound, explosionSound, dieSound, levelSound, bonusSound;
	
	private ShapeRenderer shapeR;
	
	public AsteroidsScreen(AsteroidsGame game){
		this.game = game;		
		Gdx.input.setInputProcessor(new InputHandler(this));
	
		shootTexture = new Texture(Gdx.files.internal("data/graphic/shoot.png"));		
		shipSheet = new Texture(Gdx.files.internal("data/graphic/shipSheet2.png"));
		protectionTexture = new Texture(Gdx.files.internal("data/graphic/protection.png"));
		starBack = new Texture(Gdx.files.internal("data/graphic/star.png"));
		pausedTexture = new Texture(Gdx.files.internal("data/graphic/paused.png"));		
		gameOverTexture = new Texture(Gdx.files.internal("data/graphic/gameOver.png"));
		
		destructionAtlas = new TextureAtlas(Gdx.files.internal("data/graphic/destructionAtlas.atlas"));
		destructionRegions = destructionAtlas.getRegions();
		asteroidAtlas = new TextureAtlas(Gdx.files.internal("data/graphic/asteroidsAtlas.atlas"));
		
		bonusTexture = new Texture(Gdx.files.internal("data/graphic/bonus.png"));		
		bonusRegion = new TextureRegion(bonusTexture);
				
		shootSound = Gdx.audio.newSound(Gdx.files.internal("data/sound/laser5.wav"));
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
		nextShootTime = 300; //In milliseconds
		
		startLevel = 0;	
		startingAsteroids = 4;		
		
		generalVolume = 0.3f;	
		bonusChance = 0.04f;
	//	bonusChance = 1f;
		
		shapeR = new ShapeRenderer();
	}
	
	public void render(float delta){		
		if(!isPaused){			
			camera.update();
			game.batch.setProjectionMatrix(camera.combined);
			ScreenShaker.update(camera);
			
			game.batch.begin();			
	
			background.update(delta, game.batch);   // Move and render the stars in background
			updateAsteroids(delta, game.batch);		// Move and render asteroids
			updateShoots(delta, game.batch); 		// Move and render shoos. Handles asteroid destruction
			updateBonusObjects(delta, game.batch);	// Move and render bonusObjects. Handles bonus aplication.
			ship.update(delta, game.batch);			// Move and render ship. Handles shield, lives, and stuff.
			gui.update(delta, game.batch);			// Render hearts, score and gui stuff.
			
			game.batch.end();	
			
			checkGameOver();			// Handles asteroid collision with ship. Checks if gameover (lives <= 0)
			checkLevelComplete();		// Checks if all asteroids are destroyed, and pass to next level.
		
		}else{  					// If it is paused
			game.batch.begin();			
			game.batch.draw(pausedTexture, 250, 200);			
			game.batch.end();			
		}
	}
	
	private void updateBonusObjects(float delta, SpriteBatch batch){
		/**
		 * Moves, draws and checks collision with ship.
		 */
		Iterator<BonusObject> bonusIterator = bonusObjects.iterator();
		while(bonusIterator.hasNext()){
			BonusObject bonus = bonusIterator.next();
			bonus.update(delta, batch);   // Moves and draws
			
			if(bonus.isOverlaping(ship)){      // If ship grabs it
				applyBonus(bonus.getType());   // Aplly the bonus
				bonusIterator.remove();
			}			
			else if(bonus.isDistanceReached()) // Remove if they travel for long distance
				bonusIterator.remove();
			
		}		
	}
	
	public void applyBonus(int bonus){	
		/**
		 * Apllies bonuses depending on the bonus type 
		 */
		bonusSound.play(generalVolume);		  
		score += 200;
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
		/**
		 * Moves, draws and checks collision with asteroids.
		 * HANDLES ASTEROID DESTRUCTION AND DIVISION
		 */
		if(isShooting)
			makeShoot();
		
		Iterator<Shoot> iter = shoots.iterator();
		while(iter.hasNext()){	
			Shoot shoot = iter.next();
			shoot.update(delta, batch);  // Moves and draws
			
			if(asteroidCollision(shoot)){  	// If collisions with asteroid
				destroyAsteroid(shoot);				// Destruct and divide asteroid
				explosionSound.play(generalVolume+0.1f);
				score += 10;
				iter.remove();
			}
			else if(shoot.isDistanceReach()){	// Remove if shoot travels a long distance
				iter.remove();
			}
		}
	}
	
	public void destroyAsteroid(Shoot shoot){
		/**
		 * Asteroid destruction and asteroid division 
		 * if there is collision with shoot.
		 * Probability of creating a random bonus.
		 */
		Iterator<Asteroid> asterIter = asteroids.iterator();
		while(asterIter.hasNext()){
			Asteroid ast = asterIter.next();
			if(ast.getBounds().overlaps(shoot.getBounds())){   // If shoot collision with asteroid
				ScreenShaker.shakeScreen(4, new Vector3(camera.position), 11f* ast.getScale()/2);  // Shake screen in relation to asteroid size
				createAsteroidDivision(ast);     // Creates two smaller asteroids on its place.
				createRandomBonus();			 // Probability of creating a random bonus.
				asterIter.remove();
				break;
			}
		}
		
		for(Asteroid astTemp : asteroidsTemporal){   //Add to main array the asteroids recently created
			asteroids.add(astTemp);
		}
		asteroidsTemporal.clear();
	}
	
	private void createAsteroidDivision(Asteroid asteroid) {	
		/**
		 * Create two smaller asteroids in the place of the father
		 * Does not create new ones if the father is a small one.
		 */
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
			direction1.rotate(25f);   						// Direction won't be the same of the father
			
			Vector2 direction2 = new Vector2(direction);
			direction2.rotate(335f);
				
			Asteroid asteroid1 = new Asteroid(this, scale, direction1.x, direction1.y, position1.x, position1.y);	
			asteroid1.setNormalSpeed(speed);  			// Create new asteroid and adjust its values			
			asteroidsTemporal.add(asteroid1);
			
			Asteroid asteroid2 = new Asteroid(this, scale, direction2.x, direction2.y, position2.x, position2.y);	
			asteroid2.setNormalSpeed(speed);			// Create new asteroid and adjust its values
			asteroidsTemporal.add(asteroid2);
		}	
	}

	public boolean asteroidCollision(GameObject obj){
		/**
		 * If asteroid collisions with an object, return true
		 */
		Iterator<Asteroid> iter = asteroids.iterator();
		while(iter.hasNext()){		// Asteroid rectangle  collides with   object rectangle
			if(iter.next().getBounds().overlaps(obj.getBounds())){
				return true;
			}
		}		
		return false;
	}
	
	public void createRandomBonus(){
		/**
		 * Probability of creating a bonus (bonusChance)
		 * If bonus is created, its type will be random.
		 */
		float r = MathUtils.random(1f);
		if(r < bonusChance){
			bonusObjects.add(new BonusObject(this, MathUtils.random(2)));
		}
	}
		
	public void makeShoot(){
		/**
		 * Creates a new shoot object, in realtion to ship position and direction.
		 */
		if(!isPaused){
			if((TimeUtils.timeSinceMillis(getLastShootTime())) > nextShootTime){ //If 300 milliseconds passed since last shoot, shoot again
				shoots.add(new Shoot(ship, shootTexture));			
				lastShootTime = TimeUtils.millis();
				shootSound.play(generalVolume);
			}
		}
	}
	
	private void updateAsteroids(float delta, SpriteBatch batch) {
		/**
		 * Moves and draws asteroids.
		 */
		for(Asteroid ast : asteroids){
			ast.update(delta, batch);
		}		
	}
	
	
	public void createAsteroids(int amount){
		/**
		 * Create the passed amount of big asteroids, and adds the to the array. 
		 */
		for(int i = 0; i < amount + startingAsteroids; i++){
			asteroids.add(new Asteroid(this, Asteroid.SIZE_BIG));
		}
	}
	
	public void nextLevel(){
		/**
		 * Change level in +1.
		 * Updates all necesary conditions.
		 */
		level++;
		score += level*100;
		shoots.clear();
		createAsteroids(level);
		ship.addLife();
		ship.restartShip();			
		levelSound.play(generalVolume);
	}
	
	public void gameOver(){
		/**
		 * Updates hi-score
		 * Change screen to gameOver screen.
		 */
		if(score >= getHiScore()) setHiScore(score);
		game.setScreen(new GameOverScreen(this, game));
	}
	
	public long getHiScore(){
		return AsteroidsGame.preferences.getLong("hiscore");
	}
	
	public void setHiScore(long score){
		AsteroidsGame.preferences.putLong("hiscore", score);
		AsteroidsGame.preferences.flush();
	}

	
	public void dispose(){
		/**
		 * Makes all necesary disposes.
		 */
		shipSheet.dispose();
		protectionTexture.dispose();
		shootTexture.dispose();
		asteroidsSheet.dispose();
		gui.dispose();		
		starBack.dispose();	
		bonusTexture.dispose();
		pausedTexture.dispose();
		gameOverTexture.dispose();
				
		shootSound.dispose();
		dieSound.dispose();
		levelSound.dispose();
		explosionSound.dispose();
		bonusSound.dispose();
		ship.dispose();
		
		shapeR.dispose();		
		
	}	
	
	public void newGame(){
		/**
		 * Create a default new game, from level 1
		 */
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
		/**
		 * Level is complete if there are no more asteroids to destroy.
		 * If so, pass to nextLevel.
		 */
		if(asteroids.isEmpty()){
			nextLevel();
		}
	}
	
	private void checkGameOver(){
		/**
		 * Check collision between asteroids and ship.
		 * If collision, destroy the ship and decrease lives
		 * Then, if lives <= 0, gameover.
		 */
		if(asteroidCollision(ship)){
			if(!ship.isRespawning()){     // Only if the ship has not the shield
				dieSound.play(generalVolume);
				ship.destroy();				// Restarts the ship and substract a life.
				if(ship.getLives() <= 0)  // If there are no more lives, then game over.
					gameOver();
				
				else ScreenShaker.shakeScreen(12, new Vector3(camera.position), 40f);  // Shake screen only if didn't lose
				
			}
		}				
	}
	
	@SuppressWarnings("unused")
	private void drawBounds(){
		/**
		 * Draws all collision bounds for debugging purproses.
		 */
		shapeR.begin(ShapeRenderer.ShapeType.Line);
		
		shapeR.setColor(1, 0, 0, 1);
		shapeR.rect(ship.getBounds().getX(), ship.getBounds().getY(), ship.getBounds().getWidth(), ship.getBounds().getHeight());
		
		shapeR.setColor(0, 1, 0, 1);
		for(BonusObject bonus : bonusObjects)
			shapeR.rect(bonus.getBounds().getX(), bonus.getBounds().getY(), bonus.getBounds().getWidth(), bonus.getBounds().getHeight());	
		
		shapeR.setColor(0, 0, 1, 1);
		for(Asteroid ast : asteroids)
			shapeR.rect(ast.getBounds().getX(), ast.getBounds().getY(), ast.getBounds().getWidth(), ast.getBounds().getHeight());	
		
		shapeR.end();
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
	
	public void togglePause(){
		isPaused = !isPaused;
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
	
	public Texture getGameOverTexture(){
		return gameOverTexture;
	}
	
	public void toggleFpsCounter(){
		gui.toggleFpsCounter();
	}
	
	public TextureRegion getAsteroidRegion(String name, int index){
		return asteroidAtlas.findRegion(name, index);
	}
	
	public void setShooting(boolean isShooting){
		this.isShooting = isShooting;
	}

	
}
