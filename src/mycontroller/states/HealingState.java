package mycontroller.states;

import mycontroller.MyAIController;
import mycontroller.Route;
import mycontroller.strategies.MyStrategy;
import utilities.Coordinate;
import world.Car;

import java.util.ArrayList;

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
public class HealingState extends MyState{

    public HealingState(Coordinate coordinate, Car car, Route route, MyStrategy strategy) {
        super(coordinate, car, route, strategy);
    }


    //test is the healing of the car is completed
    @Override
    public boolean finish() {
        if (car.getHealth() >= MyAIController.healthLimit) {
            return true;
        }
        return false;
    }
    // update healing
    @Override
    public void update() {
        //TODO: if finish healing, just continue exploring, otherwise just update
    }
}