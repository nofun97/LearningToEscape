package mycontroller;

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



	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

}
