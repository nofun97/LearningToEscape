package mycontroller.states;

import mycontroller.pathfinders.PathFinder;
import mycontroller.Route;
import utilities.Coordinate;
import world.World;

import java.util.*;

/**
 * The type Exploration method for the car.
 */
public class ExplorationState implements State {
    public static final Coordinate ALL_EXPLORED = null;
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
     * @param explorationMap the exploration map
     */
    public ExplorationState(int[][] explorationMap) {
        this.explorationMap = explorationMap;
    }

    @Override
    public Coordinate getCoordinate(Coordinate currentCoordinate) {
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
                                    .filter(value -> value != Route.BLOCKED)
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
        Set<Coordinate> initialPossibleCoordinates = new LinkedHashSet<>();
        Set<Coordinate> addedCoordinates = new HashSet<>();
        Coordinate initialCoordinate = new Coordinate(currentCoordinate.x,
                currentCoordinate.y);

        initialPossibleCoordinates.add(initialCoordinate);
        addedCoordinates.add(initialCoordinate);
        printExplorationMap();


        /**
         * Find the nearest unexplored spot
         */
        return findNearestUnexploredSpot(initialPossibleCoordinates,
                addedCoordinates);
    }

    @Override
    public void addImportantCoordinate(Coordinate coordinate) {
        ;
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

        /**
         * When there is no more possible coordinates it will return null
         * value as everything is explored
         */

        //TODO this needs to be handled by strategy
        if (possibleCoordinate.isEmpty()) return ALL_EXPLORED;

        for (Coordinate currentCoordinate: possibleCoordinate) {

            /**
             * Iterate through each coordinate starting from the ones nearest to
             * the car to the furthest to find the nearest coordinate with
             * the smallest value
             */
            int currentX = currentCoordinate.x;
            int currentY = currentCoordinate.y;

            for (int i = 0; i < PathFinder.NUM_OF_POSSIBLE_DIRECTION; i++) {
                int nextX = currentX + PathFinder.DIRECTIONS_DELTA[i];
                int nextY = currentY + PathFinder.DIRECTIONS_DELTA
                        [(i+1)%PathFinder.NUM_OF_POSSIBLE_DIRECTION];
                Coordinate coordinate = new Coordinate(nextX, nextY);

                if(!Route.isWithinMap(nextX, nextY)) continue;

                if(explorationMap[nextY][nextX] == smallestValue){
//                    System.out.printf("%d %d\n", nextX, nextY);
                    return new Coordinate(nextX, nextY);
                } else if (explorationMap[nextY][nextX] != Route.BLOCKED &&
                        !addedCoordinates.contains(coordinate)){
                    /**
                     * If coordinate has not been checked, it will be added
                     * into the next checked coordinate
                     */

                    nextPossibleCoordinate.add(coordinate);
                }

                /**
                 * Record checked coordinates
                 */
                addedCoordinates.add(coordinate);
            }
        }
//        System.out.println(nextPossibleCoordinate.size());
//        printExplorationMap();
        /**
         * checking the neighbouring coordinates of the next possible
         * coordinates
         */
        return findNearestUnexploredSpot(nextPossibleCoordinate,
                addedCoordinates);
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
