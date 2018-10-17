package mycontroller;

import com.sun.xml.internal.bind.v2.TODO;
import controller.CarController;
import utilities.Coordinate;
import world.Car;

import java.util.ArrayList;
import java.util.HashMap;
import controller.CarController;
import tiles.HealthTrap;
import tiles.LavaTrap;
import tiles.MapTile;
import world.WorldSpatial;


public class MyAIController extends CarController{
	// to store how many keys already got
    private ArrayList<Coordinate> keyList = new ArrayList<>();
    private HashMap<Coordinate, MapTile> map = super.getMap();
    // How many minimum units the wall is away from the player.
    private int wallSensitivity = 1;

    private boolean isFollowingWall = false; // This is set to true when the car starts sticking to a wall.
    private Route route;

	public MyAIController(Car car) {
		super(car);
        route = new Route(map, super.getPosition());
	}

	/**
     * basic idea: view and gain the TRAP information
     * then use the info to recover the original map
     * finally use DFS/Dijkstra to get all keys then exit.(?????????????????????????????????NOVAN REALLY?????)
    */
	@Override
	public void update() {
		//exploreMap(super.getView());
	}

    // update the view of the car (replace the normal tile with trap tile if given)
    private void updateMap() {
        HashMap<Coordinate, MapTile> currview = getView();
        MapTile newtile, currtile;

        for(Coordinate c : currview.keySet()) {
            newtile = currview.get(c);
            Coordinate tmp = new Coordinate(c.x, c.y);
            if((currtile = map.get(tmp)) != null && newtile.getType() != currtile.getType()) {
                map.put(tmp, newtile);
            }
        }
    }

    /**
     * Turn the car anti-clockwise
     */
    private void applyLeftTurn(WorldSpatial.Direction orientation) {
        switch(orientation) {
            case EAST:
                if(!getOrientation().equals(WorldSpatial.Direction.NORTH)) {
                    turnLeft();
                }
                break;
            case NORTH:
                if(!getOrientation().equals(WorldSpatial.Direction.WEST)) {
                    turnLeft();
                }
                break;
            case SOUTH:
                if(!getOrientation().equals(WorldSpatial.Direction.EAST)) {
                    turnLeft();
                }
                break;
            case WEST:
                if(!getOrientation().equals(WorldSpatial.Direction.SOUTH)) {
                    turnLeft();
                }
                break;
            default:
                break;

        }

    }

    /**
     * Turn the car clockwise
     */
    private void applyRightTurn(WorldSpatial.Direction orientation) {
        switch(orientation) {
            case EAST:
                if(!getOrientation().equals(WorldSpatial.Direction.SOUTH)) {
                    turnRight();
                }
                break;
            case NORTH:
                if(!getOrientation().equals(WorldSpatial.Direction.EAST)) {
                    turnRight();
                }
                break;
            case SOUTH:
                if(!getOrientation().equals(WorldSpatial.Direction.WEST)) {
                    turnRight();
                }
                break;
            case WEST:
                if(!getOrientation().equals(WorldSpatial.Direction.NORTH)) {
                    turnRight();
                }
                break;
            default:
                break;

        }

    }
//
//	// explore and recover the original map based on the TRAP info received.
//	public void exploreMap(HashMap<Coordinate, MapTile> knownMap){
//	    for(Coordinate coord : knownMap.keySet()){
//	        MapTile current = knownMap.get(coord);
//	        if(current.isType(MapTile.Type.TRAP)){
//                //TODO: DO something to handle the trap, and mark it as handled
//            }
//        }
//    }
//
//    public void handleTheTrap(Coordinate coord, MapTile mapTile){
//	    //TODO: if there is a LAVA, based on the condition(do we have any keys?)
//    }
}
