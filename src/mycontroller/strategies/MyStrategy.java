/*
package mycontroller.strategies;
import java.util.*;

import mycontroller.Route;
import mycontroller.states.GettingKeyState;
import mycontroller.states.HealingState;
import mycontroller.states.CoordinateTrackerStates;
import utilities.Coordinate;
import world.Car;

public class MyStrategy {
    private Car car;
    private CoordinateTrackerStates state;
    private Route route;
    private ArrayList<Coordinate> keyList = new ArrayList<>();

    public MyStrategy(Car car, Route map, ArrayList<Coordinate> keyList) {
        this.car = car;
        this.route = map;
        this.keyList = keyList;
        exploreGraph(null);
    }

    // explore the graph according to the location of the importance mark point
    // TODO: and i have not touch the ExploreationState
    public void exploreGraph(Coordinate coordinate){
        if (route.searchFinished()){
            gettingKey(null);
            return;
        }
        if (coordinate == null){
            coordinate = route.nextMarkPoint();
        }
        // TODO: just give a new state which is ExploringState
    }

    public void gettingKey(Coordinate coordinate){
        if(coordinate == null){
            if(keyList.size() == 0){
                goExit();
            }
            coordinate = keyList.get(0);
        }
        this.state = new GettingKeyState(coordinate, car, route, this);
    }


    public void getHealing(Coordinate coordinate){
        if (this.state instanceof HealingState){
            return;
        }
        this.state = new HealingState(coordinate, car, route, this);
    }

    public void goExit(){
        Coordinate exit = route.getExit();
    }

    public void updateKeyList() {
        if (!keyList.isEmpty()) {
            keyList.remove(0);
        }
    }
}
*/
