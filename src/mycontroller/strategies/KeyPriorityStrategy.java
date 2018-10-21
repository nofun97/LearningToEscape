package mycontroller.strategies;

import mycontroller.Route;
import mycontroller.pathfinders.PathFinder;
import mycontroller.states.*;
import utilities.Coordinate;
import world.Car;

public class KeyPriorityStrategy implements StrategyFactory {


    private boolean healCommences = false;
    private Route route;
//    private Coordinate lastCoordinate;
    private State explore, heal, getKey, exit;
    private Car car;

    public KeyPriorityStrategy(Route route, Car car, PathFinder pathFinder) {
        this.route = route;
        this.car = car;
        this.explore = new ExplorationState(this.route);
        this.heal = new HealingState(pathFinder, route, car);
        this.getKey = new GettingKeyState(pathFinder, route);
//        this.lastCoordinate = new Coordinate(-1, -1);
        this.exit = new ExitingState(pathFinder, route);
//        this.getKey = new GettingKeyState();
//        this.heal = new HealingState();
//
    }

    @Override
    public Coordinate decideNextCoordinate(Coordinate currentCoordinate) {
        State currentState = null;
        if(healCommences){
            currentState = heal;
            if(heal.isFinished()){
              healCommences = false;
          }
        } else if(car.getKeys().size() == car.numKeys && exit.getSize() > 0){
            System.out.println("Exiting");
            currentState = exit;
        } else if (!healCommences &&
                car.getHealth() <= MINIMUM_HEALTH && heal.getSize() > 0){
            System.out.println("Healing");
            currentState = heal;
            healCommences = true;
        } else if (getKey.getSize() > 0){
            System.out.println("Get Keys");
            currentState = getKey;
        } else if (!healCommences){
            System.out.println("Dora Dora Dora the Explorer");
            currentState = explore;
        }

        assert currentState != null;
        Coordinate nextCoordinate =
                currentState.getCoordinate(currentCoordinate,
                        car.getOrientation());
//        System.out.println(nextCoordinate.toString());
/*      if (lastCoordinate == nextCoordinate){
            route.blockCoordinate(lastCoordinate.x, lastCoordinate.y);
            nextCoordinate =
                    currentState.getCoordinate(currentCoordinate, null);
        }
        recordCoordinate(nextCoordinate);*/
//        assert nextCoordinate != null;
//        System.out.println(nextCoordinate.toString());
        return nextCoordinate;
    }

    @Override
    public void updateData(Coordinate coordinate, IMPORTANT_DATA type) {
        State trackerStates = null;
        switch (type){
            case KEY:
                trackerStates = getKey;
                break;
            case HEALING:
                trackerStates = heal;
                break;
            case EXIT:
                trackerStates = exit;
                break;
        }
        assert trackerStates != null;
        trackerStates.addImportantCoordinate(coordinate);
    }

//    public void recordCoordinate(Coordinate newCoordinate){
//        this.lastCoordinate = newCoordinate;
//    }

//    public void checkUnreachableCoordinate(Coordinate newCoordinate){
//        if (newCoordinate == lastCoordinate){
//            route.blockCoordinate(lastCoordinate.x, lastCoordinate.y);
//        }
//    }
}
