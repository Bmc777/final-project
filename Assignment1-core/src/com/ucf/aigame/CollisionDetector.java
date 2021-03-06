package com.ucf.aigame;

import java.util.ArrayList;

/**
 * Created by Bryan on 2/8/2016.
 */
public class CollisionDetector
{
	private static final int TILE_DIMENSIONS = 16;
	
    private PlayerEntity playerEntity;
    private ArrayList<WallObject> wallList;
    private ArrayList<GameEntity> entityList;
	private ArrayList<Treasure> treasureList;

    CollisionDetector ( GameWorld gameWorld )
    {
    	playerEntity = gameWorld.getPlayerEntity();
    	wallList = gameWorld.getWallList();
    	entityList = gameWorld.getEntityList();
		treasureList = gameWorld.getTreasureArrayList();
    }
    
    public void update( GameWorld gameWorld )
    {
    	entityList = gameWorld.getEntityList();
    	playerEntity = gameWorld.getPlayerEntity();
		treasureList = gameWorld.getTreasureArrayList();
    	
    	GameEntity collidingEntity;
    	ArrayList<WallObject> collidingWalls;
		Treasure collidingTreasure;
    	
    	boolean collisionLeft = false;
    	boolean collisionRight = false;
    	boolean collisionUp = false;
    	boolean collisionDown = false;
    	
    	// Checking player entity for collisions
    	collidingEntity = entityCollisionCheck( playerEntity.getBoundingBox() );
		if ( collidingEntity != null )
		{
			// Debug:
			// System.out.println( "Player is colliding with Entity #" + entityList.indexOf( collidingEntity ) + " in list!");
			playerEntity.takeDamage(1);
		}
		
    	collisionLeft = false;
    	collisionRight = false;
    	collisionUp = false;
    	collisionDown = false;
		
		collidingWalls = wallCollisionCheck( playerEntity.getBoundingBox() );
    	if ( !collidingWalls.isEmpty() )
    	{
    		// Debug:
    		// System.out.println("Player is colliding with Wall #" + wallList.indexOf( collidingWall ) + " in list!");
    		
    		for ( int i = 0; i < collidingWalls.size(); i++ )
    		{
	    		if ( leftCollision( playerEntity.getBoundingBox(), collidingWalls.get(i).getBoundingBox() ) )
	    		{
	    			collisionLeft = true;
	    		}
	    		
	    		if ( rightCollision( playerEntity.getBoundingBox(), collidingWalls.get(i).getBoundingBox() ) )
	    		{
	    			collisionRight = true;
	    		}
    		
	    		if ( topCollision( playerEntity.getBoundingBox(), collidingWalls.get(i).getBoundingBox() ) )
	    		{
	    			collisionUp = true;
	    		}
	    		
	    		if ( bottomCollision( playerEntity.getBoundingBox(), collidingWalls.get(i).getBoundingBox() ) )
	    		{
	    			collisionDown = true;
	    		}
    		}
    	}

		collidingTreasure = treasureCollisionCheck(playerEntity.getBoundingBox());
		if ( collidingTreasure != null )
		{
			//System.out.println("Player is colliding with Treasure #" + treasureList.indexOf( collidingTreasure ) + " in list!");
			//System.out.println("Treasure Collected!");
			playerEntity.getBackpack().add( collidingTreasure );
			treasureList.remove(collidingTreasure);
		}
		
    	playerEntity.setCollisionDetection( collisionUp, collisionDown, collisionLeft, collisionRight );
    	

    	
    	// Checking each entity for collisions
    	for ( int i = 0; i < entityList.size(); i++ )
    	{
    		/*/
    		collidingEntity = entityCollisionCheck( entityList.get(i).getBoundingBox() );
    		if ( collidingEntity != null )
    		{
    			//System.out.println( "Entity #" + i + " is colliding with Entity #" + entityList.indexOf( collidingEntity ) + " in list!");
    		}
    		
    		collidingWall = wallCollisionCheck( entityList.get(i).getBoundingBox() );
        	if ( collidingWall != null )
        	{
    			//System.out.println( "Entity #" + i + " is colliding with Wall #" + wallList.indexOf( collidingWall ) + " in list!");
        	} // */

			if ( entityList.get(i).getAdjacentAgentSensor().contains( playerEntity.getCenter() ) )
			{
				entityList.get(i).setAlertion(true);
			}
    	}
    	

    }
    
    private GameEntity entityCollisionCheck( BoundingBox boundingBox )
    {
    	for ( int i = 0; i < entityList.size(); i++ )
    	{
    		if ( boundingBox.isBoundingBoxIntersecting( entityList.get(i).getBoundingBox() ) && !boundingBox.equals( entityList.get( i ).getBoundingBox() ) )
    		{
    			return entityList.get( i );
    		}
    	}
    	
    	return null;
    }

	private Treasure treasureCollisionCheck( BoundingBox boundingBox )
	{
		for ( int i = 0; i < treasureList.size(); i++ )
		{
			if ( boundingBox.isBoundingBoxIntersecting( treasureList.get(i).getBoundingBox() ) )
			{
				return treasureList.get( i );
			}
		}

		return null;
	}
    
    private ArrayList<WallObject> wallCollisionCheck( BoundingBox boundingBox )
    {
    	ArrayList<WallObject> collidingWalls = new ArrayList<WallObject>( wallList.size() );
    	
    	for ( int i = 0; i < wallList.size(); i++ )
    	{
    		if ( boundingBox.isBoundingBoxIntersecting( wallList.get( i ).getBoundingBox() ) )
    		{
    			collidingWalls.add( wallList.get( i ) );
    			
    			//return wallList.get( i );
    		}
    	}
    	
    	return collidingWalls;
    }
    
    private boolean leftCollision( BoundingBox thisBoundingBox, BoundingBox otherBoundingBox )
    {
    	if ( ( thisBoundingBox.getX() < otherBoundingBox.getX() + otherBoundingBox.getWidth() ) && 
    			( thisBoundingBox.getX() + thisBoundingBox.getWidth() > otherBoundingBox.getX() + otherBoundingBox.getWidth() ) )
    	{
    		return true;
    	}
    	
    	return false;
    }
    
    private boolean rightCollision( BoundingBox thisBoundingBox, BoundingBox otherBoundingBox )
    {
    	if ( ( thisBoundingBox.getX() < otherBoundingBox.getX() ) && thisBoundingBox.getX() + thisBoundingBox.getWidth() > otherBoundingBox.getX() )
    	{
    		return true;
    	}
    	
    	return false;
    }
    
    private boolean topCollision( BoundingBox thisBoundingBox, BoundingBox otherBoundingBox )
    {
    	if ( ( thisBoundingBox.getY() < otherBoundingBox.getY() ) && ( thisBoundingBox.getY() > otherBoundingBox.getY() - otherBoundingBox.getHeight() ) )
    	{
    		return true;
    	}
    	
    	return false;
    }
    
    private boolean bottomCollision( BoundingBox thisBoundingBox, BoundingBox otherBoundingBox )
    {
    	if ( ( thisBoundingBox.getY() - thisBoundingBox.getHeight() < otherBoundingBox.getY() ) && ( thisBoundingBox.getY() > otherBoundingBox.getY() ) )
    	{
    		return true;
    	}
    	
    	return false;
    }
}
