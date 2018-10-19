package mycontroller.states;

import utilities.Coordinate;

import java.util.ArrayList;

public class GettingKeyState implements State {

    private ArrayList<Coordinate> keyList;

    public GettingKeyState() {
        this.keyList = new ArrayList<>();
    }

    @Override
    public Coordinate getCoordinate(Coordinate currentCoordinate) {
        return null;
    }

    @Override
    public void addImportantCoordinate(Coordinate coordinate) {
        keyList.add(coordinate);
    }
}
