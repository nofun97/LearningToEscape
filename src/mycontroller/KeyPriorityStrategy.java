package mycontroller;

import utilities.Coordinate;

import java.util.ArrayList;

public class KeyPriorityStrategy implements StrategyFactory {

    private ArrayList<Coordinate> KeyLocations;
    private ArrayList<Coordinate> HealingLocations;
    private Route route;

    public KeyPriorityStrategy(Route route) {
        this.route = route;
    }

    @Override
    public Coordinate decideNextCoordinate() {

        return null;
    }


}
