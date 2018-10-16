package mycontroller;

import com.sun.xml.internal.bind.v2.TODO;
import controller.CarController;
import utilities.Coordinate;
import world.Car;

import java.util.ArrayList;
import java.util.HashMap;
import controller.CarController;
import tiles.HealthTrap;
import tiles.LavaTrap;
import tiles.MapTile;


public class MyAIController extends CarController{
	// to store how many keys already got
    private ArrayList<Coordinate> keyList = new ArrayList<>();
    private HashMap<Coordinate, MapTile> map = super.getMap();
    private Route route;

	public MyAIController(Car car) {
		super(car);
        route = new Route(map, super.getPosition());
	}

	/**
     * basic idea: view and gain the TRAP information
     * then use the info to recover the original map
     * finally use DFS/Dijkstra to get all keys then exit.
    */
	@Override
	public void update() {
		exploreMap(super.getView());
	}

	// explore and recover the original map based on the TRAP info received.
	public void exploreMap(HashMap<Coordinate, MapTile> knownMap){
	    for(Coordinate coord : knownMap.keySet()){
	        MapTile current = knownMap.get(coord);
	        if(current.isType(MapTile.Type.TRAP)){
                //TODO: DO something to handle the trap, and mark it as handled
            }
        }
    }

    public void handleTheTrap(Coordinate coord, MapTile mapTile){
	    //TODO: if there is a LAVA, based on the condition(do we have any keys?)
    }
}
