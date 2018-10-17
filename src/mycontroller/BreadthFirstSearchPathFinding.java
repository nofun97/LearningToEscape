package mycontroller;

import utilities.Coordinate;
import world.World;
import world.WorldSpatial;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BreadthFirstSearchPathFinding implements PathFinder{
    public static final int DISTANCE = 1;
    public static final int NUM_OF_POSSIBLE_DIRECTION = 4;
    public static final int[] DIRECTIONS_DELTA = new int[]{0, 1, 0, -1};
    private int[][] distanceArray;
    private Route route;

    public BreadthFirstSearchPathFinding(Route route) {
        distanceArray = new int[World.MAP_HEIGHT][World.MAP_WIDTH];
        this.route = route;
    }

    @Override
    public List<Coordinate> findBestPath(Coordinate currentCoordinate,
                                         Coordinate destination,
                                         WorldSpatial.Direction orientation) {

        for (int i = 0; i < World.MAP_HEIGHT; i++) {
            for (int j = 0; j < World.MAP_WIDTH; j++) {
                distanceArray[i][j] = Integer.MAX_VALUE;
            }
        }

        distanceArray[currentCoordinate.y][currentCoordinate.x] = 0;
        ArrayList<Integer[]> initialPossibleCoordinates = new ArrayList<>();

        /**
         * based on the orientation of the car, defines where the next possible
         * coordinates
         */

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

    private void manipulateArray(ArrayList<Integer[]> possibleCoordinates,
                                 Coordinate destination){
        boolean reachedDestination = false;
        ArrayList<Integer[]> nextPossibleCoordinates = new ArrayList<>();
        for (Integer[] coordinate: possibleCoordinates){
            int x = coordinate[0];
            int y = coordinate[1];

            for (int i = 0; i < NUM_OF_POSSIBLE_DIRECTION; i++) {
                int nextX = x + DIRECTIONS_DELTA[i];
                int nextY = y +
                        DIRECTIONS_DELTA[(i+1)%NUM_OF_POSSIBLE_DIRECTION];

                if(nextX < 0 || nextX >= World.MAP_WIDTH || nextY < 0
                        || nextY >= World.MAP_HEIGHT){
                    continue;
                }

                if(route.gridMap[nextY][nextX] != Route.WALL &&
                        distanceArray[y][x] + DISTANCE <
                                distanceArray[nextY][nextX]){
                    distanceArray[nextY][nextX] =
                            distanceArray[y][x] + DISTANCE;
                    nextPossibleCoordinates.add(new Integer[]{nextX, nextY});
                }
            }
            if(x == destination.x && y == destination.y){
                reachedDestination = true;
                break;
            }
        }

        if (!nextPossibleCoordinates.isEmpty() && !reachedDestination){
            manipulateArray(nextPossibleCoordinates, destination);
        }
    }

    private List<Coordinate> backtrack(Coordinate startingCoordinate,
                                       Coordinate destination){
        int currentX = destination.x;
        int currentY = destination.y;
        List<Coordinate> path = new ArrayList<>();
        System.out.println(startingCoordinate.toString());
        path.add(destination);
        while(currentX != startingCoordinate.x ||
                currentY != startingCoordinate.y){
            for (int i = 0; i < NUM_OF_POSSIBLE_DIRECTION; i++) {
                int nextX = currentX + DIRECTIONS_DELTA[i];
                int nextY = currentY +
                        DIRECTIONS_DELTA[(i+1)%NUM_OF_POSSIBLE_DIRECTION];

                if(nextX < 0 || nextX >= World.MAP_WIDTH || nextY < 0
                        || nextY >= World.MAP_HEIGHT){
                    continue;
                }

                if(distanceArray[currentY][currentX] -
                        distanceArray[nextY][nextX] == DISTANCE){
                    path.add(new Coordinate(nextX, nextY));

                    currentX = nextX;
                    currentY = nextY;
                    break;
                }
            }

        }
        Collections.reverse(path);
        return path;
    }
}
