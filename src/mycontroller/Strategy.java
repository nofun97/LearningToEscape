package mycontroller;

import tiles.MapTile;
import utilities.Coordinate;
import world.Car;

import java.util.ArrayList;
import java.util.HashMap;

public class Strategy implements StrategyFactory {
    private Car car;
    private ArrayList<Coordinate> KeyLocations;



    @Override
    public Coordinate decideNextTile(HashMap<Coordinate, MapTile> map) {
        return null;
    }
}
