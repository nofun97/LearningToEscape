package mycontroller;

import utilities.Coordinate;
import world.WorldSpatial;

import java.util.List;

/**
 * The interface Path finder.
 */
public interface PathFinder {
    /**
     * Generate a sequence of points from the starting current coordinate to
     * the destination
     *
     * @param currentCoordinate the current coordinate
     * @param destination       the destination
     * @param orientation       the orientation of the car
     * @return the list
     */
    public List<Coordinate> findBestPath(Coordinate currentCoordinate,
                                         Coordinate destination,
                                         WorldSpatial.Direction orientation);
}
