package mycontroller;

import controller.CarController;
import mycontroller.pathfinders.BreadthFirstSearchPathFinding;
import mycontroller.pathfinders.PathFinder;
import mycontroller.strategies.KeyPriorityStrategy;
import mycontroller.strategies.MyStrategy;
import mycontroller.strategies.StrategyFactory;
import tiles.HealthTrap;
import tiles.LavaTrap;
import tiles.MudTrap;
import utilities.Coordinate;
import world.Car;
import world.WorldSpatial;

import java.util.*;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.HashMap;

import tiles.MapTile;


public class MyAIController extends CarController{
	private enum Commands {FORWARD, REVERSE, LEFT, RIGHT, BRAKE, NONE}
    private ArrayList<Coordinate> recordCoordinate = new ArrayList<>();
	private Queue<Commands> commandsQueue = new LinkedList<>();
	private Queue<Coordinate> pathQueue;
	private StrategyFactory strategy;
	private MyStrategy myStrategy;
    public static final int BLOCK = -1;
    //TODO: just give a threshold
    public static final int healthLimit = 90;
//	private StrategyFactory strategy = new KeyPriorityStrategy();

	// to store how many keys already got
    private ArrayList<Coordinate> keyList = new ArrayList<>();
    private HashMap<Coordinate, MapTile> map = super.getMap();
    private PathFinder pathFinder;

    // How many minimum units the wall is away from the player.
    private int wallSensitivity = 1;

    // This is set to true when the car starts sticking to a wall.
    private boolean isFollowingWall = false;
    private Route route;
//    private boolean calculated = false;
	public MyAIController(Car car) {
		super(car);
        route = new Route(map, super.getPosition());
        pathFinder = new BreadthFirstSearchPathFinding(route);
        pathQueue = new LinkedList<>();
        strategy = new KeyPriorityStrategy(this.route);
        //TODO: cuz we need a place to store the key!!
        myStrategy = new MyStrategy(car, route, keyList);

	}

