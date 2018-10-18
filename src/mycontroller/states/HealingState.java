package mycontroller.states;

import utilities.Coordinate;

import java.util.ArrayList;

public class HealingState implements State {
    private ArrayList<Coordinate> healingTiles;

    @Override
    public Coordinate getNearestCoordinate(Coordinate currentCoordinate) {
        

        return null;
    }

    @Override
    public void addImportantCoordinate(Coordinate coordinate) {
        healingTiles.add(coordinate);
    }


}
