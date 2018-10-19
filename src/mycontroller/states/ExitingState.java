package mycontroller.states;

import utilities.Coordinate;

public class ExitingState implements State{
    Coordinate exit;

    public ExitingState() {
    }

    @Override
    public Coordinate getCoordinate(Coordinate currentCoordinate) {
        return exit;
    }

    @Override
    public void addImportantCoordinate(Coordinate coordinate) {
        exit = coordinate;
    }
}
