package mycontroller;

import controller.CarController;
import utilities.Coordinate;
import world.Car;
import world.WorldSpatial;

import java.util.*;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.HashMap;

import tiles.MapTile;


public class MyAIController extends CarController{
	private enum Commands {FORWARD, REVERSE, LEFT, RIGHT, BRAKE};
	private Queue<Commands> commandsQueue = new LinkedList<>();
	private StrategyFactory strategy = new Strategy();
	// to store how many keys already got
    private ArrayList<Coordinate> keyList = new ArrayList<>();
    private HashMap<Coordinate, MapTile> map = super.getMap();
    private PathFinder pathFinder;
    // How many minimum units the wall is away from the player.
    private int wallSensitivity = 1;

    private boolean isFollowingWall = false; // This is set to true when the car starts sticking to a wall.
    private Route route;

	public MyAIController(Car car) {
		super(car);
        route = new Route(map, super.getPosition());
        pathFinder = new BreadthFirstSearchPathFinding(route);
	}

	/**
     * basic idea: view and gain the TRAP information
     * then use the info to recover the original map
     * finally use DFS/Dijkstra to get all keys then exit.(?????????????????????????????????NOVAN REALLY?????)
    */
	@Override
	public void update() {

		if (commandsQueue.isEmpty()){
			Coordinate x = getCurrentCoordinate();
			Coordinate y = new Coordinate(x.x + 3, x.y - 1);
            List<Coordinate> z = pathFinder.findBestPath(x, y, getOrientation());
//            for (Coordinate a:z) {
//                System.out.println(a.toString());
//            }
			setCommandSequence(z);
		}

		Commands nextCommand = commandsQueue.poll();
		assert nextCommand != null;
		switch (nextCommand){
			case LEFT:
				turnLeft();
				break;
			case RIGHT:
				turnRight();
				break;
			case REVERSE:
				applyReverseAcceleration();
				break;
			case FORWARD:
				applyForwardAcceleration();
				break;
			case BRAKE:
				applyBrake();
				break;
		}


	}

	public void setCommandSequence(List<Coordinate> coordinates){
		Coordinate currentCoordinate = getCurrentCoordinate();
		WorldSpatial.Direction currentOrientation = getOrientation();
		final int[] RIGHT_DIRECTION = {1,0};
		final int[] LEFT_DIRECTION = {-1,0};
		final int[] UP_DIRECTION = {0,1};
		final int[] DOWN_DIRECTION = {0,-1};
		boolean faceForward = false;
		for(Coordinate coordinate: coordinates){
			int deltaX = coordinate.x - currentCoordinate.x;
			int deltaY = coordinate.y - currentCoordinate.y;
			int[] direction = {deltaX, deltaY};

			switch(currentOrientation){
				case EAST:

					if (Arrays.equals(direction, RIGHT_DIRECTION)){
						commandsQueue.add(Commands.FORWARD);
						faceForward = true;
					} else if (Arrays.equals(direction, LEFT_DIRECTION)){
						commandsQueue.add(Commands.REVERSE);
					} else if (Arrays.equals(direction, UP_DIRECTION)){
						commandsQueue.add(Commands.LEFT);
						if (faceForward){
							currentOrientation = WorldSpatial.Direction.NORTH;
						} else {
							currentOrientation = WorldSpatial.Direction.SOUTH;
						}
					} else if (Arrays.equals(direction, DOWN_DIRECTION)){
						commandsQueue.add(Commands.RIGHT);
						if (faceForward){
							currentOrientation = WorldSpatial.Direction.SOUTH;
						} else {
							currentOrientation = WorldSpatial.Direction.NORTH;
						}
					}
                    break;
                case WEST:
                    if (Arrays.equals(direction, RIGHT_DIRECTION)){
                        commandsQueue.add(Commands.REVERSE);
                    } else if (Arrays.equals(direction, LEFT_DIRECTION)){
                        commandsQueue.add(Commands.FORWARD);
                        faceForward = true;
                    } else if (Arrays.equals(direction, UP_DIRECTION)){
                        commandsQueue.add(Commands.RIGHT);
                        if (faceForward){
                            currentOrientation = WorldSpatial.Direction.NORTH;
                        } else {
                            currentOrientation = WorldSpatial.Direction.SOUTH;
                        }
                    } else if (Arrays.equals(direction, DOWN_DIRECTION)){
                        commandsQueue.add(Commands.LEFT);
                        if (faceForward){
                            currentOrientation = WorldSpatial.Direction.SOUTH;
                        } else {
                            currentOrientation = WorldSpatial.Direction.NORTH;
                        }
                    }
                    break;
                case NORTH:
                    if (Arrays.equals(direction, RIGHT_DIRECTION)){
                        commandsQueue.add(Commands.RIGHT);
                        if (faceForward){
                            currentOrientation = WorldSpatial.Direction.EAST;
                        } else {
                            currentOrientation = WorldSpatial.Direction.WEST;
                        }
                    } else if (Arrays.equals(direction, LEFT_DIRECTION)){
                        commandsQueue.add(Commands.LEFT);
                        if (faceForward){
                            currentOrientation = WorldSpatial.Direction.WEST;
                        } else {
                            currentOrientation = WorldSpatial.Direction.EAST;
                        }
                    } else if (Arrays.equals(direction, UP_DIRECTION)){
                        commandsQueue.add(Commands.FORWARD);
                        faceForward = true;
                    } else if (Arrays.equals(direction, DOWN_DIRECTION)){
                        commandsQueue.add(Commands.REVERSE);
                    }
                    break;
                case SOUTH:
                    if (Arrays.equals(direction, RIGHT_DIRECTION)){
                        commandsQueue.add(Commands.LEFT);
                        if (faceForward){
                            currentOrientation = WorldSpatial.Direction.EAST;
                        } else {
                            currentOrientation = WorldSpatial.Direction.WEST;
                        }
                    } else if (Arrays.equals(direction, LEFT_DIRECTION)){
                        commandsQueue.add(Commands.RIGHT);
                        if (faceForward){
                            currentOrientation = WorldSpatial.Direction.WEST;
                        } else {
                            currentOrientation = WorldSpatial.Direction.EAST;
                        }
                    } else if (Arrays.equals(direction, UP_DIRECTION)){
                        commandsQueue.add(Commands.REVERSE);
                    } else if (Arrays.equals(direction, DOWN_DIRECTION)){
                        commandsQueue.add(Commands.FORWARD);
                        faceForward = true;
                    }
                    break;
            }

            currentCoordinate = coordinate;
        }

        commandsQueue.add(Commands.BRAKE);
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


	public void findBestPath(Coordinate nextDestination){


	}

	private Coordinate getCurrentCoordinate() {
		List<Integer> coordinateString =
				Arrays.stream(getPosition().split(","))
						.map(Integer::parseInt)
						.collect(Collectors.toList());

		return new Coordinate(coordinateString.get(0), coordinateString.get(1));
	}

}
