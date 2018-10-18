package mycontroller.states;

import mycontroller.pathfinders.PathFinder;
import mycontroller.Route;
import utilities.Coordinate;
import world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * The type Exploration method for the car.
 */
public class ExplorationState implements State {

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
    public Coordinate getNearestCoordinate(Coordinate currentCoordinate) {
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
        ArrayList<Integer[]> initialPossibleCoordinates = new ArrayList<>();
        int currentX = currentCoordinate.x;
        int currentY = currentCoordinate.y;

        for (int i = 0; i < PathFinder.NUM_OF_POSSIBLE_DIRECTION; i++) {
            int nextX = currentX + PathFinder.DIRECTIONS_DELTA[i];
            int nextY = currentY + PathFinder.DIRECTIONS_DELTA
                    [(i+1)%PathFinder.NUM_OF_POSSIBLE_DIRECTION];

            if(!Route.isWithinMap(nextX, nextY)) continue;

            initialPossibleCoordinates.add(new Integer[]{nextX, nextY});

        }

        /**
         * Find the nearest unexplored spot
         */
        return findNearestUnexploredSpot(initialPossibleCoordinates);
//        Coordinate x = findNearestUnexploredSpot(initialPossibleCoordinates);
//        System.out.println(x.toString());
//        return x;
    }

    /**
     * Find nearest unexplored coordinate.
     *
     * @param possibleCoordinate the possible coordinate
     * @return the nearest unexplored coordinate
     */
    public Coordinate findNearestUnexploredSpot(ArrayList<Integer[]>
                                                          possibleCoordinate){
        ArrayList<Integer[]> nextPossibleCoordinate = new ArrayList<>();

        /**
         * Iterate through the possible coordinates and adding the next
         * possible coordinates
         */
        for (Integer[] currentCoordinate: possibleCoordinate) {

            /**
             * Iterate through each coordinate starting from the ones nearest to
             * the car to the furthest to find the nearest coordinate with
             * the smallest value
             */
            int currentX = currentCoordinate[0];
            int currentY = currentCoordinate[1];

            for (int i = 0; i < PathFinder.NUM_OF_POSSIBLE_DIRECTION; i++) {
                int nextX = currentX + PathFinder.DIRECTIONS_DELTA[i];
                int nextY = currentY + PathFinder.DIRECTIONS_DELTA
                        [(i+1)%PathFinder.NUM_OF_POSSIBLE_DIRECTION];

                if(!Route.isWithinMap(nextX, nextY)) continue;

                if(explorationMap[nextY][nextX] == smallestValue){
                    return new Coordinate(nextX, nextY);
                } else {
//                    System.out.printf("%d %d\n", nextX, nextY);
                    nextPossibleCoordinate.add(new Integer[]{nextX, nextY});
                }
            }
        }
//        System.out.println(nextPossibleCoordinate.size());

        /**
         * checking the neighbouring coordinates of the next possible
         * coordinates
         */
        return findNearestUnexploredSpot(nextPossibleCoordinate);
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
    }
}
