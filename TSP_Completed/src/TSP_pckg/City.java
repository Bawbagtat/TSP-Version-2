package TSP_pckg;

import java.awt.geom.Point2D;

public class City extends Point2D.Double {
		private String name;
		public static double maxX=0;
		public static double maxY = 0;
		
		//City created by point and data etc
		
		public City(String name, double x, double y)
		{
			//maxX and maxY for visualising results
			super(x,y);
			if(x>maxX){
				maxX=x;
			}
			if(y>maxY){
				maxY=y;
			}
			this.name = name;
		}
		
		//Get city's name and returns the name or number associated
		
		public String getName()
		{
			return name;
		}
}
