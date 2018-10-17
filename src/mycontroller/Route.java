package mycontroller;
import java.util.HashMap;

import tiles.MapTile;
import tiles.TrapTile;
import utilities.Coordinate;
import world.World;
import world.WorldSpatial;

public class Route {
    public int[][] gridMap = new int[World.MAP_HEIGHT][World.MAP_WIDTH];
    private HashMap<Coordinate,MapTile> map;
    private Coordinate exit;
    private PathFinder pathFinder;
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
//        buildMap();
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
                gridMap[coord.y][coord.x] = WALL;
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
}
