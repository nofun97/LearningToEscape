package mycontroller;

import tiles.MapTile;
import utilities.Coordinate;

import java.util.HashMap;


/**
 * The interface Strategy algorithm calculates the best point to go to for the
 * car.
 */
public interface StrategyFactory {
    /**
     * Decide next tile coordinate.
     *
     * @param map             the map
     * @return the coordinate
     */
    Coordinate decideNextTile(HashMap<Coordinate, MapTile> map);

}
