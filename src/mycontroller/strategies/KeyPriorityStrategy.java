package mycontroller.strategies;

import mycontroller.Route;
import mycontroller.states.*;
import utilities.Coordinate;

public class KeyPriorityStrategy implements StrategyFactory {

    private Route route;
    private State explore, heal, getKey, exit;

    public KeyPriorityStrategy(Route route) {
        this.route = route;
        this.explore = new ExplorationState(this.route.getGridMap());
//        this.exit = new ExitingState();
//        this.getKey = new GettingKeyState();
//        this.heal = new HealingState();
//
    }

    @Override
    public Coordinate decideNextCoordinate(Coordinate currentCoordinate) {

        return explore.getNearestCoordinate(currentCoordinate);
    }


}
