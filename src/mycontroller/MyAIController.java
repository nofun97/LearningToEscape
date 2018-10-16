package mycontroller;

import controller.CarController;
import utilities.Coordinate;
import world.Car;

import java.util.ArrayList;

public class MyAIController extends CarController{
	// to store how many keys already got
    private ArrayList<Coordinate> keyList = new ArrayList<>();

	public MyAIController(Car car) {
		super(car);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

}
