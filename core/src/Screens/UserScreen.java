package Screens;

import java.util.ArrayList;

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
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.sql.Database;
import com.badlogic.gdx.sql.DatabaseCursor;
import com.badlogic.gdx.sql.DatabaseFactory;
import com.badlogic.gdx.sql.SQLiteGdxException;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.HertzRacing;

public class UserScreen implements Screen {
	AssetManager manager;
	Skin skin;
	Stage stage;
	Sprite sprite;
	SpriteBatch batch;
	OrthographicCamera camera;
	Viewport gamePort;

	HertzRacing game;
	Database database;
	private TextButton createButton;
	private Table table;
	private ScrollPane scroll;

	public UserScreen(HertzRacing game, AssetManager manager) {
		create();
		this.game = game;
		this.manager = manager;
	}

	public UserScreen() {
		create();
	}

	public void create() {
		camera = new OrthographicCamera();
		gamePort = new FitViewport(HertzRacing.V_WIDTH / HertzRacing.PPM, HertzRacing.V_HEIGHT / HertzRacing.PPM, camera);
		batch = new SpriteBatch();
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		final Table container = new Table();
		stage.addActor(container);
		container.setHeight(100);
		container.setWidth(200);
		
		skin = new Skin(Gdx.files.internal("uiskin.json"));

		// Code for adding sample background commented out for now
		// Background makes buttons not visible
		Texture texture = new Texture("HertzMockup3.png");
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

		// Configure a TextButtonStyle and name it "default". Skin resources are
		// stored by type, so this doesn't overwrite the font.
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
		textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
		textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
		textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);

		textButtonStyle.font = new BitmapFont();

		skin.add("default", textButtonStyle);

		String sqlCreate = "CREATE TABLE IF NOT EXISTS Userdata "
				+ "('Username' TEXT, 'Coins' INTEGER, "
				+ "'Highscore' INTEGER, 'UnlockOne' TEXT,"
				+ "'UnlockTwo' TEXT, 'UnlockThree' TEXT, "
				+ "'UnlockFour' TEXT, 'carSelection' TEXT,PRIMARY KEY(Username));";

		database = DatabaseFactory.getNewDatabase("HertzRacing.db", 1,
				sqlCreate, null);
		database.setupDatabase();

		try {
			database.openOrCreateDatabase();
			database.execSQL(sqlCreate);
		} catch (SQLiteGdxException e) {
			e.printStackTrace();
		}

		DatabaseCursor cursor = null;

		try {
			cursor = database.rawQuery("SELECT * FROM Userdata");
		} catch (SQLiteGdxException e) {
			e.printStackTrace();
		}
		

		table = new Table();
		scroll = new ScrollPane(table);
		

		try {
			cursor = database.rawQuery("SELECT * FROM Userdata");
		} catch (SQLiteGdxException e) {
			e.printStackTrace();
		}
		
		ArrayList<String[]> users = new ArrayList<String[]>();
		while (cursor.next()) {
			String user[] = new String[8];
			for(int i = 0; i < 8; i++){
				user[i] = cursor.getString(i);
			}
			users.add(user);
		}
		
		for(String[] user : users){
			System.out.println(user[0]);
			table.row();

			TextButton button = new TextButton(user[0], skin);
			table.add(button);
			
			final String[] text = user;
			
			button.addListener(new ClickListener() {
				public void clicked(InputEvent event, float x, float y) {
					System.out.println("click " + x + ", " + y);
					try {
						database.closeDatabase();
					} catch (SQLiteGdxException e) {
						e.printStackTrace();
					}
					database = null;
					game.setScreen(new MenuScreen(game, manager, text));
				}
			});
			TextButton deleteButton = new TextButton("X", skin);
			table.add(deleteButton);
			deleteButton.addListener(new ClickListener() {
				public void clicked(InputEvent event, float x, float y) {
					System.out.println("Need to add SQL to delete user" + text);	
//		    		try {
//						database.execSQL("DELETE FROM 'Userdata' WHERE ('"+ txtUsername.getText() +"', 0, 0, 'false', 'false', 'false', 'false', '0')");
//					} catch (SQLiteGdxException e) {
//						e.printStackTrace();
//					}					
				}
			});
		}
		table.row();
		table.add(createButton);
		
		//table.pad(10).defaults().expandX().space(4);

		
		createButton = new TextButton("Create", skin);
		table.row();
		table.add(createButton);
		createButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("Create Button " + x + ", " + y);
				
				final TextField txtUsername = new TextField("", skin);
			    txtUsername.setMessageText("");
			    txtUsername.setPosition(250, 150);
			    
			    final TextButton addButton = new TextButton("Add", skin); 
			    addButton.setPosition(300,100);
			    
			    container.setVisible(false);
			    stage.addActor(txtUsername);
			    stage.addActor(addButton);
			    
			    addButton.addListener(new ClickListener(){
			    	public void clicked(InputEvent event, float x, float y){
			    		try {
							database.execSQL("INSERT INTO 'Userdata' VALUES ('"+ txtUsername.getText() +"', 9000, 0, 'false', 'false', 'false', 'false', '0')");
						} catch (SQLiteGdxException e) {
							e.printStackTrace();
						}	
			    		game.setScreen(new UserScreen(game, manager));
			    	}
			    });			    
			}
		});

		// scroll settings
		scroll.setFlickScroll(true);
		scroll.setSmoothScrolling(true);
		scroll.setScrollbarsOnTop(true);

		container.add(scroll).expand().fill().colspan(4);
		container.row().space(10).padBottom(10);

		if(Gdx.app.getType() == ApplicationType.Android)
			container.setPosition(250, 250);
		else
			container.setPosition(225, 50);
		
		
//		container.setPosition(camera.position.x, camera.position.y);
		stage.addActor(container);
		
	}

	public void render(float delta) {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
//		camera.update();

		// Draws background see code in method above
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		sprite.draw(batch);
		batch.end();

		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void dispose() {
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