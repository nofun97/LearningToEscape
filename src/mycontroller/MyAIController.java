package mycontroller;

import controller.CarController;
import utilities.Coordinate;
import world.Car;
import world.WorldSpatial;

import java.util.*;
import java.util.stream.Collectors;

public class MyAIController extends CarController{
	private enum Commands {FORWARD, REVERSE, LEFT, RIGHT, BRAKE};
	private Queue<Commands> commandsQueue = new LinkedList<>();
	private StrategyFactory strategy = new Strategy();
	private Route map;
	// to store how many keys already got
    private ArrayList<Coordinate> keyList = new ArrayList<>();

	public MyAIController(Car car) {
		super(car);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

		if (commandsQueue.isEmpty()){
			Coordinate x = getCurrentCooordinate();
			ArrayList<Coordinate> coordinates = new ArrayList<>();
			coordinates.add(new Coordinate(x.x + 1, x.y));
			coordinates.add(new Coordinate(x.x + 2, x.y));
			coordinates.add(new Coordinate(x.x + 2, x.y-1));
			coordinates.add(new Coordinate(x.x + 3, x.y-1));
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
		Coordinate currentCoordinate = getCurrentCooordinate();
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

	public void findBestPath(Coordinate nextDestination){


	}

	private Coordinate getCurrentCooordinate() {
		List<Integer> coordinateString =
				Arrays.stream(getPosition().split(","))
						.map(Integer::parseInt)
						.collect(Collectors.toList());

		return new Coordinate(coordinateString.get(0), coordinateString.get(1));
	}

}
