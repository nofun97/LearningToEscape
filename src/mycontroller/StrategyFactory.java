package mycontroller;

import tiles.MapTile;
import utilities.Coordinate;
import world.World;

import java.util.HashMap;


/**
 * The interface Strategy algorithm calculates the best point to go to for the
 * car.
 */
public interface StrategyFactory {

    /**
     * Decide next tile coordinate.
     *
     * @return the coordinate
     */
    Coordinate decideNextCoordinate();

}
