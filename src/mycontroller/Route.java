package mycontroller;
import java.util.HashMap;
import java.util.*;
import tiles.MapTile;
import utilities.Coordinate;
import world.Car;
import world.World;
import world.WorldSpatial;

public class Route {
    private HashMap<Coordinate,MapTile> map;
    private Coordinate exit;

    public Route(HashMap<Coordinate, MapTile> map) {
        this.map = map;
    }
}
