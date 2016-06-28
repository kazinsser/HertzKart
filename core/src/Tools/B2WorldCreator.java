package Tools;

import java.util.ArrayList;

import Screens.PlayScreen;
import Sprites.Rocks;
import Sprites.Coins;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.HertzRacing;

public class B2WorldCreator {

	    public B2WorldCreator(PlayScreen screen, ArrayList<Rocks> rocks, ArrayList<Coins> coins){
	        World world = screen.getWorld();
	        TiledMap map = screen.getMap();
	        //create body and fixture variables
	        BodyDef bdef = new BodyDef();
	        PolygonShape shape = new PolygonShape();
	        FixtureDef fdef = new FixtureDef();
	        Body body;

	        
	        System.out.println(map.getLayers().getCount());
	        
	        //create ground bodies/fixtures
	        for(MapObject object : map.getLayers().get("bumpers").getObjects().getByType(RectangleMapObject.class)){
	            Rectangle rect = ((RectangleMapObject) object).getRectangle();

	            bdef.type = BodyDef.BodyType.StaticBody;
	            bdef.position.set((rect.getX() + rect.getWidth() / 2) / HertzRacing.PPM, (rect.getY() + rect.getHeight() / 2) / HertzRacing.PPM);

	            body = world.createBody(bdef);

	            shape.setAsBox(rect.getWidth() / 2 / HertzRacing.PPM, rect.getHeight() / 2 / HertzRacing.PPM);
	            fdef.shape = shape;
	            Fixture fixture = body.createFixture(fdef);
	            
	            Filter filter = new Filter();
	            filter.categoryBits = HertzRacing.BUMPER_BIT;
	            fixture.setFilterData(filter);
	        }

		    for(MapObject object : map.getLayers().get("obstacles").getObjects().getByType(RectangleMapObject.class)){
	           	rocks.add(new Rocks(screen, world, object));
	        }	     
		    
		    for(MapObject object : map.getLayers().get("coins").getObjects().getByType(RectangleMapObject.class)){
	           	coins.add(new Coins(screen, world, object));
	        }	     
    }    
	   
}
