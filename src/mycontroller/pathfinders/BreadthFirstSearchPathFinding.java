package mycontroller.pathfinders;

import mycontroller.Route;
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
            int possibleX1 = currentCoordinate.x + 1;
            int possibleX2 = currentCoordinate.x - 1;
            int possibleY = currentCoordinate.y;
            if(Route.isWithinMap(possibleX1, possibleY) &&
                    !route.isBlocked(possibleX1, possibleY)) {

                distanceArray[possibleY][possibleX1] = DISTANCE;
                initialPossibleCoordinates.add(
                        new Integer[]{possibleX1, possibleY});
            }

            if(Route.isWithinMap(possibleX2, possibleY) &&
                    !route.isBlocked(possibleX2, possibleY)) {

                distanceArray[possibleY][possibleX2] = DISTANCE;
                initialPossibleCoordinates.add(
                        new Integer[]{possibleX2, possibleY});
            }

        } else if (orientation == WorldSpatial.Direction.SOUTH ||
                        orientation == WorldSpatial.Direction.NORTH){
            int possibleY1 = currentCoordinate.y + 1;
            int possibleY2 = currentCoordinate.y - 1;
            int possibleX = currentCoordinate.x;

            if(Route.isWithinMap(possibleX, possibleY1) &&
                    !route.isBlocked(possibleX, possibleY1)) {

                distanceArray[possibleY1][possibleX] = DISTANCE;
                initialPossibleCoordinates.add(
                        new Integer[]{possibleX, possibleY1});
            }

            if(Route.isWithinMap(possibleX, possibleY2) &&
                    !route.isBlocked(possibleX, possibleY2)) {

                distanceArray[possibleY2][possibleX] = DISTANCE;
                initialPossibleCoordinates.add(
                        new Integer[]{possibleX, possibleY2});
            }

        }

        /**
         * Manipulate array to calculate best path
         */
//        System.out.println(initialPossibleCoordinates.size());
        manipulateArray(initialPossibleCoordinates, destination);

        if (!isReachable(destination)){
            return UNREACHABLE;
        }

//        printDistArray();
        return backtrack(currentCoordinate, destination);
        /*List<Coordinate> x = backtrack(currentCoordinate, destination);
//        for (Coordinate a :
//                x) {
//            System.out.println(a.toString());
//        }
        return x;*/
    }

    private boolean isReachable(Coordinate coordinate) {
        return distanceArray[coordinate.y][coordinate.x] != Integer.MAX_VALUE;
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
            int sourceX = coordinate[0];
            int sourceY = coordinate[1];

            /**
             * Update the surrounding values should it fulfill the conditions
             */
            for (int i = 0; i < NUM_OF_POSSIBLE_DIRECTION; i++) {

                /**
                 * One of the surrounding coordinates to update
                 */
                int nextX = sourceX + DIRECTIONS_DELTA[i];
                int nextY = sourceY +
                        DIRECTIONS_DELTA[(i+1)%NUM_OF_POSSIBLE_DIRECTION];

                /**
                 * Ignoring invalid values
                 */
                if(!Route.isWithinMap(nextX, nextY)){
                    continue;
                }

                /**
                 * Update the distance values should a faster way to reach there
                 * is faster and if it is not a wall
                 */
                if(!route.isBlocked(nextX, nextY) &&
                        distanceArray[sourceY][sourceX] + DISTANCE <
                                distanceArray[nextY][nextX]){

                    distanceArray[nextY][nextX] =
                            distanceArray[sourceY][sourceX] + DISTANCE;
                    nextPossibleCoordinates.add(new Integer[]{nextX, nextY});
                }
            }

            /**
             * Reaching a destination means that the algorithm should stop
             */
            if(sourceX == destination.x && sourceY == destination.y){
                reachedDestination = true;
                break;
            }
        }
//        printDistArray();
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
//            System.out.printf("%2d %2d\n", currentX, currentY);
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
//                System.out.println("HERE");
                /**
                 * Should the value of the current coordinate be higher than
                 * the neighouring values, the path is added. As the
                 * difference of values is, at maximum, to be 1, any path
                 * chosen will be fine.
                 */
                if(distanceArray[currentY][currentX] -
                        distanceArray[nextY][nextX] == DISTANCE){
                    path.add(new Coordinate(nextX, nextY));
//                    System.out.printf("%d %d\n", currentX, currentY);
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

    private void printDistArray(){
        for (int i = World.MAP_HEIGHT - 1; i >= 0; i--) {
            int[] x = distanceArray[i];
            for (int y :
                    x) {
                if (y == Integer.MAX_VALUE){
                    System.out.print("-- ");
                } else{
                    System.out.printf("%2d ", y);
                }
            }
            System.out.printf("\n");
        }
        System.out.printf("\n");
    }
}
