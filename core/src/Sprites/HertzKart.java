package Sprites;

import java.util.Random;

import Scenes.Hud;
import Screens.PlayScreen;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.HertzRacing;

public class HertzKart extends Sprite{

	public World world;
	public PlayScreen screen;
	public Body b2body;
	private int KART_WIDTH = 16;
	private int KART_HEIGHT = 32;	

	public float lastX = 3953/HertzRacing.PPM;
	public float lastY = 4000/HertzRacing.PPM;
		
	public Sprite sp;
		

	public HertzKart(World world, PlayScreen screen, String[] userData){
		super(new Texture("CarSprite"+ userData[7] +".png"));
		this.world = world;
		this.screen = screen;
		defineKart();
		
	}
	
	public void defineKart(){
		BodyDef bdef = new BodyDef();
		bdef.position.set(lastX, lastY);
		bdef.type = BodyDef.BodyType.DynamicBody;
//		bdef.type = BodyDef.BodyType.KinematicBody;
		b2body = world.createBody(bdef);
		
		FixtureDef fdef = new FixtureDef();		
		PolygonShape shape2 = new PolygonShape();
		shape2.setAsBox(KART_WIDTH/HertzRacing.PPM, KART_HEIGHT/HertzRacing.PPM);
		
		fdef.shape = shape2;
		fdef.filter.categoryBits = HertzRacing.KART_BIT;
		fdef.filter.maskBits = HertzRacing.ROCK_BIT | HertzRacing.BUMPER_BIT | HertzRacing.COIN_BIT;
		
		b2body.createFixture(fdef).setUserData(this);
		b2body.setLinearDamping(2);
		
		Fixture fixture = b2body.createFixture(fdef);
		fixture.setFilterData(fdef.filter);
		fixture.setUserData(this);

	}

	public void reduceHealth(int reduction){
		if(screen.iFrames >= 1){
			Hud hud = screen.getHUD();
			hud.reduceHealth(reduction);
			Random random = new Random();
			int num = (random.nextInt(3) + 1);
			screen.manager.get("audio/hit" + num + ".wav" , Sound.class).play();
		}		

		screen.iFrames = 0;
	}
}
