/*
 * @author Rohit Tavare
 * @version 1.0
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//Import swing components
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class Mandelbrot {
	
	//GUI components are declared as attributes to be accessible in other methods
	JTextField x;
	JTextField y;
	JTextField zoom;
	JTextField iter;
	JButton render;
	Screen screen;
	JLabel console;
	
	//declare the array that will store iteration values for each pixel
	int[][] world;
	
	//Main method
	public static void main(String[] args) {
		Mandelbrot m = new Mandelbrot();
	}
	
	//creates the GUI and sets defaults
	public Mandelbrot() {
		
		//Top menu bar
		zoom = new JTextField("1");
		iter = new JTextField("1");
		render = new JButton("Render");
		render.addActionListener(new RenderFractal());
		x = new JTextField("0");
		y = new JTextField("0");
		console = new JLabel("Ready");
		
		//setting rendering defaults
		x.setPreferredSize(new Dimension(50, 20));
		y.setPreferredSize(new Dimension(50, 20));
		iter.setPreferredSize(new Dimension(50, 20));
		zoom.setPreferredSize(new Dimension(50, 20));
		
		JFrame window = new JFrame("Mandelbrot Fractal");
		
		//Centering frame on the screen
		Rectangle r = window.getGraphicsConfiguration().getBounds();
		double width = r.getWidth();
		double height = r.getHeight();
		int w = 650;
		int h = 745;
		
		window.setBounds((int)(width - w)/2, (int)(height - h)/2, w, h);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		
		//Panels to organize GUI components
		JPanel sideBar = new JPanel();
		JPanel location = new JPanel();
		JPanel X = new JPanel();
		JPanel Y = new JPanel();
		JPanel Z = new JPanel();
		JPanel Iter = new JPanel();
		JPanel stat = new JPanel();
		
		X.add(new JLabel("X:"));
		X.add(x);
		
		Y.add(new JLabel("Y:"));
		Y.add(y);
		
		Z.add(zoom);
		Z.setBorder(new TitledBorder("Zoom"));
		Iter.add(iter);
		Iter.setBorder(new TitledBorder("Iterations"));
		
		location.add(X);
		location.add(Y);
		location.setBorder(new TitledBorder("Location"));
		
		stat.add(console);
		stat.setBorder(new TitledBorder("Status"));
		
		JButton abt = new JButton("About");
		abt.addActionListener(new ShowAbout());
		
		sideBar.add(location);
		sideBar.add(Z);
		sideBar.add(Iter);
		sideBar.add(render);
		sideBar.add(stat);
		sideBar.add(abt);
		
		screen = new Screen();
		
		window.getContentPane().add(sideBar, BorderLayout.NORTH);
		window.getContentPane().add(screen, BorderLayout.CENTER);
		
		world = new int[650][650];
		
		window.setVisible(true);
	}
	
	//Private inner class to render the fractal
	private class Screen extends JPanel {

		@Override
		//Fractal is painted by overriding paintComponent()
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			Graphics2D g2 = (Graphics2D)g;
			g2.drawRect(0, 0, 1000, 900);
			
			for(int i = 0; i < world.length; i++) {
				for(int j = 0; j < world[0].length; j++) {
					int num = world[i][j] % 7;
					switch(num) {
					case 0:
						g2.setColor(Color.PINK);
						break;
					case 1:
						g2.setColor(Color.MAGENTA);
						break;
					case 2:
						g2.setColor(Color.BLUE);
						break;
					case 3:
						g2.setColor(Color.GREEN);
						break;
					case 4:
						g2.setColor(Color.YELLOW);
						break;
					case 5:
						g2.setColor(Color.ORANGE);
						break;
					case 6:
						g2.setColor(Color.RED);
						break;
						
					}
					if(world[i][j] == 0) {
						g2.setColor(Color.BLACK);
					}
					g2.fillRect(j, i, 1, 1);
				}
			}
		}
		
	}
	
	//private inner class to run the mandelbrot algorithm on each pixel coordinate
	private class RenderFractal implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			console.setText("Rendering...");
			render();
			screen.repaint();
			console.setText("Done");
		}
		
		private void render() {
			
			double z = Double.valueOf(zoom.getText());
			double xOff = Double.valueOf(x.getText());
			double yOff = Double.valueOf(y.getText());
			int iterations = Integer.valueOf(iter.getText());
			double width = 2/z;
			double height = (2.0 * world.length)/(world[0].length * z);
			double xVal = -width/2 + xOff;
			double yVal = -height/2 + yOff;
			double xInc = width/world[0].length;
			double yInc = height/world.length;
			for(int i = 0; i < world.length; i++) {
				for(int j = 0; j < world[0].length; j++) {
					int it = 0;
					double X = xVal + (j * xInc);
					double Y = yVal + (i * yInc);
					double real = X;
					double im = Y;
					while(it <= iterations) {
						it++;
						double tempR = Math.pow(real, 2) - Math.pow(im, 2) + X;
						double tempIm = 2*real*im + Y;
						real = tempR;
						im = tempIm;
						if(Math.sqrt(Math.pow(real, 2) + Math.pow(im, 2)) > 2) {
							break;
						}
					}
					if(it > iterations) {
						world[i][j] = 0;
					} else {
						world[i][j] = it;
					}
				}
			}
			
		}
		
	}
	
}

//action listener to launch the about window
class ShowAbout implements ActionListener {
	About aboutWindow;
	
	@Override
	public void actionPerformed(ActionEvent e) {
		aboutWindow = new About();
	}
	
}

//creates the about window
class About {
	
	public About() {
		JFrame window = new JFrame("About");
		Rectangle r = window.getGraphicsConfiguration().getBounds();
		double width = r.getWidth();
		double height = r.getHeight();
		int w = 500;
		int h = 400;
		
		window.setBounds((int)(width - w)/2, (int)(height - h)/2, w, h);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		
		JPanel leftFiller = new JPanel();
		leftFiller.setPreferredSize(new Dimension(20, 300));
		window.getContentPane().add(leftFiller, BorderLayout.WEST);
		
		JPanel rightFiller = new JPanel();
		rightFiller.setPreferredSize(new Dimension(20, 300));
		window.getContentPane().add(rightFiller, BorderLayout.EAST);
		
		JLabel text = new JLabel("<html>Author: Rohit Tavare<br/>" +
				"Version: 1.0<br/>" + 
				"The mandelbrot fractal is a world famous fractal with thousands of different patterns. " +
				"This renderer is built to help visualize the fractal, and zoom in to different parts of the fractal. " +
				"Each pixel on the frame is given a coordinate from the imaginary number plane, and those coordinates are plugged into an algorithm. " +
				"The algorithm runs until it's values have passed a threshold point, or until it reaches the maximum iteration count. " +
				"The pixels are then colored based on the number of iterations taken to reach the threshold, or are black, if the threshold was not reached.\n" +
				"<br/>more information on the mandelbrot fractal is available at: http://mathworld.wolfram.com/MandelbrotSet.html </html>"
				);
		
		text.setVerticalAlignment(JLabel.CENTER);
		window.getContentPane().add(text, BorderLayout.CENTER);
		
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		window.setVisible(true);
	}
}