	/**
     * basic idea: view and gain the TRAP information
     * then use the info to recover the original map
     * finally use DFS/Dijkstra to get all keys then exit.(?????????????????????????????????NOVAN REALLY?????)
    */
	@Override
	public void update() {
        /**
         *  Update the map based on the TRAP information given
         */
        updateMap();
        Coordinate currentCoordinate = getCurrentCoordinate();
        route.updateMap(currentCoordinate.x, currentCoordinate.y);
//        route.printGridMap();
        /**
         * Creating a command sequence to go to a certain point
         */
//        if (commandsQueue.isEmpty() && !calculated)
        if (commandsQueue.isEmpty()){
            // TODO just a simple case, need to be fixed

            /**
             * Generate the next coordinate the car should go through
             */
			Coordinate destination =
                    strategy.decideNextCoordinate(currentCoordinate);
//            System.out.println(destination.toString());
            /**
             * Generate a list of coordinates that the car has to go through
             * using certain path finding calculation
             */
            List<Coordinate> path =
                    pathFinder.findBestPath
                            (currentCoordinate, destination, getOrientation());

            while(path == PathFinder.UNREACHABLE){
                route.blockFromSource(destination.x, destination.y);
                destination =  strategy.decideNextCoordinate(currentCoordinate);

                System.out.println(destination.toString());

                path = pathFinder.findBestPath
                        (currentCoordinate, destination, getOrientation());
            }
            pathQueue = new LinkedList<>(path);
            pathQueue.poll();
            /**
             * Converting a list of coordinates into commands based on the car
             * condition
             */
            setCommandSequence(path);

		}

        /**
         * Taking the command enum and giving that command based on the enum
         */
        checkOncomingCollision();

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
            case NONE:
                break;
		}


	}

    /**
     * Check the next coordinate where the car will head to and it will stop
     * should it be a trap that kills or a wall
     */
	public void checkOncomingCollision(){
	    Coordinate nextPath = pathQueue.poll();
	    if (nextPath != null && route.isBlocked(nextPath.x, nextPath.y)){
	        commandsQueue = new LinkedList<>();
	        commandsQueue.add(Commands.BRAKE);
        }
    }


    /**
     * setCommandSequence takes a list of coordinates that the car has to go
     * through and turns it into a list of commands the car has to do to reach
     * that point
     * @param coordinates the list of coordinates
     */
	public void setCommandSequence(List<Coordinate> coordinates){
        /**
         * Get current coordinates and current orientation
         */
		Coordinate currentCoordinate = getCurrentCoordinate();
		WorldSpatial.Direction currentOrientation = getOrientation();

        /**
         * Accepted values of direction
         */
		final int[] RIGHT_DIRECTION = {1,0};
		final int[] LEFT_DIRECTION = {-1,0};
		final int[] UP_DIRECTION = {0,1};
		final int[] DOWN_DIRECTION = {0,-1};

        /**
         * Dictates to whether the car is facing forward or not
         */
		boolean faceForward = false;

        /**
         * Dictates whether acceleration is applied, when acceleration is
         * already applied, no more acceleration is needed. This is to
         * prevent crashing into the walls.
         */
        boolean accelerationApplied = false;

        /**
         * Process commands based on each coordinates
         */
		for(Coordinate coordinate: coordinates){

            /**
             * Changing the next coordinate and the current coordinate into
             * acceptable values of direaction
             */
			int deltaX = coordinate.x - currentCoordinate.x;
			int deltaY = coordinate.y - currentCoordinate.y;
			int[] direction = {deltaX, deltaY};
//            System.out.println(Arrays.toString(direction));
            /**
             * Based on the car orientation and where the car is supposed to
             * go, it gives the correct command and the new orientation should
             * the car change orientation
             */
			switch(currentOrientation){
				case EAST:

					if (Arrays.equals(direction, RIGHT_DIRECTION) &&
                            !accelerationApplied){

						commandsQueue.add(Commands.FORWARD);
						accelerationApplied = true;
						faceForward = true;

					} else if (Arrays.equals(direction, LEFT_DIRECTION) &&
                            !accelerationApplied){

                        accelerationApplied = true;
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

					} else if (accelerationApplied){
					    commandsQueue.add(Commands.NONE);
                    }
                    break;
                case WEST:
                    if (Arrays.equals(direction, RIGHT_DIRECTION) &&
                            !accelerationApplied){

                        accelerationApplied = true;
                        commandsQueue.add(Commands.REVERSE);

                    } else if (Arrays.equals(direction, LEFT_DIRECTION) &&
                            !accelerationApplied){

                        commandsQueue.add(Commands.FORWARD);
                        accelerationApplied = true;
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

                    } else if (accelerationApplied){
                        commandsQueue.add(Commands.NONE);
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

                    } else if (Arrays.equals(direction, UP_DIRECTION) &&
                            !accelerationApplied){

                        accelerationApplied = true;
                        commandsQueue.add(Commands.FORWARD);
                        faceForward = true;

                    } else if (Arrays.equals(direction, DOWN_DIRECTION) &&
                            !accelerationApplied){

                        accelerationApplied = true;
                        commandsQueue.add(Commands.REVERSE);

                    } else if (accelerationApplied){
                        commandsQueue.add(Commands.NONE);
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

                    } else if (Arrays.equals(direction, UP_DIRECTION) &&
                            !accelerationApplied){

                        accelerationApplied = true;
                        commandsQueue.add(Commands.REVERSE);

                    } else if (Arrays.equals(direction, DOWN_DIRECTION) &&
                            !accelerationApplied){

                        accelerationApplied = true;
                        commandsQueue.add(Commands.FORWARD);
                        faceForward = true;

                    } else if (accelerationApplied){
                        commandsQueue.add(Commands.NONE);
                    }
                    break;
            }

            currentCoordinate = coordinate;
        }

        /**
         * Braking everytime
         */
        commandsQueue.add(Commands.BRAKE);
    }

    // LOOK AT THISSSSSS!!!!!!!! VERY IMPORTANT!!!!!
    // TODO HELLLOOOOOH THE CAR CAN SPIN YAY it's 2 AM im dying
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
                handleTheTrap(coord, map.get(coord));
            }
        }
    }


    public void handleTheTrap(Coordinate coordinate, MapTile mapTile){
        if (mapTile instanceof MudTrap){
            int[][] tempMap = route.getGridMap();
            tempMap[coordinate.y][coordinate.x] = BLOCK;
            // TODO: strategy, implement avoid
        }
        //TODO: if there is a LAVA, based on the condition(do we have any keys?)
        else if(mapTile instanceof LavaTrap){
            // if we already have at least one key
            if(((LavaTrap)mapTile).getKey() != 0){
                //TODO: dont know should put which condition)
                if(){
                    myStrategy.gettingKey(coordinate);
                }
                // else just record the keys location
                else{
                    keyList.add(coordinate);
                }
                return;
            }
            else if(mapTile instanceof HealthTrap){
                if(super.getHealth() <= healthLimit){
                    myStrategy.getHealing(coordinate);
                    return;
                }
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




	public void findBestPath(Coordinate nextDestination){


	}

    /**
     * Converting position from string into a coordinate
     *
     * @return the converted string into coordinate
     */
	private Coordinate getCurrentCoordinate() {
		return new Coordinate(getPosition());
	}

}
