package mycontroller.strategies;

import mycontroller.Route;
import mycontroller.states.*;
import utilities.Coordinate;

public class KeyPriorityStrategy implements StrategyFactory {

    private Route route;
    private Coordinate lastCoordinate;
    private State explore, heal, getKey, exit;

    public KeyPriorityStrategy(Route route) {
        this.route = route;
        this.explore = new ExplorationState(this.route.getGridMap());

        this.lastCoordinate = new Coordinate(-1, -1);
//        this.exit = new ExitingState();
//        this.getKey = new GettingKeyState();
//        this.heal = new HealingState();
//
    }

    @Override
    public Coordinate decideNextCoordinate(Coordinate currentCoordinate) {
        State currentState = explore;
        Coordinate nextCoordinate =
                currentState.getNearestCoordinate(currentCoordinate);

        /*if (lastCoordinate == nextCoordinate){
            route.blockCoordinate(lastCoordinate.x, lastCoordinate.y);
            nextCoordinate =
                    currentState.getNearestCoordinate(currentCoordinate);
        }
        recordCoordinate(nextCoordinate);*/

        return nextCoordinate;
    }

    public void recordCoordinate(Coordinate newCoordinate){
        this.lastCoordinate = newCoordinate;
    }

//    public void checkUnreachableCoordinate(Coordinate newCoordinate){
//        if (newCoordinate == lastCoordinate){
//            route.blockCoordinate(lastCoordinate.x, lastCoordinate.y);
//        }
//    }
}
