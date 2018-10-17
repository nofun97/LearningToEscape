package mycontroller;

import utilities.Coordinate;
import world.World;
import world.WorldSpatial;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The PathFinder that uses the Breadth First Search Algorithm.
 */
public class BreadthFirstSearchPathFinding implements PathFinder{
    /**
     * The constant DISTANCE as distance from one coordinate to the next
     * coordinate is always 1.
     */
    public static final int DISTANCE = 1;
    /**
     * The constant NUM_OF_POSSIBLE_DIRECTION, a car can only go to one of
     * the four directions from a coordinate.
     */
    public static final int NUM_OF_POSSIBLE_DIRECTION = 4;
    /**
     * The constant DIRECTIONS_DELTA, used to calculate possible next
     * coordinates.
     */
    public static final int[] DIRECTIONS_DELTA = new int[]{0, 1, 0, -1};

    /**
     * distanceArray is used to execute the algorithm
     */
    private int[][] distanceArray;
    private Route route;

    /**
     * Instantiates a new Breadth first search path finding.
     *
     * @param route the route
     */
    public BreadthFirstSearchPathFinding(Route route) {
        distanceArray = new int[World.MAP_HEIGHT][World.MAP_WIDTH];
        this.route = route;
    }

    @Override
    public List<Coordinate> findBestPath(Coordinate currentCoordinate,
                                         Coordinate destination,
                                         WorldSpatial.Direction orientation) {

        /**
         * Assigning every coordinate as very far except the starting point
         */
        for (int i = 0; i < World.MAP_HEIGHT; i++) {
            for (int j = 0; j < World.MAP_WIDTH; j++) {
                distanceArray[i][j] = Integer.MAX_VALUE;
            }
        }

        distanceArray[currentCoordinate.y][currentCoordinate.x] = 0;



        /**
         * Based on the orientation of the car, defines where the next possible
         * coordinates. A car must move one coordinate forward or backward at
         * first, therefore there is only a maximum of two possible points
         * right after the initial coordinate.
         */
        ArrayList<Integer[]> initialPossibleCoordinates = new ArrayList<>();

        if (orientation == WorldSpatial.Direction.EAST ||
                orientation == WorldSpatial.Direction.WEST){

            if(route.gridMap[currentCoordinate.y][currentCoordinate.x+1] !=
                Route.WALL){
                distanceArray[currentCoordinate.y][currentCoordinate.x + 1]
                        = DISTANCE;
                initialPossibleCoordinates.add(
                        new Integer[]{currentCoordinate.x + 1,
                                currentCoordinate.y});
            }

            if(route.gridMap[currentCoordinate.y][currentCoordinate.x-1] !=
                    Route.WALL){
                distanceArray[currentCoordinate.y][currentCoordinate.x - 1]
                        = DISTANCE;

                initialPossibleCoordinates.add(
                        new Integer[]{currentCoordinate.x - 1,
                                currentCoordinate.y});
            }

        } else if (orientation == WorldSpatial.Direction.SOUTH ||
                        orientation == WorldSpatial.Direction.NORTH){
            if(route.gridMap[currentCoordinate.y+1][currentCoordinate.x] !=
                    Route.WALL){

                distanceArray[currentCoordinate.y + 1][currentCoordinate.x]
                        = DISTANCE;

                initialPossibleCoordinates.add(
                        new Integer[]{currentCoordinate.x,
                                currentCoordinate.y + 1});
            }

            if(route.gridMap[currentCoordinate.y-1][currentCoordinate.x] !=
                    Route.WALL){

                distanceArray[currentCoordinate.y - 1][currentCoordinate.x]
                        = DISTANCE;

                initialPossibleCoordinates.add(
                        new Integer[]{currentCoordinate.x - 1,
                                currentCoordinate.y});
            }
        }

        /**
         * Manipulate array to calculate best path
         */
        manipulateArray(initialPossibleCoordinates, destination);

//        for (int i = World.MAP_HEIGHT - 1; i >= 0; i--) {
//            int[] x = distanceArray[i];
//            for (int y :
//                    x) {
//                System.out.printf("%d ", y);
//            }
//            System.out.printf("\n");
//        }
//        System.out.printf("\n");
        return backtrack(currentCoordinate, destination);
    }

