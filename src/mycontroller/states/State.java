package mycontroller.states;

import utilities.Coordinate;
import world.WorldSpatial;

/**
 * The interface State.
 */
public interface State {


    /**
     * Gets nearest coordinate based on the state.
     *
     * @param currentCoordinate the current coordinate
     * @param orientation the orientation
     * @return the nearest coordinate
     */
    Coordinate getCoordinate(Coordinate currentCoordinate,
                             WorldSpatial.Direction orientation);


    /**
     * Add important coordinate to each state.
     *
     * @param coordinate the coordinate
     */
    boolean offerImportantCoordinate(Coordinate coordinate);

    void removeCoordinate(Coordinate coordinate);

    boolean isFinished();

    int getSize();
}
