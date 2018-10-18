package mycontroller.states;

import utilities.Coordinate;

/**
 * The interface State.
 */
public interface State {

    /**
     * Gets nearest coordinate based on the state.
     *
     * @param currentCoordinate the current coordinate
     * @return the nearest coordinate
     */
    Coordinate getNearestCoordinate(Coordinate currentCoordinate);


    /**
     * Add important coordinate to each state.
     *
     * @param coordinate the coordinate
     */
    void addImportantCoordinate(Coordinate coordinate);
}
