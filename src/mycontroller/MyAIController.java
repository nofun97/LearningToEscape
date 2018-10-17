package mycontroller;

import controller.CarController;
import utilities.Coordinate;
import world.Car;
import world.WorldSpatial;

import java.util.*;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.HashMap;
import controller.CarController;
import tiles.HealthTrap;
import tiles.LavaTrap;
import tiles.MapTile;
import world.WorldSpatial;


public class MyAIController extends CarController{
	private enum Commands {FORWARD, REVERSE, LEFT, RIGHT, BRAKE};
    private ArrayList<Coordinate> recordCoordinate = new ArrayList<>();
	private Queue<Commands> commandsQueue = new LinkedList<>();
	private StrategyFactory strategy = new Strategy();

	// to store how many keys already got
    private ArrayList<Coordinate> keyList = new ArrayList<>();
    private HashMap<Coordinate, MapTile> map = super.getMap();

    // How many minimum units the wall is away from the player.
    private int wallSensitivity = 1;

    // This is set to true when the car starts sticking to a wall.
    private boolean isFollowingWall = false;
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

		if (commandsQueue.isEmpty()){
			Coordinate coord = getCurrentCoordinate();
			ArrayList<Coordinate> coordinates = new ArrayList<>();
			coordinates.add(new Coordinate(coord.x + 1, coord.y));
			coordinates.add(new Coordinate(coord.x + 2, coord.y));
			coordinates.add(new Coordinate(coord.x + 2, coord.y-1));
			coordinates.add(new Coordinate(coord.x + 3, coord.y-1));
			setCommandSequence(coordinates);
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

    // LOOK AT THISSSSSS!!!!!!!! VERY IMPORTANT!!!!!
    // update the view of the car (replace the normal tile with trap tile if given)
    private void updateMap() {
        HashMap<Coordinate, MapTile> currentView = getView();
        MapTile newTile, currentTile;

        for(Coordinate coord : currentView.keySet()) {
            newTile = currentView.get(coord);
            Coordinate tmp = new Coordinate(coord.x, coord.y);
            currentTile = map.get(tmp);
            if(currentTile != null && newTile.getType() != currentTile.getType()
                    & !recordCoordinate.contains(coord)) {
                //TODO: WE NEED TO DO something to handle the trap, (avoid, gotKey or goToHeal?????????)then mark it as
                //TODO: handled, no need to handle this point agiannnnnn.
                map.put(tmp, newTile);
                recordCoordinate.add(coord);
            }
        }
    }

    public void handleTheTrap(Coordinate coord, MapTile mapTile){
        //TODO: if there is a LAVA, based on the condition(do we have any keys?)
        //TODO: I think here need to implement strategy
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