    /**
     * Manipulating distance array to calculate the best path
     *
     * @param possibleCoordinates possible center coordinates, its
     *                            surrounding values will be updated
     * @param destination the destination
     */
    private void manipulateArray(ArrayList<Integer[]> possibleCoordinates,
                                 Coordinate destination){

        boolean reachedDestination = false;

        /**
         * Gathering next coordinates as the centre point for its surrounding
         * values to be updated
         */
        ArrayList<Integer[]> nextPossibleCoordinates = new ArrayList<>();
        for (Integer[] coordinate: possibleCoordinates){
            /**
             * Convert the array into index
             */
            int x = coordinate[0];
            int y = coordinate[1];

            /**
             * Update the surrounding values should it fulfill the conditions
             */
            for (int i = 0; i < NUM_OF_POSSIBLE_DIRECTION; i++) {

                /**
                 * One of the surrounding coordinates to update
                 */
                int nextX = x + DIRECTIONS_DELTA[i];
                int nextY = y +
                        DIRECTIONS_DELTA[(i+1)%NUM_OF_POSSIBLE_DIRECTION];

                /**
                 * Ignoring invalid values
                 */
                if(nextX < 0 || nextX >= World.MAP_WIDTH || nextY < 0
                        || nextY >= World.MAP_HEIGHT){
                    continue;
                }

                /**
                 * Update the distance values should a faster way to reach there
                 * is faster and if it is not a wall
                 */
                if(route.gridMap[nextY][nextX] != Route.WALL &&
                        distanceArray[y][x] + DISTANCE <
                                distanceArray[nextY][nextX]){
                    distanceArray[nextY][nextX] =
                            distanceArray[y][x] + DISTANCE;
                    nextPossibleCoordinates.add(new Integer[]{nextX, nextY});
                }
            }

            /**
             * Reaching a destination means that the algorithm should stop
             */
            if(x == destination.x && y == destination.y){
                reachedDestination = true;
                break;
            }
        }

        /**
         * Recursively update the distance of every coordinate from the source
         * until it reaches the destination or if there is no more values to
         * update
         */
        if (!nextPossibleCoordinates.isEmpty() && !reachedDestination){
            manipulateArray(nextPossibleCoordinates, destination);
        }
    }

    /**
     * Converting the distance array into a list of coordinates which
     * represents a path
     *
     * @param startingCoordinate the source coordinate
     * @param destination the destination
     * @return
     */
    private List<Coordinate> backtrack(Coordinate startingCoordinate,
                                       Coordinate destination){
        /**
         * Start to track the path from the destination
         */
        int currentX = destination.x;
        int currentY = destination.y;
        List<Coordinate> path = new ArrayList<>();

        /**
         * adding the destination to the path
         */
        path.add(destination);

        /**
         * Backtracking from the destination, keep finding the path until the
         * source coordinate is reached
         */
        while(currentX != startingCoordinate.x ||
                currentY != startingCoordinate.y){

            /**
             * Checking the surroundings
             */
            for (int i = 0; i < NUM_OF_POSSIBLE_DIRECTION; i++) {

                /**
                 * One of the surroundings
                 */
                int nextX = currentX + DIRECTIONS_DELTA[i];
                int nextY = currentY +
                        DIRECTIONS_DELTA[(i+1)%NUM_OF_POSSIBLE_DIRECTION];

                /**
                 * Ignoring invalid values
                 */
                if(nextX < 0 || nextX >= World.MAP_WIDTH || nextY < 0
                        || nextY >= World.MAP_HEIGHT){
                    continue;
                }

                /**
                 * Should the value of the current coordinate be higher than
                 * the neighouring values, the path is added. As the
                 * difference of values is, at maximum, to be 1, any path
                 * chosen will be fine.
                 */
                if(distanceArray[currentY][currentX] >
                        distanceArray[nextY][nextX]){
                    path.add(new Coordinate(nextX, nextY));
                    /**
                     * Change the currently processed coordinate
                     */
                    currentX = nextX;
                    currentY = nextY;
                    break;
                }
            }

        }

        /**
         * as it was backtracked, the list has to be reversed
         */
        Collections.reverse(path);
        return path;
    }
}
