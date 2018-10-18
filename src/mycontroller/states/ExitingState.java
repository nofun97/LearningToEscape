package mycontroller.states;

import utilities.Coordinate;

public class ExitingState implements State{
    Coordinate exit;

    public ExitingState() {
    }

    @Override
    public Coordinate getNearestCoordinate(Coordinate currentCoordinate) {
        return exit;
    }

    @Override
    public void addImportantCoordinate(Coordinate coordinate) {
        exit = coordinate;
    }
}
