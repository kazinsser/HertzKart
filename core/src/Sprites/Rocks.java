package Sprites;

import Screens.PlayScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.HertzRacing;

public class Rocks extends InteractiveTileObject{
	MapObject rock;
	BodyDef bdef;
	FixtureDef fdef;
	PlayScreen screen;
	
	public Rocks(PlayScreen screen, World world, MapObject object) {		
		super(screen, world, object);
		this.screen = screen;
		fixture.setUserData(this);
		setCategoryFilter(HertzRacing.ROCK_BIT);	
		rock = object;
	}

	public MapObject getObject(){
		return rock;
	}

	@Override
	public void onCollide(HertzKart kart) {
		Gdx.input.vibrate(100);
		setCategoryFilter(HertzRacing.DESTROYED_BIT);
//		getCell().setTile(null);
		screen.blinking = true;
		
		switch(screen.worldOrientation){
		case "NORTH":
			kart.b2body.applyLinearImpulse(new Vector2(0, -12f),kart.b2body.getWorldCenter(), true);
			break;
		case "SOUTH":
			kart.b2body.applyLinearImpulse(new Vector2(0, 12f),kart.b2body.getWorldCenter(), true);
			break;
		case "EAST":
			kart.b2body.applyLinearImpulse(new Vector2(-12f, 0),kart.b2body.getWorldCenter(), true);
			break;
		case "WEST":
			kart.b2body.applyLinearImpulse(new Vector2(12f, 0),kart.b2body.getWorldCenter(), true);
			break;
		default:
			break;
		
		}
	}
	
	public void dispose(){
	}
}
