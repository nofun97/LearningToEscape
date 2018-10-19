package mycontroller.strategies;

import utilities.Coordinate;

import java.util.ArrayList;


/**
 * The interface Strategy algorithm calculates the best point to go to for the
 * car.
 */
public interface StrategyFactory {

    ArrayList<Coordinate> keyList = new ArrayList<>();
    ArrayList<Coordinate> healCoordinate = new ArrayList<>();
    ArrayList<Coordinate> exitList = new ArrayList<>();

    /**
     * Decide next tile coordinate.
     *
     * @return the coordinate
     * @param currentCoordinate
     */
    Coordinate decideNextCoordinate(Coordinate currentCoordinate);

//    void updateData()
}
