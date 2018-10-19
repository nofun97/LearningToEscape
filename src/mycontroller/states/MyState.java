package mycontroller.states;

import mycontroller.Route;
import mycontroller.strategies.MyStrategy;
import utilities.Coordinate;
import world.Car;

public class MyState {
    protected Car car;
    protected Route route;
    protected Coordinate Target;
    protected MyStrategy strategy;
    protected int CarX;
    protected int CarY;

    public MyState(Coordinate coord, Car car, Route route, MyStrategy strategy) {
        this.Target = coord;
        this.car = car;
        this.route = route;
        this.strategy = strategy;
        updateMap();
    }

    public void updateMap() {
        route.updateMap(Target.x, Target.y);
    }

    // the finish condition
    public boolean finish() {
        if (CarY == Target.y && CarX == Target.x) {
            return true;
        }
        return false;
    }

    // get the position of the car
    public void getPosition() {
        String[] positionStr = car.getPosition().split(",");
        this.CarY = Integer.parseInt(positionStr[1]);
        this.CarX = Integer.parseInt(positionStr[0]);
    }

    public Coordinate moveTo() {
        return route.findUnexploredCoordinate(CarX, CarY);
    }

    // TODO: i think here should be a update method based on moveTO then decide which direction to go
}
