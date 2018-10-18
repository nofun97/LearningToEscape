package mycontroller;
import java.util.ArrayList;
import java.util.HashMap;

import mycontroller.pathfinders.PathFinder;
import tiles.MapTile;
import utilities.Coordinate;
import world.World;
import world.WorldSpatial;

public class Route {
    private int[][] gridMap = new int[World.MAP_HEIGHT][World.MAP_WIDTH];
    private HashMap<Coordinate,MapTile> map;
    private Coordinate exit;
    private PathFinder pathFinder;
    private static final String LAVA = "lava";
    private static final String HEALTH = "health";
    public static final int BLOCKED = -1;
    public static final int TRAP_OR_ROAD = 0;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;

    public Route(HashMap<Coordinate, MapTile> map, String position) {
        String[] posStr = position.split(",");
        int initCarX = Integer.parseInt(posStr[0]);
        int initCarY = Integer.parseInt(posStr[1]);
        this.map = map;
        buildMap();
    }

    // TODO: dont think this part should be this class, but it useful
    // Determines whether the give coordinate is a lava tile.
//    public static boolean isLava(HashMap<Coordinate, MapTile> map, Coordinate coord){
//        MapTile mapTile = map.get(coord);
//        if(mapTile != null && mapTile.isType(MapTile.Type.TRAP)){
//            TrapTile trapTile = (TrapTile) mapTile;
//
//            return trapTile.getTrap().equals(LAVA);
//        }
//
//        return false;
//    }
//
//    // Determines whether the give coordinate is a health tile.
//    public static boolean isHealth(HashMap<Coordinate, MapTile> map, Coordinate coord){
//        MapTile mapTile = map.get(coord);
//        if(mapTile != null && mapTile.isType(MapTile.Type.TRAP)){
//            TrapTile trapTile = (TrapTile) mapTile;
//
//            return trapTile.getTrap().equals(HEALTH);
//        }
//
//        return false;
//    }

    // build a new form of map to store the tile information
    public void buildMap(){
        for(Coordinate coord: map.keySet()){
            MapTile currLoc = map.get(coord);

            // if current loc is BLOCKED, then mark this grid as BLOCKED
            if(currLoc.isType(MapTile.Type.WALL)){
                gridMap[coord.y][coord.x] = BLOCKED;
            }

            // do same thing with EXIT
            else if(currLoc.isType(MapTile.Type.FINISH)){
                this.exit = coord;
            }
            else{
                gridMap[coord.y][coord.x] = TRAP_OR_ROAD;
            }
        }
    }

    //check if the coordinate is in the map
    public static boolean isWithinMap(int x, int y){

        return !(x < 0 || x >= World.MAP_WIDTH || y < 0
                || y >= World.MAP_HEIGHT);
    }

    /*// update the map based on the location (useful for deciding which direction to go)
    public void updateMap(int x, int y, int value){
        this.buildMap();
        if(!isWithinMap(x, y) || gridMap[x][y] == BLOCKED || gridMap[x][y] < value){
            return;
        }
        // then set the gridMap's value into given value (which is 0 in this case)
        gridMap[y][x] = value;
        for (int i = 0; i < PathFinder.NUM_OF_POSSIBLE_DIRECTION; i++) {
            int nextX = x + PathFinder.DIRECTIONS_DELTA[i];
            int nextY = y + PathFinder.DIRECTIONS_DELTA
                    [(i+1)%PathFinder.NUM_OF_POSSIBLE_DIRECTION];

            if(!isBlocked(nextX, nextY)) gridMap[nextY][nextX] = value + 1;
        }
    }*/

    //TODO This is a crude updateMap, will need more fixing
    public void updateMap(int x, int y){
//        this.buildMap();
        if(!isWithinMap(x, y) || gridMap[y][x] == BLOCKED){
            return;
        }
        // then set the gridMap's value into given value (which is 0 in this case)
        gridMap[y][x] += 1;
        for (int i = 0; i < PathFinder.NUM_OF_POSSIBLE_DIRECTION; i++) {
            int nextX = x + PathFinder.DIRECTIONS_DELTA[i];
            int nextY = y + PathFinder.DIRECTIONS_DELTA
                    [(i+1)%PathFinder.NUM_OF_POSSIBLE_DIRECTION];

            if(!isBlocked(nextX, nextY)) gridMap[nextY][nextX] += 1;
        }
    }

    // check if there is a wall on which side.
    public int checkWall(WorldSpatial.Direction direction, int x, int y){
        switch (direction){
            case EAST:
                if(gridMap[x][y+1] == BLOCKED){
                    return LEFT;
                }
                else if (gridMap[x][y-1] == BLOCKED) {
                    return RIGHT;
                }
                break;

            case WEST:
                if(gridMap[x][y-1] == BLOCKED){
                    return LEFT;
                }
                else if (gridMap[x][y+1] == BLOCKED){
                    return RIGHT;
                }
                break;

            case NORTH:
                if(gridMap[x-1][y] == BLOCKED){
                    return LEFT;
                }
                else if (gridMap[x+1][y] == BLOCKED){
                    return RIGHT;
                }
                break;

            case SOUTH:
                if(gridMap[x+1][y] == BLOCKED){
                    return LEFT;
                }
                else if (gridMap[x-1][y] == BLOCKED){
                    return RIGHT;
                }
                break;
        }
        return 0;
    }

    // based on the lowest value, choose the next direction of the car
    public Coordinate findUnexploredCoordinate(int carX, int carY){
        // use array list to store four coordinates surrounding the car
        ArrayList<Coordinate> surrCoord = new ArrayList<>();
        // add the current position's surrounding coordinates
        surrCoord.add(new Coordinate(carX-1, carY));
        surrCoord.add(new Coordinate(carX+1, carY));
        surrCoord.add(new Coordinate(carX, carY-1));
        surrCoord.add(new Coordinate(carX, carY+1));

        // set a maximum value for comparison
        int value = Integer.MAX_VALUE;
        Coordinate lowestCoord = null;

        // find the minimum value of surrounding grid, this grid is the one which
        // hasn't explored or less explore.
        for(Coordinate coord : surrCoord){
            int current = gridMap[coord.y][coord.x];
            if(current < value && !isBlocked(coord.x, coord.y)){
                // then update
                value = current;
                lowestCoord = new Coordinate(coord.x, coord.y);
            }
        }

        return lowestCoord;
    }


    public boolean isBlocked(int x, int y){
        return gridMap[y][x] == BLOCKED;
    }

    public int[][] getGridMap() {
        return gridMap;
    }

}


// TODO: one part left, how to know the search finished???