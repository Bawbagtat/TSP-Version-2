package TSP_pckg;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import javax.swing.JComponent;

public class Visualiser extends JComponent{
	
	private ArrayList<Line2D.Double> lines;
	public static double totalDistance = 0;
	
	Visualiser(int width, int height){
		super();
		setPreferredSize(new Dimension(width, height));
		this.lines = new ArrayList<Line2D.Double>();
	}
	
	public void addLine (City c1,City c2){
		double x1= c1.getX();
		double y1 = c1.getY();
		double x2= c2.getX();
		double y2 = c2.getY();
		
		int width = (int)getPreferredSize().getWidth(); 
		int height = (int)getPreferredSize().getHeight();

		x1 = width/City.maxX * x1;
		y1 = height/City.maxY * y1;
		x2 = width/City.maxX* x2;
		y2 = height/City.maxY * y2;
		
		double padding = 0.80;
		
		x1 = (x1*padding)+(width-width*padding)/2;
		x2 = (x2*padding)+(width-width*padding)/2;
		y1 = (y1*padding)+(height-height*padding)/2;
		y2 = (y2*padding)+(height-height*padding)/2;

	
		Line2D.Double line = new Line2D.Double(x1, y1, x2, y2);
		this.lines.add(line);
		repaint();
	}
	
	
	public void paintComponent(Graphics g){
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
		int counter = 0;
		for(Line2D.Double line : lines){
			if(counter%2==0){
				g.setColor(Color.white);
				g.drawLine((int)line.getX1(),(int)line.getY1(),(int)line.getX2(),(int)line.getY2());
			}else{
				g.setColor(Color.red);
				g.drawLine((int)line.getX1(),(int)line.getY1(),(int)line.getX2(),(int)line.getY2());
			}
			counter++;
		}
	}

}