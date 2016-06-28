package Sprites;

import Screens.PlayScreen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.HertzRacing;

public class Bumpers {
    protected World world;
    public Body body;
    protected PlayScreen screen;
    protected Fixture fixture;

    public Bumpers(PlayScreen screen, World world, float x, float y, int width, int height){
        this.screen = screen;
        this.world = world;
        
        Rectangle rect = new Rectangle(x, y, width, height);
        
        BodyDef bdef = new BodyDef();
		bdef.position.set((rect.getX() + rect.getWidth() / 2)/HertzRacing.PPM, (rect.getY() + rect.getHeight() / 2)/HertzRacing.PPM);
		bdef.type = BodyDef.BodyType.StaticBody;
		body = world.createBody(bdef);
		
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(rect.getWidth()/2/HertzRacing.PPM, rect.getHeight()/2/HertzRacing.PPM);
				
		fdef.shape = shape;
		fixture = body.createFixture(fdef);        	
    }

    public void setCategoryFilter(short filterBit){
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }
    
}
