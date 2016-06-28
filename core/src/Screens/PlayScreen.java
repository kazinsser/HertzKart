package Screens;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

import Scenes.Hud;
import Sprites.HertzKart;
import Sprites.Rocks;
import Sprites.Coins;
import Tools.B2WorldCreator;
import Tools.WorldContactListener;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.HertzRacing;

public class PlayScreen implements Screen {

	public boolean test = true;
	float count = 0;
	private boolean reset = false;
	
	private HertzRacing game;

	private OrthographicCamera gamecam;
	private Viewport gamePort;
	private Hud hud;

	private OrthogonalTiledMapRenderer renderer;

	//Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;
    
    // sprites
	Sprite sp;
	ArrayList<Rocks> rocks;
	ArrayList<Coins> coins;
	private HertzKart player;
	private Music music;

	private TextureAtlas atlas;
	
	public AssetManager manager;
	public String worldOrientation = "NORTH";
	
	//Tiled map variables
    private TmxMapLoader maploader;
    private TiledMap map;
    
    private float timer = 1;
    public boolean blinking;
    public float iFrames;
    
    boolean turnLeft = false;

	float alpha;

	// TODO: Streamline the pause/unpause system
	private boolean gameStarted = false;
	private boolean mobile = false;
	

	private String[] userData;

	public enum State {
		Running, Paused
	}

