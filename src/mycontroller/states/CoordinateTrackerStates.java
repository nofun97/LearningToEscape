package mycontroller.states;

import mycontroller.Route;
import mycontroller.pathfinders.PathFinder;
import utilities.Coordinate;
import world.WorldSpatial;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class CoordinateTrackerStates implements State{

    private PathFinder pathFinder;
    private Route route;
    private Set<Coordinate> importantCoordinates;
    private Set<Coordinate> coordinatesHistory;

    public CoordinateTrackerStates(PathFinder pathFinder, Route route) {
        this.pathFinder = pathFinder;
        this.route = route;
        this.importantCoordinates = new HashSet<>();
        this.coordinatesHistory = new HashSet<>();
    }


    @Override
    public Coordinate getCoordinate(Coordinate currentCoordinate, WorldSpatial.Direction orientation) {
        assert importantCoordinates != null;
//        System.out.println(importantCoordinates);
        List<Coordinate> unreachableCoordinates = new ArrayList<>();
        Coordinate nearestCoordinate =
                pathFinder.findNearestCoordinate(
                        new ArrayList<>(importantCoordinates),
                        currentCoordinate, orientation,
                        unreachableCoordinates);

        for (Coordinate coordinate: unreachableCoordinates)
            importantCoordinates.remove(coordinate);


//        importantCoordinates.remove(nearestCoordinate);
        return nearestCoordinate;
    }

    @Override
    public boolean offerImportantCoordinate(Coordinate coordinate) {
//        System.out.println("DID U DIE");

        if(!coordinatesHistory.contains(coordinate)){
            coordinatesHistory.add(coordinate);
            importantCoordinates.add(coordinate);
            return true;
        }
        return false;
    }

    @Override
    public abstract boolean isFinished();

    @Override
    public int getSize(){return importantCoordinates.size();}

    @Override
    public void removeCoordinate(Coordinate coordinate) {
        if(!importantCoordinates.contains(coordinate)) return;
        importantCoordinates.remove(coordinate);
    }

}
