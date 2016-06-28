package Sprites;

import Scenes.Hud;
import Screens.PlayScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.HertzRacing;

public class Coins extends InteractiveTileObject{
	MapObject coin;
	BodyDef bdef;
	FixtureDef fdef;
	PlayScreen screen;
	
	public Coins(PlayScreen screen, World world, MapObject object) {		
		super(screen, world, object);
		this.screen = screen;
		fixture.setUserData(this);
		setCategoryFilter(HertzRacing.COIN_BIT);	
		coin = object;
	}

	public MapObject getObject(){
		return coin;
	}

	@Override
	public void onCollide(HertzKart kart) {
		Gdx.input.vibrate(20);
		setCategoryFilter(HertzRacing.DESTROYED_BIT);
		screen.manager.get("audio/coin.wav", Sound.class).play();
		Hud hud = screen.getHUD();
		hud.collectCoin();
		if(getCell() != null)
			getCell().setTile(null);
		
	}
	
	public void dispose(){
	}
}