	State state = State.Running;
	public PlayScreen(HertzRacing game, AssetManager manager, String[] userData) {

        rocks = new ArrayList<Rocks>();
        coins = new ArrayList<Coins>();
		
		this.manager = manager;
		this.game = game;

		this.userData = userData;
		gamecam = new OrthographicCamera();
		
		gamePort = new FitViewport(HertzRacing.V_WIDTH / HertzRacing.PPM, HertzRacing.V_HEIGHT / HertzRacing.PPM, gamecam);
				
		hud = new Hud(game.batch);
	
		//Load our map and setup our map renderer
        maploader = new TmxMapLoader();
        map = maploader.load("map1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1  / HertzRacing.PPM);

        //initially set our gamcam to be centered correctly at the start of of map
        gamecam.position.set(123, 250, 0);

        //create our Box2D world, setting no gravity in X, -10 gravity in Y, and allow bodies to sleep
        world = new World(new Vector2(0, 0), true);
        //allows for debug lines of our box2d world.
        b2dr = new Box2DDebugRenderer();

        new B2WorldCreator(this, rocks, coins);

		player = new HertzKart(world, this, userData);

		world.setContactListener(new WorldContactListener(player));		

		if(Gdx.app.getType() == ApplicationType.Android)
			mobile = true;

//		if(manager.containsAsset("audio/mainSong.mp3")){
			music = manager.get("audio/mainSong.mp3");
			music.setLooping(true);
			music.play();
//		}		
	}
	
	@Override
	public void show() {
	}

	public void handleInput(float dt) {
		if ( !gameStarted && Gdx.input.isTouched()) {
			Timer.schedule(new Task() {
				int count = 3;

				@Override
				public void run() {
					hud.countdown(count--);
				}
			}, 0 // (delay)
					, 1 // (interval)
					, 3 // (# of executions)
			);
			Timer.schedule(new Task() {

				@Override
				public void run() {
					gameStarted = true;
					hud.gameStarted = true;
					state = state.Running;
				}
			}, 3 // (delay)
					, 0 // (interval)
					, 1 // (# of executions)
			);
		}

		if (gameStarted == true) {
			// touch controls


			if(Gdx.app.getType() == ApplicationType.Android){
//				 move faster
				if (worldOrientation.equals("NORTH")) {
					player.b2body.applyLinearImpulse(new Vector2(0, 1f),
							player.b2body.getWorldCenter(), true);
				}
				if (worldOrientation.equals("WEST")) {
					player.b2body.applyLinearImpulse(new Vector2(-1f, 0),
							player.b2body.getWorldCenter(), true);
				}
				if (worldOrientation.equals("EAST")) {
						player.b2body.applyLinearImpulse(new Vector2(1f, 0),
								player.b2body.getWorldCenter(), true);

				}
				if (worldOrientation.equals("SOUTH")) {
						player.b2body.applyLinearImpulse(new Vector2(0, -1f),
								player.b2body.getWorldCenter(), true);
				}
				
				
				switch (worldOrientation) {
				case "NORTH":
					// move right
					if (Gdx.input.getRoll() > 10 && player.b2body.getLinearVelocity().x <= 3)
						 player.b2body.applyForceToCenter(6 * Gdx.input.getRoll(), 0, true);

					// move left
					if (Gdx.input.getRoll() < -10 && player.b2body.getLinearVelocity().x >= -3)
						 player.b2body.applyForceToCenter(6 * Gdx.input.getRoll(), 0, true);					
													
					if (Gdx.input.justTouched() && Gdx.input.getX() <= Gdx.graphics.getWidth()/2 ) {
						player.b2body.setTransform(player.b2body.getPosition().x,
								player.b2body.getPosition().y, (float) -90
										* MathUtils.degreesToRadians);
						gamecam.rotate(270);
						worldOrientation = "WEST";
					}
					if (Gdx.input.justTouched() && Gdx.input.getX() > Gdx.graphics.getWidth()/2 ) {
						player.b2body.setTransform(player.b2body.getPosition().x,
								player.b2body.getPosition().y, (float) 90
										* MathUtils.degreesToRadians);
						gamecam.rotate(90);						
						worldOrientation = "EAST";
					}
					break;
				case "EAST":
					// move right
					if (Gdx.input.getRoll() > 10 && player.b2body.getLinearVelocity().y >= -3)
						 player.b2body.applyForceToCenter(0, -6 * Gdx.input.getRoll(), true);

					// move left
					if (Gdx.input.getRoll() < -10 && player.b2body.getLinearVelocity().y <= 3)
						 player.b2body.applyForceToCenter(0, -6 * Gdx.input.getRoll(), true);
					
					if (Gdx.input.justTouched() && Gdx.input.getX() <= Gdx.graphics.getWidth()/2) {
						player.b2body.setTransform(player.b2body.getPosition().x,
								player.b2body.getPosition().y, (float) 0
										* MathUtils.degreesToRadians);
						gamecam.rotate(270);
						worldOrientation = "NORTH";
					}
					if (Gdx.input.justTouched() && Gdx.input.getX() > Gdx.graphics.getWidth()/2) {
						player.b2body.setTransform(player.b2body.getPosition().x,
								player.b2body.getPosition().y, (float) 0
										* MathUtils.degreesToRadians);
						gamecam.rotate(90);
						worldOrientation = "SOUTH";
					}
					break;
				case "SOUTH":
					// move right
					if (Gdx.input.getRoll() > 10 && player.b2body.getLinearVelocity().x >= -3)
						 player.b2body.applyForceToCenter(-6 * Gdx.input.getRoll(), 0, true);

//					// move left
					if (Gdx.input.getRoll() < -10 && player.b2body.getLinearVelocity().x <= 3)
						 player.b2body.applyForceToCenter(-6 * Gdx.input.getRoll(), 0, true);
					
					if (Gdx.input.justTouched() && Gdx.input.getX() <= Gdx.graphics.getWidth()/2) {
						player.b2body.setTransform(player.b2body.getPosition().x,
								player.b2body.getPosition().y, (float) 90
										* MathUtils.degreesToRadians);
						gamecam.rotate(270);
						worldOrientation = "EAST";
					}
					if (Gdx.input.justTouched() && Gdx.input.getX() > Gdx.graphics.getWidth()/2) {
						player.b2body.setTransform(player.b2body.getPosition().x,
								player.b2body.getPosition().y, (float) -90
										* MathUtils.degreesToRadians);
						gamecam.rotate(90);
						worldOrientation = "WEST";
					}
					break;
				case "WEST":
					// move right
					if (Gdx.input.getRoll() > 10 && player.b2body.getLinearVelocity().y <= 3)
						 player.b2body.applyForceToCenter(0, 6 * Gdx.input.getRoll(), true);

					// move left
					if (Gdx.input.getRoll() < -10 && player.b2body.getLinearVelocity().y >= -3)
						 player.b2body.applyForceToCenter(0, 6 * Gdx.input.getRoll(), true);
					
					if (Gdx.input.justTouched() && Gdx.input.getX() <= Gdx.graphics.getWidth()/2) {
						player.b2body.setTransform(player.b2body.getPosition().x,
								player.b2body.getPosition().y, (float) 0
										* MathUtils.degreesToRadians);
						gamecam.rotate(270);
						worldOrientation = "SOUTH";
					}
					if (Gdx.input.justTouched() && Gdx.input.getX() > Gdx.graphics.getWidth()/2) {
						player.b2body.setTransform(player.b2body.getPosition().x,
								player.b2body.getPosition().y, (float) 0
										* MathUtils.degreesToRadians);
						gamecam.rotate(90);
						worldOrientation = "NORTH";
					}
					break;
				default:
					break;

				}
			}else{			
				if (worldOrientation.equals("NORTH")) {
					// move faster
					if (Gdx.input.isKeyPressed(Input.Keys.UP)){
						player.b2body.applyLinearImpulse(new Vector2(0, 1f),
								player.b2body.getWorldCenter(), true);
					}
	
					// move slower
					if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
						player.b2body.applyLinearImpulse(new Vector2(0, -1f),
								player.b2body.getWorldCenter(), true);
	
					// move right
					if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
						player.b2body.applyLinearImpulse(new Vector2(1f, 0),
								player.b2body.getWorldCenter(), true);
	
					// move left
					if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
						player.b2body.applyLinearImpulse(new Vector2(-1f, 0),
								player.b2body.getWorldCenter(), true);
				}
				if (worldOrientation.equals("WEST")) {
					// move faster
					if (Gdx.input.isKeyPressed(Input.Keys.UP))
						player.b2body.applyLinearImpulse(new Vector2(-1f, 0),
								player.b2body.getWorldCenter(), true);
	
					// move slower
					if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
						player.b2body.applyLinearImpulse(new Vector2(1f, 0),
								player.b2body.getWorldCenter(), true);
	
					// move right
					if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
						player.b2body.applyLinearImpulse(new Vector2(0, 1f),
								player.b2body.getWorldCenter(), true);
	
					// move left
					if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
						player.b2body.applyLinearImpulse(new Vector2(0, -1f),
								player.b2body.getWorldCenter(), true);
	
				}
				if (worldOrientation.equals("EAST")) {
					// move faster
					if (Gdx.input.isKeyPressed(Input.Keys.UP))
						player.b2body.applyLinearImpulse(new Vector2(1f, 0),
								player.b2body.getWorldCenter(), true);
	
					// move slower
					if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
						player.b2body.applyLinearImpulse(new Vector2(-1f, 0),
								player.b2body.getWorldCenter(), true);
	
					// move right
					if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
						player.b2body.applyLinearImpulse(new Vector2(0, -1f),
								player.b2body.getWorldCenter(), true);
	
					// move left
					if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
						player.b2body.applyLinearImpulse(new Vector2(0, 1f),
								player.b2body.getWorldCenter(), true);
	
				}
				if (worldOrientation.equals("SOUTH")) {
					// move faster
					if (Gdx.input.isKeyPressed(Input.Keys.UP))
						player.b2body.applyLinearImpulse(new Vector2(0, -1f),
								player.b2body.getWorldCenter(), true);
	
					// move slower
					if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
						player.b2body.applyLinearImpulse(new Vector2(0, 1f),
								player.b2body.getWorldCenter(), true);
	
					// move right
					if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
						player.b2body.applyLinearImpulse(new Vector2(-1f, 0),
								player.b2body.getWorldCenter(), true);
	
					// move left
					if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
						player.b2body.applyLinearImpulse(new Vector2(1f, 0),
								player.b2body.getWorldCenter(), true);
				}
	
				if (Gdx.input.isKeyJustPressed(Input.Keys.SLASH))
					player.b2body.setLinearVelocity(0, 0);
	
				// testing rotation controls
				if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
					player.lastX = player.b2body.getPosition().x;
					player.lastY = player.b2body.getPosition().y;
					switch (worldOrientation) {
					case "NORTH":
						player.b2body.setTransform(player.b2body.getPosition().x,
								player.b2body.getPosition().y, (float) -90
										* MathUtils.degreesToRadians);
						gamecam.rotate(270);
						worldOrientation = "WEST";
						break;
					case "EAST":
						player.b2body.setTransform(player.b2body.getPosition().x,
								player.b2body.getPosition().y, (float) 0
										* MathUtils.degreesToRadians);
						gamecam.rotate(270);
						worldOrientation = "NORTH";
						break;
					case "SOUTH":
						player.b2body.setTransform(player.b2body.getPosition().x,
								player.b2body.getPosition().y, (float) 90
										* MathUtils.degreesToRadians);
						gamecam.rotate(270);
						worldOrientation = "EAST";
						break;
					case "WEST":
						player.b2body.setTransform(player.b2body.getPosition().x,
								player.b2body.getPosition().y, (float) 0
										* MathUtils.degreesToRadians);
						gamecam.rotate(270);
						worldOrientation = "SOUTH";
						break;
					default:
						break;
	
					}
				}
	
				if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
					player.lastX = player.b2body.getPosition().x;
					player.lastY = player.b2body.getPosition().y;
					switch (worldOrientation) {
					case "NORTH":
						player.b2body.setTransform(player.b2body.getPosition().x,
								player.b2body.getPosition().y, (float) 90
										* MathUtils.degreesToRadians);
						gamecam.rotate(90);						
						worldOrientation = "EAST";
						break;
					case "EAST":
						player.b2body.setTransform(player.b2body.getPosition().x,
								player.b2body.getPosition().y, (float) 0
										* MathUtils.degreesToRadians);
						gamecam.rotate(90);
						worldOrientation = "SOUTH";
						break;
					case "SOUTH":
						player.b2body.setTransform(player.b2body.getPosition().x,
								player.b2body.getPosition().y, (float) -90
										* MathUtils.degreesToRadians);
						gamecam.rotate(90);
						worldOrientation = "WEST";
						break;
					case "WEST":
						player.b2body.setTransform(player.b2body.getPosition().x,
								player.b2body.getPosition().y, (float) 0
										* MathUtils.degreesToRadians);
						gamecam.rotate(90);
						worldOrientation = "NORTH";
						break;
					default:
						break;
	
					}
				}
			}
		} else if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
			gameStarted = true;
			hud.gameStarted = true;
			state = state.Running;
		}		
		
	}

	public void update(float dt) {
//		System.out.println("update method called");
		
		handleInput(dt);
		world.step(1 / 60f, 6, 2);
		
		gamecam.position.set(player.b2body.getPosition().x,
				player.b2body.getPosition().y, 0);

		hud.update(dt);

		gamecam.update();

//		renderer.setView(gamecam);
		renderer.setView(gamecam.combined, gamecam.position.x - 20, gamecam.position.y - 20, 1280/32, 1280/32);
	}

	@Override
	public void render(float delta) {
		switch (state) {
		case Running:
			update(delta);
			
			Gdx.gl.glClearColor(0, 0, 0, 1);			
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//			Gdx.gl.glViewport( 0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight() );
			
			gamecam.update();
			
			if(blinking == true)
				timer += delta;

			renderer.render();

			// this draws green outline around objects
			//TODO
//			b2dr.render(world, gamecam.combined);
			
			if(hud.health > 0){
				game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
				game.batch.begin();
	
				sp = player;
	
				sp.setPosition(341, 595);
				
				sp.draw(game.batch);
		        sp.setAlpha(1);
		        
				if (blinking == true && timer >= 1) {
			        timer-= .2;   // If you reset it to 0 you will loose a few milliseconds every 2 seconds.
			        sp.setAlpha(0);
			        if(timer <= .81){
			        	blinking = false;
			        	timer = 1;
			        }
			    }
				
				iFrames += delta;
				
				game.batch.end();
			}else{
				hud.gameOver(false);
				game.batch.begin();

//				Texture texture = new Texture("gameOver.png");
//				Image img = new Image(texture);
//				stage.addActor(img);
				sp = new Sprite(new Texture("gameOver.png"));
				
				boolean gameOver = false;
				
				if(alpha + 0.1f <= 1)
					alpha += 0.1f;
				else{
					alpha = 1;
					gameOver = true;
				}
		        sp.setAlpha(alpha);
		        
				sp.draw(game.batch);
		        				
				game.batch.end();
				
				if(gameOver){
					hud.gameOver(true);
					if(!reset){
						reset = true;
						restartGame();
					}
				}
				
			}


			break;
		case Paused:
			// don't update
			break;
		}

		hud.stage.draw();
	}
	
	public void restartGame(){
		Timer.schedule(new Task() {
			int count = 3;

			@Override
			public void run() {
				hud.countdown(count--);
			}
		}, 0 // (delay)
				, 1 // (interval)
				, 3 // (# of executions)
		);		
		Timer.schedule(new Task() {
		
		@Override
		public void run() {
			Gdx.app.exit();
		}
		}, 3 // (delay)
				, 1 // (interval)
				, 1 // (# of executions)
		);		

	}

	@Override
	public void resize(int width, int height) {
		gamePort.update(width, height);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
		map.dispose();
		world.dispose();
		b2dr.dispose();
		hud.dispose();
		renderer.dispose();
		manager.clear();
		game.dispose();
	}

	public Hud getHUD() {
		return this.hud;
	}

	public World getWorld() {
		return this.world;
	}

	public TiledMap getMap() {
		return map;
	}

}
