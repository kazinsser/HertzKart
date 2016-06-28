package com.mygdx.game;

import Screens.MenuScreen;
import Screens.PlayScreen;
import Screens.UserScreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class HertzRacing extends Game {
	public static final int V_WIDTH = 720;
	public static final int V_HEIGHT = 1280;
	public static final float PPM = 32;
	public static final short KART_BIT = 1;
	public static final short ROCK_BIT = 2;
	public static final short BUMPER_BIT = 4;
	public static final short COIN_BIT = 8;
	public static final short DESTROYED_BIT = 16;
	
	
	public SpriteBatch batch;
	public AssetManager manager;
	
	public HertzRacing() {
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		
		manager = new AssetManager();
		manager.load("audio/mainSong.mp3", Music.class);
		manager.load("audio/coin.wav", Sound.class);
		manager.load("audio/hit1.wav", Sound.class);
		manager.load("audio/hit2.wav", Sound.class);
		manager.load("audio/hit3.wav", Sound.class);
		
		manager.finishLoading();
		setScreen(new UserScreen(this, manager));
	}

	@Override
	public void render () {
		super.render();
	}
}
