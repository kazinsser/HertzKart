package Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.HertzRacing;

public class Hud implements Disposable{
	public boolean gameStarted = false;
	
	public Stage stage;
	public Viewport viewport;

	private float score;
	private float distance;
	private float coins;
	public float health;
	
	private boolean gameOver = false;
	
	BitmapFont font = new BitmapFont(Gdx.files.internal("fonts/arialWhite.fnt"));	
	
	Label scoreLabel;
	Label distanceLabel;
	Label coinsLabel;
	Label healthLabel;
	
	Label scoreLabel2;
	Label distanceLabel2;
	Label coinsLabel2;
	Label healthLabel2;
	
	Label countdown;
	
	Table table;
	
	public Hud(SpriteBatch sb){
		font.getData().setScale(.75f);
		score = 0;
		distance = 0;
		coins = 0;
		health = 100;
		
		viewport = new FitViewport(HertzRacing.V_WIDTH, HertzRacing.V_HEIGHT, new OrthographicCamera());
		stage = new Stage(viewport, sb);
		
		table = new Table();
		table.top();
		table.setFillParent(true);
		
		scoreLabel = new Label("Score", new Label.LabelStyle(font, Color.WHITE));
		distanceLabel = new Label("Distance", new Label.LabelStyle(font, Color.WHITE));
		coinsLabel = new Label("Coins", new Label.LabelStyle(font, Color.WHITE));
		healthLabel = new Label("Health", new Label.LabelStyle(font, Color.WHITE));

		scoreLabel2 = new Label(String.format("%6d",(int) score), new Label.LabelStyle(font, Color.WHITE));
		distanceLabel2 = new Label(String.format("%4d",(int) distance), new Label.LabelStyle(font, Color.WHITE));
		coinsLabel2 = new Label(String.format("%3d",(int) coins), new Label.LabelStyle(font, Color.WHITE));
		healthLabel2 = new Label(String.format("%3d",(int) health), new Label.LabelStyle(font, Color.WHITE));
				
		countdown = new Label(String.format("%1d",(int) 3), new Label.LabelStyle(font, Color.WHITE));
		countdown.setVisible(false);
		
		table.add();
		table.add(countdown).padTop(450).uniformX().expandX();
		table.add();
		table.row();		

		table.add(healthLabel).padTop(650).uniformX().expandX();
		table.add(distanceLabel).padTop(650).uniformX().expandX();
//		table.add(scoreLabel).expandX().padTop(10);
		table.add(coinsLabel).padTop(650).uniformX().expandX();
		
		table.row();

		table.add(healthLabel2).uniformX().expandX();
		table.add(distanceLabel2).uniformX().expandX();
//		table.add(scoreLabel2).expandX();
		table.add(coinsLabel2).uniformX().expandX();			
				

		
		Texture texture = new Texture("Dashboard.png");
		Image img = new Image(texture);

		stage.addActor(img);
		stage.addActor(table);
	}
		
	@Override
	public void dispose() {
		stage.dispose();
	}

	public void update(float dt) {				
		if(gameStarted && !gameOver){
			distance += dt;
			distanceLabel2.setText(String.format("%4d",(int) distance * 10));
			score = distance * 10 + coins * 50;
			scoreLabel2.setText(String.format("%6d",(int) score));
		}
	}
	
	public void countdown(int x){
		if(x != 0)
			countdown.setVisible(true);
		else
			countdown.setVisible(false);			
			
		countdown.setText(String.format("%4d",(int) x));		
		
	}

	public void collectCoin(){
		coins += 1;
		coinsLabel2.setText(String.format("%3d",(int) coins));
	}
	
	public void reduceHealth(int reduction){
		if(health - reduction < 0)
			health = 0;
		else if(health - reduction <= 100)
			health -= reduction;
		else
			health = 100;
		healthLabel2.setText(String.format("%3d",(int) health));
		
	}
	
	public void gameOver(boolean bool){
		if(!bool){
			stage.dispose();
			table.reset();
			gameOver = true;
		}
		else{
			table.top();
			table.add(scoreLabel).padTop(400).uniformX().expandX();
			table.row();
			table.add(scoreLabel2).uniformX().expandX();
			table.row();
			table.add(countdown).padTop(400);
			
			stage.addActor(table);
		}
		
		
		
	}
	
	
}
