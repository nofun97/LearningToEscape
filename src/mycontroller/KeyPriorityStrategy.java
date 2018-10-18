package mycontroller;

import tiles.MapTile;
import utilities.Coordinate;

import java.util.ArrayList;
import java.util.HashMap;

public class KeyPriorityStrategy implements StrategyFactory {

    private ArrayList<Coordinate> KeyLocations;
    private ArrayList<Coordinate> HealingLocations;
    private Route route;

    public KeyPriorityStrategy(Route route) {
        this.route = route;
    }

    @Override
    public Coordinate decideNextTile(HashMap<Coordinate, MapTile> map) {

        return null;
    }
}
