package mycontroller.states;

import mycontroller.MyAIController;
import mycontroller.Route;
import mycontroller.pathfinders.PathFinder;
//import mycontroller.strategies.MyStrategy;
import utilities.Coordinate;
import world.Car;

//public class HealingState implements State {
//    private ArrayList<Coordinate> healingTiles;
//
//    @Override
//    public Coordinate getNearestCoordinate(Coordinate currentCoordinate) {
//
//
//        return null;
//    }
//
//    @Override
//    public void addImportantCoordinate(Coordinate coordinate) {
//        healingTiles.add(coordinate);
//    }
//
//
//}
public class HealingState extends CoordinateTrackerStates {
    private Car car;
    public static int MAXIMUM_HEALTH_TO_HEAL = 100;
    public HealingState(PathFinder pathFinder, Route route, Car car) {
        super(pathFinder, route);
        this.car = car;
    }

    //test is the healing of the car is completed



    @Override
    public boolean isFinished() {
        return car.getHealth() == MAXIMUM_HEALTH_TO_HEAL;
    }
}