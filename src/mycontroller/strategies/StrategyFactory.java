package mycontroller.strategies;

import utilities.Coordinate;

import java.util.ArrayList;


/**
 * The interface Strategy algorithm calculates the best point to go to for the
 * car.
 */
public interface StrategyFactory {
    int MINIMUM_HEALTH = 50;
    enum IMPORTANT_DATA {KEY, HEALING, EXIT}
    /**
     * Decide next tile coordinate.
     *
     * @return the coordinate
     * @param currentCoordinate
     */
    Coordinate decideNextCoordinate(Coordinate currentCoordinate);

    void updateData(Coordinate coordinate, IMPORTANT_DATA type);
}
