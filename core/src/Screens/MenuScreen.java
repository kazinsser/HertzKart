package Screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.HertzRacing;

public class MenuScreen implements Screen {
	AssetManager manager;
	Skin skin;
	Stage stage;
	Sprite sprite;
	SpriteBatch batch;
	OrthographicCamera camera;
	String[] userData;
	Viewport gamePort;

	HertzRacing game;
	public MenuScreen(HertzRacing game, AssetManager manager, String[] text){
		this.game = game;
		this.manager = manager;
		this.userData = text;		
		create();
	}

	public MenuScreen(){
		create();
	}
	public void create(){
		camera = new OrthographicCamera();
//		gamePort = new FitViewport(HertzRacing.V_WIDTH / HertzRacing.PPM, HertzRacing.V_HEIGHT / HertzRacing.PPM, camera);
		batch = new SpriteBatch();
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		skin = new Skin();
		
		//Code for adding sample background commented out for now
		// Background makes buttons not visible
		Texture texture = new Texture("HertzMockup2.png");
		sprite = new Sprite(texture);	

		if(Gdx.app.getType() == ApplicationType.Android)
			sprite.setSize(2, 2);
		else
			sprite.setSize(1,2);
		
	    sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        sprite.setPosition(-sprite.getWidth() / 2, -sprite.getHeight() / 2);
		
		Pixmap pixmap = new Pixmap(100, 50, Format.RGBA8888);
		pixmap.setColor(Color.rgb888(148, 195, 253));
		pixmap.fill();

		skin.add("white", new Texture(pixmap));

		// Store the default libgdx font under the name "default".
		BitmapFont bfont = new BitmapFont();
		
		skin.add("default",bfont);

		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
		textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
		textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
		textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);

		textButtonStyle.font = skin.getFont("default");

		skin.add("default", textButtonStyle);

		final TextButton playButton = new TextButton("Play", skin);
		TextButton marketButton = new TextButton("Market", skin);
		TextButton settingsButton = new TextButton("Settings", skin);
		

		if(Gdx.app.getType() == ApplicationType.Android){
			playButton.setPosition(game.V_WIDTH/2 + 50 - playButton.getWidth(),game.V_HEIGHT/4 + 40);
			marketButton.setPosition(game.V_WIDTH/2 + 50 - marketButton.getWidth(),game.V_HEIGHT/4 - 50);
			settingsButton.setPosition(game.V_WIDTH/2 + 55 - settingsButton.getWidth(),game.V_HEIGHT/4 - 150);
			
		}
		else{
			playButton.setPosition(game.V_WIDTH/2 - playButton.getWidth(),game.V_HEIGHT/4 - 190);
			marketButton.setPosition(game.V_WIDTH/2 - marketButton.getWidth(),game.V_HEIGHT/4 - playButton.getHeight() - 175);
			settingsButton.setPosition(game.V_WIDTH/2 - settingsButton.getWidth(),game.V_HEIGHT/4 - playButton.getHeight() - marketButton.getHeight() - 165);			
		}
		
		
		playButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				playButton.setText("Starting new game");
				game.setScreen( new PlayScreen(game, manager, userData));

			}
		});
		
		marketButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				game.setScreen( new MarketScreen(game, manager,userData));
			}
		});
		
		settingsButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				System.out.println("Settings");
				game.setScreen( new SettingsScreen(game, manager,userData));
			}
		});
		
		stage.addActor(playButton);
		stage.addActor(marketButton);
		stage.addActor(settingsButton);
	}

	public void render (float delta) {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);		
		
		//Draws background see code in method above
		batch.setProjectionMatrix(camera.combined);
	    batch.begin();
	    sprite.draw(batch);
	    batch.end();	    

		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
	}

	@Override
	public void resize (int width, int height) {
	}

	@Override
	public void dispose () {
		stage.dispose();
		skin.dispose();
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}
}