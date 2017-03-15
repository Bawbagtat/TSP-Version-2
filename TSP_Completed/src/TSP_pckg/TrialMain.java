package TSP_pckg;

//import java.awt.GraphicsEnvironment;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class TrialMain {

	public static void main(String[] args) {
		File file;
		if (args.length > 0) {
			file = new File(args[0]);
		} else {
			JFileChooser chooser = new JFileChooser();

			int response = chooser.showOpenDialog(null);
			if (response != JFileChooser.APPROVE_OPTION)
				return;

			file = chooser.getSelectedFile();
		}

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
		} catch (IOException e) {

			System.exit(1);
		}

		int dimension = 0;
		try {
			String line;
			while (!(line = reader.readLine()).equals("NODE_COORD_SECTION")) {
				String[] entry = line.split(": ", 1);
				switch (entry[0].trim()) {
				case "TYPE":
					if (!entry[1].trim().equals("TSP"))
						throw new Exception("File not in TSP format");
					break;
				case "DIMENSION":
					dimension = Integer.parseInt(entry[1]);
					break;
				}
			}
		} catch (Exception e) {

			System.exit(1);
		}

		ArrayList<City> cities = new ArrayList<City>(dimension);

		try {
			String line;
			while ((line = reader.readLine()) != null && !line.equals("EOF")) {
				line = line.trim();
				line.replaceAll("  ", " ");
				line.replaceAll("  ", " ");
				String[] entry = line.split(" ");
				cities.add(new City(entry[0], Double.parseDouble(entry[1]), Double.parseDouble(entry[2])));
			}

			reader.close();
		} catch (Exception e) {

			System.exit(1);
		}

		/*
		 * Takes nearest neighbour from the solution class, gets the result of
		 * the total distance and also displays the map before implementing a 2
		 * opt solution.
		 */
		long StartTime = System.nanoTime();
		cities.add(cities.get(0));
		Solver solution = new Solver(cities);
		solution.Solution();
		double distance = totalDistance(solution.result);
		System.out.println(solution.result.toString());
		outputData(solution.result);
		long Endtime = System.nanoTime();
		long ResultTime = (Endtime - StartTime) / 1000000;
		System.out.println("Nearest Neighbour takes " + ResultTime);

		long StartOpt = System.nanoTime();
		boolean hasChanged = true;
		while (hasChanged) {
			hasChanged = true;
			boolean keepGoing = false;
			System.out.println(distance);// Display distance after nearest
											// neighbour sort

			for (int i = 1; i < solution.result.size() - 1; i++) {

				for (int j = i + 1; j < solution.result.size() - 1; j++) {
					if (solution.result.get(i) != solution.result.get(j)) {

						/*
						 * The distance of i and i-1 is calculated and stored
						 * into distancein Then i and i + 1 is calculated.
						 * Position J carries out same calculation
						 */

						// Finds the value of changed
						// paths and checks the
						// difference
						double distancein = 0;
						double distancein2 = 0;
						double distanceout = 0;
						double distanceout2 = 0;
						if (i > 0) {
							distancein = Point2D.distance(solution.result.get(i).getX(), solution.result.get(i).getY(),
									solution.result.get(i - 1).getX(), solution.result.get(i - 1).getY());
						}
						if (i < solution.result.size() - 1) {
							distanceout = Point2D.distance(solution.result.get(i).getX(), solution.result.get(i).getY(),
									solution.result.get(i + 1).getX(), solution.result.get(i + 1).getY());
						}
						if (j > 0) {
							distancein2 = Point2D.distance(solution.result.get(j).getX(), solution.result.get(j).getY(),
									solution.result.get(j - 1).getX(), solution.result.get(j - 1).getY());
						}
						if (j < solution.result.size() - 1) {

							distanceout2 = Point2D.distance(solution.result.get(j).getX(),
									solution.result.get(j).getY(), solution.result.get(j + 1).getX(),
									solution.result.get(j + 1).getY());
						}

						// Subtracts the distances obtained from the total
						// distance
						double dist = distance;

						dist = dist - distancein;
						dist = dist - distancein2;
						dist = dist - distanceout;
						dist = dist - distanceout2;

						double ndistancein = 0;
						double ndistancein2 = 0;
						double ndistanceout = 0;
						double ndistanceout2 = 0;

						// Values of i and j are swapped
						Collections.swap(solution.result, i, j);

						// Distances of i and i-1+i+1 are calculated again after
						// the swap.
						// Same applies to j in this case
						if (i > 0) {
							ndistancein = Point2D.distance(solution.result.get(i).getX(), solution.result.get(i).getY(),
									solution.result.get(i - 1).getX(), solution.result.get(i - 1).getY());
						}
						if (i < solution.result.size() - 1) {
							ndistanceout = Point2D.distance(solution.result.get(i).getX(),
									solution.result.get(i).getY(), solution.result.get(i + 1).getX(),
									solution.result.get(i + 1).getY());
						}
						if (j > 0) {
							ndistancein2 = Point2D.distance(solution.result.get(j).getX(),
									solution.result.get(j).getY(), solution.result.get(j - 1).getX(),
									solution.result.get(j - 1).getY());
						}
						if (j < solution.result.size() - 1) {

							ndistanceout2 = Point2D.distance(solution.result.get(j).getX(),
									solution.result.get(j).getY(), solution.result.get(j + 1).getX(),
									solution.result.get(j + 1).getY());
						}

						// If the values are found to be better than the
						// original we keep going.
						// If not then i+j are swapped again to return to the
						// original state and we continue
						dist += (ndistancein + ndistancein2 + ndistanceout + ndistanceout2);
						if (Math.abs(distance - dist) > 0.0001 && dist < distance) {

							distance = dist;
							keepGoing = true;
						} else {
							Collections.swap(solution.result, i, j);
						}

					}
				}

				hasChanged = keepGoing;
			}
		}
		outputData(solution.result);
		long EndOpt = System.nanoTime();
		long ResultOpt = (EndOpt - StartOpt) / 1000000;
		// double reslutSecond = ResultOpt / 1000;
		double resultMilliseconds = ResultOpt;
		System.out.println("2 Opt takes " + resultMilliseconds + " seconds");

		// Ensures that all the points are the same and that notghing has been
		// missed out
		// Needs to be corrected as cities is empty so nothing to compare
		if (solution.result.containsAll(cities)) {
			System.out.println("All items are the same in both lists");
		}

	}

	// Calculates total distance of route.
	private static double totalDistance(ArrayList<City> cin) {
		double distance = 0;
		for (int i = 1; i < cin.size(); i++) {
			distance += Point2D.distance(cin.get(i).getX(), cin.get(i).getY(), cin.get(i - 1).getX(),
					cin.get(i - 1).getY());
		}
		return distance;

	}

	public static void outputData(ArrayList<City> cin) {
		Visualiser vis = new Visualiser(800, 650);
		for (int i = 1; i < cin.size(); i++) {
			vis.addLine(cin.get(i), cin.get(i - 1));
		}
		JOptionPane.showMessageDialog(null, vis);
	}

}
