package mycontroller.states;

import mycontroller.pathfinders.PathFinder;
import mycontroller.Route;
import utilities.Coordinate;
import world.World;
import world.WorldSpatial;

import java.util.*;

/**
 * The type Exploration method for the car.
 */
public class ExplorationState implements State {
    public static final Coordinate ALL_EXPLORED = null;
    public static final int UNEXPLORED = 0;
    public static final int LEVELS_TO_SKIP = 2;
    private int modifier;
    private Route route;
    private boolean finished = false;
    /**
     * a map that marks which coordinate has not been explored
     */
    private int[][] explorationMap;


    /**
     * the coordinate with smallest value means it is unexplored
     */
    private int smallestValue;

    /**
     * Instantiates a new Exploration state.
     *
     * @param route the exploration map
     */
    public ExplorationState(Route route) {
        this.explorationMap = route.getGridMap();
        this.route = route;
    }

    @Override
    public Coordinate getCoordinate(Coordinate currentCoordinate,
                                    WorldSpatial.Direction orientation) {
        /**
         * initializing initial value
         */
        smallestValue = Integer.MAX_VALUE;
//        printExplorationMap();

        /**
         * Finding the smallest value of the coordinate that has not been
         * explored
         */
        for (int[] row : explorationMap) {
            int currentValue;

            try{
                /**
                 * Finding minimum value of a row
                 */
                currentValue = Arrays.stream(row)
                                    .filter(value -> value != Route.BLOCKED &&
                                            value != Route.TO_AVOID)
                                    .min()
                                    .getAsInt();
            } catch (NoSuchElementException e){
                /**
                 * Happens when all members of row is Route.Blocked
                 */
                currentValue = Integer.MAX_VALUE;
            }

            /**
             * Updating the smallest value
             */
            if (smallestValue > currentValue){
                smallestValue = currentValue;
            }
        }
//        System.out.println(smallestValue);
        /**
         * Initial possible coordinate which are the coordinates around the car
         */
        modifier = orientationPriorityModifier(orientation);
        Set<Coordinate> initialPossibleCoordinates = new LinkedHashSet<>();
        Set<Coordinate> addedCoordinates = new HashSet<>();
//        int sourceX = currentCoordinate.x;
//        int sourceY = currentCoordinate.y;
        addedCoordinates.add(currentCoordinate);
        initialPossibleCoordinates.add(currentCoordinate);


        /**
         * Find the nearest unexplored spot
         */
//        return findNearestUnexploredSpot(initialPossibleCoordinates,
//                addedCoordinates);
        Coordinate a = findNearestUnexploredSpot(initialPossibleCoordinates,
                addedCoordinates);

//        printExplorationMap();
//        System.out.println(a.toString());
        return a;
    }

    @Override
    public boolean offerImportantCoordinate(Coordinate coordinate) {
        return false;
    }

    @Override
    public int getSize() {
        return 1;
    }

    @Override
    public void removeCoordinate(Coordinate coordinate) {
        ;
    }

    /**
     * A priority modifier for finding unexplore spots, the integer returned
     * adds a number that will push the starting values of the PathFinder
     * .DIRECTIONS_DELTA
     * @param orientation current orientation of the car
     * @return the number modifier
     */
    private int orientationPriorityModifier(WorldSpatial.Direction orientation){
        switch (orientation){
            case EAST:
                return 0;
            case NORTH:
                return 1;
            case WEST:
                return 2;
            case SOUTH:
                return 3;
        }
        return 0;
    }
    /**
     * Find nearest unexplored coordinate.
     *
     * @param possibleCoordinate the possible coordinate
     * @param addedCoordinates to record coordinates that has been checked
     * @return the nearest unexplored coordinate
     */
    public Coordinate findNearestUnexploredSpot(Set<Coordinate>
                                                          possibleCoordinate,
                                                Set<Coordinate>
                                                        addedCoordinates){
        Set<Coordinate> nextPossibleCoordinate = new LinkedHashSet<>();
        /**
         * Iterate through the possible coordinates and adding the next
         * possible coordinates
         */
//        System.out.println(possibleCoordinate.size());


        for (Coordinate currentCoordinate: possibleCoordinate) {

            /**
             * Iterate through each coordinate starting from the ones nearest to
             * the car to the furthest to find the nearest coordinate with
             * the smallest value
             */
            int currentX = currentCoordinate.x;
            int currentY = currentCoordinate.y;

            for (int i = 0; i < PathFinder.NUM_OF_POSSIBLE_DIRECTION; i++) {
                    int index1 = (i + modifier) %
                            PathFinder.NUM_OF_POSSIBLE_DIRECTION;
                    int index2 =
                            (i + modifier + 1) %
                                    PathFinder.NUM_OF_POSSIBLE_DIRECTION;
//                int index1 = i;
//                int index2 = (i+1)%PathFinder.NUM_OF_POSSIBLE_DIRECTION;
                int nextX = currentX + PathFinder.DIRECTIONS_DELTA[index1];
                int nextY = currentY + PathFinder.DIRECTIONS_DELTA[index2];
                Coordinate coordinate = new Coordinate(nextX, nextY);

                if(!Route.isWithinMap(nextX, nextY)) continue;
//                System.out.printf("%2d %2d\n", nextX, nextY);
                if(explorationMap[nextY][nextX] == smallestValue &&
                        !route.toAvoid(coordinate)){
                    return new Coordinate(nextX, nextY);
                } else if (!route.isBlocked(nextX, nextY) &&
                        !addedCoordinates.contains(coordinate)){
                    /**
                     * If coordinate has not been checked, it will be added
                     * into the next checked coordinate
                     */

                    nextPossibleCoordinate.add(coordinate);
                    /**
                     * Record checked coordinates
                     */
                    addedCoordinates.add(coordinate);
                }


            }
        }

//        printExplorationMap();
//        System.out.println("SIZE: "+nextPossibleCoordinate.size());
//        printExplorationMap();
        /**
         * checking the neighbouring coordinates of the next possible
         * coordinates
         */
        return findNearestUnexploredSpot(nextPossibleCoordinate,
                addedCoordinates);
    }

    @Override
    public boolean isFinished() {
        if(finished) return true;
        for (int[] row: explorationMap){
            if(Arrays.stream(row).anyMatch(x -> x == UNEXPLORED)){
                return false;
            }
        }
        finished = true;
        return true;
    }

    private void printExplorationMap(){
        for (int i = World.MAP_HEIGHT - 1; i >= 0; i--) {
            int[] x = explorationMap[i];
            for (int y :
                    x) {
                System.out.printf("%2d ", y);
            }
            System.out.printf("\n");
        }
        System.out.printf("\n");
    }
}
