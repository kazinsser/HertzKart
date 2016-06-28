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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.sql.Database;
import com.badlogic.gdx.sql.DatabaseFactory;
import com.badlogic.gdx.sql.SQLiteGdxException;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.HertzRacing;

public class MarketScreen implements Screen {
	Skin skin;
	Stage stage;
	Sprite sprite;
	SpriteBatch batch;
	OrthographicCamera camera;
	String[] userData;
	private HertzRacing game;
	private AssetManager manager;
	boolean insufficient = false;
	Label insufficientLabel;
	int count = 0;

	public MarketScreen(HertzRacing game, AssetManager manager, String[] text){		
		this.game = game;
		this.manager = manager;
		this.userData = text;
		for(int i = 0; i< text.length; i++){
			System.out.print("i: " + i + " " + text[i] + ", ");
		}
		System.out.println();
		create();
	}

	public MarketScreen(){
		create();
	}
	public void create(){
		camera = new OrthographicCamera();
		batch = new SpriteBatch();
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		
		//Code for adding sample background commented out for now
		// Background makes buttons not visible
		Texture texture = new Texture("HertzMockup4.png");
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
		
		
		final Database database = DatabaseFactory.getNewDatabase("HertzRacing.db", 1,
				"", null);
		database.setupDatabase();

		try {
			database.openOrCreateDatabase();
		} catch (SQLiteGdxException e) {
			e.printStackTrace();
		}
		
		Table container = new Table();
		container.setFillParent(true);
		
		TextButton backButton = new TextButton("Back",skin);
		container.add(backButton);
		
		Label coins = new Label("Coins: " + userData[1], skin);
		container.add(coins);		
		container.row();
		
		String selection = "";
		if(Integer.parseInt(userData[7]) == 0){
			selection = "default";
		} else {
			selection = "Car " + userData[7];
		}
		Label selectionLabel = new Label("Selection: " + selection, skin);
		container.add(selectionLabel);
	
		
		container.row();
		
		backButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new MenuScreen(game, manager, userData));
			}
		});
		
		if(userData[3].equals("false")){
			Label labelOne = new Label("100 Coins", skin);
			container.add(labelOne);
		} else {
			Label labelOne = new Label("", skin);
			container.add(labelOne);
		}

		if(userData[4].equals("false")){
			Label labelTwo = new Label("200 Coins", skin);
			container.add(labelTwo);
		}
		
		container.row();
		
		Texture textureOne = null;
		String textOne = "";
		if(userData[3].equals("true")){
			textureOne = new Texture("CarButton1.png");
			textOne = "Select";
		} else if(Integer.parseInt(userData[7]) == 1){
			textureOne = new Texture("CarButtonSelected1.png");
			textOne = "Selected";
		} else {
			textureOne = new Texture("CarButtonLocked1.png");
			textOne = "Buy";
		}
		TextureRegion textureRegionOne = new TextureRegion(textureOne);
		ImageButton carButtonOne = new ImageButton( new TextureRegionDrawable(textureRegionOne));
		container.add(carButtonOne);
		
		
		Texture textureTwo = null;
		String textTwo = "";
		if(userData[4].equals("true")){
			textureTwo = new Texture("CarButton2.png");
			textTwo = "Select";
		} else if(Integer.parseInt(userData[7]) == 2){
			textureTwo = new Texture("CarButtonSelected2.png");
			textTwo = "Selected";
		} else{
			textureTwo = new Texture("CarButtonLocked2.png");
			textTwo = "Buy";
		}		
		TextureRegion textureRegionTwo = new TextureRegion(textureTwo);
		ImageButton carButtonTwo = new ImageButton( new TextureRegionDrawable(textureRegionTwo));
		container.add(carButtonTwo);
		
		container.row();
		
		TextButton buttonOne = new TextButton(textOne, skin);
		buttonOne.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				if(userData[3].equals("false")){
				if(Integer.parseInt(userData[1]) < 100){
					insufficient = true;
				} else {
					int coins = Integer.parseInt(userData[1]) - 100;
					try {
						database.execSQL("UPDATE UserData SET Coins='" + coins 
								+ "', UnlockOne='true', carSelection='1' "
								+ "WHERE Username='" + userData[0] + "'" );
						userData[1] = Integer.toString(coins);
						userData[3] = "true";
						userData[7] = Integer.toString(1);
					} catch (SQLiteGdxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					game.setScreen(new MarketScreen(game, manager, userData));
				}
				}  else {
					userData[7] = Integer.toString(1);	
					game.setScreen(new MarketScreen(game, manager, userData));
				}
			}
		});
		container.add(buttonOne);
		TextButton buttonTwo = new TextButton(textTwo, skin);
		buttonTwo.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				if(userData[4].equals("false")){
				if(Integer.parseInt(userData[1]) < 200){
					insufficient = true;
				} else {
					int coins = Integer.parseInt(userData[1]) - 200;
					try {
						database.execSQL("UPDATE UserData SET Coins='" + coins 
								+ "', UnlockTwo='true', carSelection='2' "
								+ "WHERE Username='" + userData[0] + "'" );
						userData[1] = Integer.toString(coins);
						userData[4] = "true";
						userData[7] = Integer.toString(2);
					} catch (SQLiteGdxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					game.setScreen(new MarketScreen(game, manager, userData));
				}
				}  else {
					userData[7] = Integer.toString(2);	
					game.setScreen(new MarketScreen(game, manager, userData));
				}
			}
		});
		container.add(buttonTwo);
		
		container.row();
		
		if(userData[5].equals("false")){
			Label labelThree = new Label("300 Coins", skin);
			container.add(labelThree);
		} else {
			Label labelThree = new Label("", skin);
			container.add(labelThree);
		}
		
		if(userData[6].equals("false")){
			Label labelFour = new Label("500 Coins", skin);
			container.add(labelFour);
		}
		
		container.row();
		
		Texture textureThree = null;
		String textThree = "";
		if(userData[5].equals("true")){
			textureThree = new Texture("CarButton3.png");
			textThree = "Select";
		}else if(Integer.parseInt(userData[7]) == 3){
			textureThree = new Texture("CarButtonSelected3.png");
			textThree = "Selected";
		} else {
			textureThree = new Texture("CarButtonLocked3.png");
			textThree = "Buy";
		}		
		TextureRegion textureRegionThree = new TextureRegion(textureThree);
		ImageButton carButtonThree = new ImageButton( new TextureRegionDrawable(textureRegionThree));
		container.add(carButtonThree);

		Texture textureFour = null;
		String textFour = "";
		if(userData[6].equals("true")){
			textureFour = new Texture("CarButton4.png");
			textFour = "Select";
		}else if(Integer.parseInt(userData[7]) == 4){
			textureFour = new Texture("CarButtonSelected4.png");
			textFour = "Selected";
		} else {
			textureFour = new Texture("CarButtonLocked4.png");
			textFour = "Buy";
		}
		TextureRegion textureRegionFour = new TextureRegion(textureFour);
		ImageButton carButtonFour = new ImageButton( new TextureRegionDrawable(textureRegionFour));
		container.add(carButtonFour);		
		
		container.row();
		
		
		TextButton buttonThree = new TextButton(textThree, skin);
		buttonThree.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				if(userData[5].equals("false")){
					if(Integer.parseInt(userData[1]) < 300){
						insufficient = true;
					} else {
						int coins = Integer.parseInt(userData[1]) - 300;
						try {
							database.execSQL("UPDATE UserData SET Coins='" + coins 
									+ "', UnlockThree='true', carSelection='3' "
									+ "WHERE Username='" + userData[0] + "'" );
							userData[1] = Integer.toString(coins);
							userData[5] = "true";
						} catch (SQLiteGdxException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						game.setScreen(new MarketScreen(game, manager, userData));
					}
				} else {
					userData[7] = Integer.toString(3);
					game.setScreen(new MarketScreen(game, manager, userData));					
				}
			}
		});
		container.add(buttonThree);
		TextButton buttonFour = new TextButton(textFour, skin);
		buttonFour.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				if(userData[6].equals("false")){
				if(Integer.parseInt(userData[1]) < 500){
					insufficient = true;
				} else {
					int coins = Integer.parseInt(userData[1]) - 500;
					try {
						database.execSQL("UPDATE UserData SET Coins='" + coins 
								+ "', UnlockFour='true', carSelection='4' "
								+ "WHERE Username='" + userData[0] + "'" );
						userData[1] = Integer.toString(coins);
						userData[6] = "true";
						userData[7] = Integer.toString(4);
					} catch (SQLiteGdxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					game.setScreen(new MarketScreen(game, manager, userData));
				}
				} else {
					userData[7] = Integer.toString(4);
					game.setScreen(new MarketScreen(game, manager, userData));					
				}
			}
		});
		container.add(buttonFour);
		
		container.row();
		insufficientLabel = new Label("Insufficient coins!", skin);
		insufficientLabel.setVisible(false);
		container.add(insufficientLabel);
				
		stage.addActor(container);
		
	}

	public void render (float delta) {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);		
		
		//Draws background see code in method above
		batch.setProjectionMatrix(camera.combined);
	    batch.begin();
	    sprite.draw(batch);
	    batch.end();	    
	    
	    
	    if(insufficient){
	    	count += 1;
	    	System.out.println(count);
	    	insufficientLabel.setVisible(true);
	    	if(count >= 100){
	    		insufficient = false;
	    	}
	    }else{
	    	count = 0;	    	
	    	insufficientLabel.setVisible(false);
	    }

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