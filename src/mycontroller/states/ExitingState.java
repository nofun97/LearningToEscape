package mycontroller.states;
import mycontroller.Route;
import mycontroller.strategies.MyStrategy;
import utilities.Coordinate;
import world.Car;

//package mycontroller.states;
//
//import utilities.Coordinate;
//
//public class ExitingState implements State{
//    Coordinate exit;
//
//    public ExitingState() {
//    }
//
//    @Override
//    public Coordinate getNearestCoordinate(Coordinate currentCoordinate) {
//        return exit;
//    }
//
//    @Override
//    public void addImportantCoordinate(Coordinate coordinate) {
//        exit = coordinate;
//    }
//}
public class ExitingState extends MyState{
    /**
     * constructor
     */
    public ExitingState(Coordinate coord, Car car, Route route, MyStrategy strategy) {
        super(coord, car, route, strategy);
    }
}