package mycontroller.states;

import mycontroller.Route;
import mycontroller.pathfinders.PathFinder;
import org.lwjgl.Sys;
import utilities.Coordinate;
import world.WorldSpatial;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class CoordinateTrackerStates implements State{
    /*protected Car car;
    protected Route route;
    protected Coordinate Target;
    protected MyStrategy strategy;
    protected int CarX;
    protected int CarY;*/

    private PathFinder pathFinder;
    private Route route;
    private Set<Coordinate> importantCoordinates;

    /*public CoordinateTrackerStates(Coordinate coord, Car car, Route route, MyStrategy strategy) {
        this.Target = coord;
        this.car = car;
        this.route = route;
        this.strategy = strategy;
        updateMap();
    }*/

    public CoordinateTrackerStates(PathFinder pathFinder, Route route) {
        this.pathFinder = pathFinder;
        this.route = route;
        this.importantCoordinates = new HashSet<>();
    }


    @Override
    public Coordinate getCoordinate(Coordinate currentCoordinate, WorldSpatial.Direction orientation) {
        assert importantCoordinates != null;
//        System.out.println(importantCoordinates);

        Coordinate nearestCoordinate =
                pathFinder.findNearestCoordinate(
                        new ArrayList<>(importantCoordinates),
                        currentCoordinate, orientation);
//        importantCoordinates.remove(nearestCoordinate);
        return nearestCoordinate;
    }

    @Override
    public void addImportantCoordinate(Coordinate coordinate) {
//        System.out.println("DID U DIE");
        importantCoordinates.add(coordinate);
    }

    @Override
    public abstract boolean isFinished();

    @Override
    public int getSize(){return importantCoordinates.size();}

    public void removeCoordinate(Coordinate coordinate) {
        importantCoordinates.remove(coordinate);
    }
}
