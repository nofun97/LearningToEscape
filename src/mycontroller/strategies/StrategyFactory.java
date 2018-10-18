package mycontroller.strategies;

import utilities.Coordinate;


/**
 * The interface Strategy algorithm calculates the best point to go to for the
 * car.
 */
public interface StrategyFactory {

    /**
     * Decide next tile coordinate.
     *
     * @return the coordinate
     * @param currentCoordinate
     */
    Coordinate decideNextCoordinate(Coordinate currentCoordinate);

}
