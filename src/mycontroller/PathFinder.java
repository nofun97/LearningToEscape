package mycontroller;

import utilities.Coordinate;

import java.util.List;

public interface PathFinder {
    public List<Coordinate> findBestPath(Coordinate currentCoordinate,
                                         Coordinate destination);
}
