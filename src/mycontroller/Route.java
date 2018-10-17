package mycontroller;
import java.util.ArrayList;
import java.util.HashMap;

import tiles.MapTile;
import tiles.TrapTile;
import utilities.Coordinate;
import world.World;
import world.WorldSpatial;

public class Route {
    public int[][] gridMap = new int[World.MAP_WIDTH][World.MAP_HEIGHT];
    private HashMap<Coordinate,MapTile> map;
    private Coordinate exit;
    private static final String LAVA = "lava";
    private static final String HEALTH = "health";
    public static final int WALL = -1;
    public static final int TRAP_OR_ROAD = 100;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;

    public Route(HashMap<Coordinate, MapTile> map, String position) {
        String[] posStr = position.split(",");
        int initCarX = Integer.parseInt(posStr[0]);
        int initCarY = Integer.parseInt(posStr[1]);
        this.map = map;
        buildMap();
    }

    // Determines whether the give coordinate is a lava tile.
    public static boolean isLava(HashMap<Coordinate, MapTile> map, Coordinate coord){
        MapTile mapTile = map.get(coord);
        if(mapTile != null && mapTile.isType(MapTile.Type.TRAP)){
            TrapTile trapTile = (TrapTile) mapTile;

            return trapTile.getTrap().equals(LAVA);
        }

        return false;
    }

    // Determines whether the give coordinate is a health tile.
    public static boolean isHealth(HashMap<Coordinate, MapTile> map, Coordinate coord){
        MapTile mapTile = map.get(coord);
        if(mapTile != null && mapTile.isType(MapTile.Type.TRAP)){
            TrapTile trapTile = (TrapTile) mapTile;

            return trapTile.getTrap().equals(HEALTH);
        }

        return false;
    }

    // build a new form of map to store the tile information
    public void buildMap(){
        for(Coordinate coord: map.keySet()){
            MapTile currLoc = map.get(coord);

            // if current loc is WALL, then mark this grid as WALL
            if(currLoc.isType(MapTile.Type.WALL)){
                gridMap[coord.x][coord.y] = WALL;
            }

            // do same thing with EXIT
            else if(currLoc.isType(MapTile.Type.FINISH)){
                this.exit = coord;
            }
            else{
                gridMap[coord.x][coord.y] = TRAP_OR_ROAD;
            }
        }
    }

    //check if the point is in the map
    public boolean withinMap(int x, int y, int[][] map){
        if(x < 0 || x >= map.length){
            return false;
        }
        if(y < 0 || y >= map[0].length){
            return false;
        }
        return true;
    }

    // update the map based on the location (useful for deciding which direction to go)
    public void updateMap(int x, int y, int value){
        this.buildMap();
        if(!withinMap(x, y, gridMap) || gridMap[x][y] == WALL || gridMap[x][y] < value){
            return;
        }
        // then set the gridMap's value into given value (which is 0 in this case)
        gridMap[x][y] = value;
        updateMap(x-1, y, value+1);
        updateMap(x+1, y, value+1);
        updateMap(x, y-1, value+1);
        updateMap(x, y+1, value+1);
    }

    // check if there is a wall on which side.
    public int checkWall(WorldSpatial.Direction direction, int x, int y){
        switch (direction){
            case EAST:
                if(gridMap[x][y+1] == WALL){
                    return LEFT;
                }
                else if (gridMap[x][y-1] == WALL) {
                    return RIGHT;
                }
                break;

            case WEST:
                if(gridMap[x][y-1] == WALL){
                    return LEFT;
                }
                else if (gridMap[x][y+1] == WALL){
                    return RIGHT;
                }
                break;

            case NORTH:
                if(gridMap[x-1][y] == WALL){
                    return LEFT;
                }
                else if (gridMap[x+1][y] == WALL){
                    return RIGHT;
                }
                break;

            case SOUTH:
                if(gridMap[x+1][y] == WALL){
                    return LEFT;
                }
                else if (gridMap[x-1][y] == WALL){
                    return RIGHT;
                }
                break;
        }
        return 0;
    }

    // based on the lowest value, choose the next direction of the car
    public WorldSpatial.Direction nextDirection(int carX, int carY){
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
            int current = gridMap[coord.x][coord.y];
            if(current < value && current != WALL){
                // then update
                value = current;
                lowestCoord = new Coordinate(coord.x, coord.y);
            }
        }
        // check which direction to go next
        if(lowestCoord.x < carX){
            return WorldSpatial.Direction.WEST;
        }
        else if(lowestCoord.x > carX){
            return WorldSpatial.Direction.EAST;
        }
        else if(lowestCoord.y < carY){
            return WorldSpatial.Direction.SOUTH;
        }
        else if(lowestCoord.y > carY){
            return WorldSpatial.Direction.NORTH;
        }
        return null;
    }
}
