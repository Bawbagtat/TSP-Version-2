package TSP_pckg;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Solver {
	ArrayList<City> cities = new ArrayList<City>();

	double distance = Double.POSITIVE_INFINITY;

	// double closest;
	public Solver(ArrayList<City> cities) {
		this.cities = cities;
	}

	ArrayList<City> result = new ArrayList<City>();

	public double getDistance(City cityA, City cityB) {
		return Point2D.distance(cityA.x, cityA.y, cityB.x, cityB.y);
	}

	public void Solution() {
		City currentCity = cities.get(0);
		while (cities.size() > 0) {
			cities.remove(currentCity);
			City closest = null;
			double closestD = -1;
			result.add(currentCity);
			for (City possibleCity : cities) {
				if (getDistance(currentCity, possibleCity) < closestD || closest == null) {
					closest = possibleCity;
					closestD = getDistance(currentCity, possibleCity);
				}
			}
			currentCity = closest;
		}
		result.add(result.get(0));

	}

	
}
		
	

