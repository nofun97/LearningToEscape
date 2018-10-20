package mycontroller.states;

import mycontroller.Route;
import mycontroller.pathfinders.PathFinder;
//import mycontroller.strategies.MyStrategy;
import utilities.Coordinate;
import world.Car;
import world.WorldSpatial;

import java.util.HashSet;
import java.util.Set;

//public class GettingKeyState implements State {
//
//    private ArrayList<Coordinate> keyList;
//
//    public GettingKeyState() {
//        this.keyList = new ArrayList<>();
//    }
//
//    @Override
//    public Coordinate getNearestCoordinate(Coordinate currentCoordinate) {
//        return null;
//    }
//
//    @Override
//    public void addImportantCoordinate(Coordinate coordinate) {
//        keyList.add(coordinate);
//    }
//}
public class GettingKeyState extends CoordinateTrackerStates {

    private Set<Coordinate> keyHistory;

    public GettingKeyState(PathFinder pathFinder, Route route) {
        super(pathFinder, route);
        keyHistory = new HashSet<>();
    }

    @Override
    public boolean isFinished() {
        return getSize() == 0;
    }

    @Override
    public void addImportantCoordinate(Coordinate coordinate) {
        if(!keyHistory.contains(coordinate)){
            keyHistory.add(coordinate);
            super.addImportantCoordinate(coordinate);
        }
    }

    @Override
    public Coordinate getCoordinate(Coordinate currentCoordinate, WorldSpatial.Direction orientation) {
        Coordinate nearestCoordinate = super.getCoordinate(currentCoordinate,
                orientation);
        removeCoordinate(nearestCoordinate);
        return nearestCoordinate;
    }
}