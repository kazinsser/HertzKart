package Tools;

//import Items.Coin;
import Scenes.Hud;
import Screens.PlayScreen;
import Sprites.HertzKart;
import Sprites.Rocks;
import Sprites.Coins;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.game.HertzRacing;

public class WorldContactListener implements ContactListener {
	HertzKart kart;
	public WorldContactListener(HertzKart player) {
		this.kart = player;
	}

	@Override
	public void beginContact(Contact contact) {
		Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
        System.out.println(cDef + ": Fixture A - " + fixA.getFilterData().categoryBits + " Fixture B - " + fixB.getFilterData().categoryBits );
        System.out.println("Rocks!" + (HertzRacing.KART_BIT | HertzRacing.ROCK_BIT));
        System.out.println("Coin!" + (HertzRacing.KART_BIT | HertzRacing.COIN_BIT));
        System.out.println("Bumper!" + (HertzRacing.KART_BIT | HertzRacing.BUMPER_BIT));
        switch (cDef){
            case HertzRacing.KART_BIT | HertzRacing.ROCK_BIT:
            	kart.reduceHealth(10);
            	if(fixA.getFilterData().categoryBits == HertzRacing.KART_BIT){
            		((Rocks) fixB.getUserData()).onCollide((HertzKart) fixA.getUserData());
            	}
                else{
            		((Rocks) fixA.getUserData()).onCollide((HertzKart) fixB.getUserData());
                }
                break;
            case HertzRacing.KART_BIT | HertzRacing.COIN_BIT:
            	if(fixA.getFilterData().categoryBits == HertzRacing.KART_BIT){
            		((Coins) fixB.getUserData()).onCollide((HertzKart) fixA.getUserData());
            	}
                else{
                	((Coins) fixA.getUserData()).onCollide((HertzKart) fixB.getUserData());
                }
            	break;
            case HertzRacing.KART_BIT | HertzRacing.BUMPER_BIT:
        		kart.reduceHealth(5);
        		break;
            default:
            	break; 		
                
        }
        
	}

	@Override
	public void endContact(Contact contact) {
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	}

}
