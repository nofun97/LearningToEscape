package mycontroller.states;

import mycontroller.Route;
import mycontroller.strategies.MyStrategy;
import utilities.Coordinate;
import world.Car;

import java.util.ArrayList;

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
public class GettingKeyState extends MyState{
    public GettingKeyState(Coordinate coordinate, Car car, Route route, MyStrategy strategy) {
        super(coordinate, car, route, strategy);
    }

    @Override
    public void update(float delta) {
        //TODO: if finish find a key, just updateKeyList in MyStrategy, and keep exploring
        //TODO: otherwise just update the direction we choose
    }
}