/**
 * 
 * This file is part of the CarParkSimulator Project, written as 
 * part of the assessment for INB370, semester 1, 2014. 
 * 
 * This GUI had used Eclipse's window designer for layout 
 * design and object creation, with modification on inner codes
 * for acceptance of data from CarPark 
 *
 * CarParkSimulator
 * asgn2Simulators 
 * 27/05/2014
 * 
 */
package asgn2Simulators;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import asgn2CarParks.CarPark;


import asgn2Exceptions.*;

import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JInternalFrame;

import javax.swing.ScrollPaneConstants;


import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * @author Chun Pui Chan
 *
 */
@SuppressWarnings("serial")
public class GUISimulator extends JFrame implements Runnable {
	private static String maxCarSpaces, maxSmallCarSpaces, maxMotorCycleSpaces, maxQueueSize, seed, carProb, smallCarProb, motorCycleProb, stayMean, staySD;
	private CarPark carPark;
	private  Simulator sim;
	private  Log log;
	private ChartPanel chartPanel;

	/**
	 * set properties of the frame
	 * @param arg0
	 * @throws HeadlessException
	 */
	public GUISimulator(String arg0) throws HeadlessException {
		super(arg0);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
	}
	/**
	 * Method to modify the chartPanel
	 * would render a bar graph using the last log(time=1080)
	 * as raw_data, obtain total number of vehicles and dissatisfied customers,
	 * and use JFreeChart to create the graph
	 */
	private void plotBarGraph(){
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		String[] currentStatus = carPark.getStatus(1080).split("::");
		//format: (time)::(TotalVehicles)::(CurrentVehicles)::(CurrentCars)::(CurrentSmallCars)::(CurrentMotorcycles)::(TotalDissatisfied)::(TotalArchived)"::(Queue)(status if any)
		dataset.addValue(Integer.parseInt(currentStatus[1]), "Total Vehicles", "");
		dataset.addValue(Integer.parseInt(currentStatus[6].substring(2)), "Total Dissatisfied", "");
		JFreeChart chart = ChartFactory.createBarChart("Summary", "Customers", "number", dataset);
		chartPanel = new ChartPanel(chart);
	}
	
	/**
	 * Method to modify chsrtPanel
	 * would render a line graph from time = 0 to time indicated
	 * accept carPark log as raw_data
	 * @param time current log time, indicates end of the x-axis
	 */
	private void plotLineGraph(int time){
		//create data series
		XYSeries seriesTotalVehicles = new XYSeries("TotalVehicles");
		XYSeries seriesCurrentVehicles = new XYSeries("CurrentVehicles");
		XYSeries seriesCurrentCars = new XYSeries("CurrentCars");
		XYSeries seriesCurrentSmallCars = new XYSeries("CurrentSmallCars");
		XYSeries seriesCurrentMotorcycles = new XYSeries("CurrentMotorcycles");
		XYSeries seriesTotalDissatisfied = new XYSeries("TotalDissatisfied");
		XYSeries seriesTotalArchived = new XYSeries("TotalArchived");
		XYSeries seriesCurrentQueue = new XYSeries("CurrentQueue");
		//read data
		for (int i = 0; i<= time; i++){
			String[] currentStatus = carPark.getStatus(i).split("::");
			//format: (time)::(TotalVehicles)::(CurrentVehicles)::(CurrentCars)::(CurrentSmallCars)::(CurrentMotorcycles)::(TotalDissatisfied)::(TotalArchived)"::(Queue)(status if any)
			seriesTotalVehicles.add(i,Integer.parseInt(currentStatus[1]));
			seriesCurrentVehicles.add(i,Integer.parseInt(currentStatus[2].substring(2)));
			seriesCurrentCars.add(i,Integer.parseInt(currentStatus[3].substring(2)));
			seriesCurrentSmallCars.add(i,Integer.parseInt(currentStatus[4].substring(2)));
			seriesCurrentMotorcycles.add(i,Integer.parseInt(currentStatus[5].substring(2)));
			seriesTotalDissatisfied.add(i,Integer.parseInt(currentStatus[6].substring(2)));
			seriesTotalArchived.add(i,Integer.parseInt(currentStatus[7].substring(2)));
			if (currentStatus[8].substring(2, 4) == "10"){
				seriesCurrentQueue.add(i, 10);
			}else {
				seriesCurrentQueue.add(i,Integer.parseInt(currentStatus[8].substring(2,3)));
			}
		}
		//create dataset form data series
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(seriesTotalVehicles);
		dataset.addSeries(seriesCurrentVehicles);
		dataset.addSeries(seriesCurrentCars);
		dataset.addSeries(seriesCurrentSmallCars);
		dataset.addSeries(seriesCurrentMotorcycles);
		dataset.addSeries(seriesTotalDissatisfied);
		dataset.addSeries(seriesTotalArchived);
		dataset.addSeries(seriesCurrentQueue);
		//generate empty graph
		JFreeChart chart = ChartFactory.createXYLineChart("Statistics", "Time", "No. of Vehicles", null);
		//start plotting graph
		XYPlot plot = (XYPlot)chart.getPlot();
		//modify graph style as suggestions as .pdf provided
		XYLineAndShapeRenderer render = new XYLineAndShapeRenderer();
		render.setSeriesPaint(0, Color.black);
		render.setSeriesShapesVisible(0, false);
		render.setSeriesPaint(1, Color.blue);
		render.setSeriesShapesVisible(1, false);
		render.setSeriesPaint(2, Color.cyan);
		render.setSeriesShapesVisible(2, false);
		render.setSeriesPaint(3, Color.gray);
		render.setSeriesShapesVisible(3, false);
		render.setSeriesPaint(4, Color.darkGray);
		render.setSeriesShapesVisible(4, false);
		render.setSeriesPaint(5, Color.green);
		render.setSeriesShapesVisible(5, false);
		render.setSeriesPaint(6, Color.red);
		render.setSeriesShapesVisible(6, false);
		render.setSeriesPaint(7, Color.yellow);
		render.setSeriesShapesVisible(7, false);
		plot.setDataset(dataset);
		plot.setRenderer(render);
		//output graph to panel
		chartPanel = new ChartPanel(chart);
	}
	
	/** 
	 * method to create internal frame
	 * render the result page, allowing user to
	 * input a certain time point, by Jspinner.
	 * then update information for ther selected time, 
	 * as well as update the graph
	 * 
	 */
	private void resultGUI(){
		final JButton btnSummary = new JButton("Summary");
		//resize the screen
		this.setSize(1000, 600);
		//run Carpark simulation
		SimulationRunner runner = new SimulationRunner(carPark, sim, log);
		try {
			runner.runSimulation();
		} catch (Exception e) {	} //since exceptions are handled in other area, escape any exceptions thrown
		//render a frame to the main frame
		final JInternalFrame resultFrame = new JInternalFrame("Result");
		getContentPane().add(resultFrame, BorderLayout.CENTER);
		resultFrame.getContentPane().setLayout(null);
		
		//creation of objects
		JLabel lblMaxCarSpaces = new JLabel("Max. Car Spaces: " + maxCarSpaces);
		JLabel lblSeed = new JLabel("Seed: " + seed);
		JLabel lblStayMean = new JLabel("Stay Mean: " + stayMean);
		JLabel lblMaxMotorcycleSpaces = new JLabel("Max. Motorcycle Spaces: " + maxMotorCycleSpaces);
		JLabel lblCarProb = new JLabel("Car Prob: " + carProb);
		JLabel lblStaySd = new JLabel("Stay SD: " + staySD);
		JLabel lblMaxQueueSize = new JLabel("Max. Queue Size: " + maxQueueSize);
		JLabel lblSmallCarProb = new JLabel("Small Car Prob: " + smallCarProb);
		JLabel lblMaxSmallCarSpaces = new JLabel("Max. Small Car Spaces: " + maxSmallCarSpaces);
		JLabel lblMotorcycleProb = new JLabel("MotorCycle Prob: " + motorCycleProb);
		JSeparator separator = new JSeparator();
		JLabel lblTime = new JLabel("Time:");
		final JLabel lblTotalCars = new JLabel("Total Cars Visited:");
		final JLabel lblRecentNoOf = new JLabel("Recent no. of vehicles: ");
		final JLabel lblRecentNoOf_1 = new JLabel("Recent no. of Cars:");
		final JLabel lblRecentNoOf_2 = new JLabel("Recent no. of Small Cars:");
		final JLabel lblRecentNoOf_3 = new JLabel("Recent no. of Motorcycles:");
		final JLabel lblNoOfDissatisfied = new JLabel("No. of dissatisfied customers:");
		final JLabel lblNoOfVehicles = new JLabel("No. of vehicles archived: ");
		final JLabel lblRecentQueue = new JLabel("Recent Queue:");
		JLabel lblEventsInThe = new JLabel("Events in the past minute:");
		final JTextField txtEvent = new JTextField("Events");
		final JPanel chartSpacePanel = new JPanel();
		final JSpinner spinner = new JSpinner();
		JButton btnNewSimulation = new JButton("New Simulation");
		JButton btnEndOfDay = new JButton("EndofDay");
		
		//lblMaxCarSpaces
		lblMaxCarSpaces.setBounds(7, 7, 172, 15);
		resultFrame.getContentPane().add(lblMaxCarSpaces);
		
		//lblSeed
		lblSeed.setBounds(189, 7, 138, 15);
		resultFrame.getContentPane().add(lblSeed);
		
		//lblStayMean
		lblStayMean.setBounds(337, 7, 131, 15);
		resultFrame.getContentPane().add(lblStayMean);
		
		//lblMaxMotorcycleSpaces
		lblMaxMotorcycleSpaces.setBounds(7, 26, 172, 15);
		resultFrame.getContentPane().add(lblMaxMotorcycleSpaces);
		
		//lblCarProb
		lblCarProb.setBounds(189, 26, 138, 15);
		resultFrame.getContentPane().add(lblCarProb);
		
		//lblStaySd
		lblStaySd.setBounds(337, 26, 131, 15);
		resultFrame.getContentPane().add(lblStaySd);
		
		//lblMaxQueueSize
		lblMaxQueueSize.setBounds(7, 45, 172, 15);
		resultFrame.getContentPane().add(lblMaxQueueSize);
		
		//lblSmallCarProb
		lblSmallCarProb.setBounds(189, 45, 138, 15);
		resultFrame.getContentPane().add(lblSmallCarProb);
		
		//lblMaxSmallCarSpaces
		lblMaxSmallCarSpaces.setBounds(7, 64, 172, 15);
		resultFrame.getContentPane().add(lblMaxSmallCarSpaces);
		
		//lblMotorcycleProb
		lblMotorcycleProb.setBounds(189, 64, 138, 15);
		resultFrame.getContentPane().add(lblMotorcycleProb);
		
		//separator
		separator.setBounds(7, 83, 464, 5);
		separator.setPreferredSize(new Dimension(480, 5));
		separator.setForeground(Color.RED);
		resultFrame.getContentPane().add(separator);
		
		//lblTime
		lblTime.setBounds(7, 105, 46, 15);
		resultFrame.getContentPane().add(lblTime);
		
		//lblTotalCars
		lblTotalCars.setBounds(7, 134, 200, 15);
		resultFrame.getContentPane().add(lblTotalCars);
		
		//lblRecentNoOf
		lblRecentNoOf.setBounds(7, 159, 200, 15);
		resultFrame.getContentPane().add(lblRecentNoOf);
		
		//lblRecentNoOf_1
		lblRecentNoOf_1.setBounds(7, 184, 200, 15);
		resultFrame.getContentPane().add(lblRecentNoOf_1);
		
		//lblRecentNoOf_2
		lblRecentNoOf_2.setBounds(7, 209, 200, 15);
		resultFrame.getContentPane().add(lblRecentNoOf_2);
		
		//lblRecentNoOf_3
		lblRecentNoOf_3.setBounds(7, 233, 200, 15);
		resultFrame.getContentPane().add(lblRecentNoOf_3);
		
		//lblNoOfDissatisfied
		lblNoOfDissatisfied.setBounds(7, 283, 200, 15);
		resultFrame.getContentPane().add(lblNoOfDissatisfied);
		
		//lblNoOfVehicles
		lblNoOfVehicles.setBounds(7, 308, 200, 15);
		resultFrame.getContentPane().add(lblNoOfVehicles);
		
		//lblRecentQueue
		lblRecentQueue.setBounds(7, 258, 200, 15);
		resultFrame.getContentPane().add(lblRecentQueue);
		
		//lblEventsInThe
		lblEventsInThe.setBounds(7, 333, 200, 15);
		resultFrame.getContentPane().add(lblEventsInThe);
		
		//txtEvent; scroll
		//bound a scroll bar to textField so more data can be shown
		txtEvent.setBackground(Color.white);
		txtEvent.setEditable ( false ); 
	    JScrollPane scroll = new JScrollPane ( txtEvent );
	    scroll.setHorizontalScrollBarPolicy ( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS );
	    scroll.setBounds(7, 358, 200, 40);
		resultFrame.getContentPane().add(scroll);
		
		//chartSpacePanel
		//create canvas for charts
		chartSpacePanel.setBounds(217, 89, 751, 444);
		resultFrame.getContentPane().add(chartSpacePanel);
		
		//spinner
		//time mutator
		spinner.addChangeListener(new ChangeListener() {
			/**
			 * Method to update innerFrame, get self value and
			 * read from carPark log.
			 * plot a new XYLineGraph and place into chart panel reserved
			 * @param arg0 event arg
			 */
			public void stateChanged(ChangeEvent arg0) {
				plotLineGraph((int)spinner.getValue());
				btnSummary.setText("Summary");
				chartSpacePanel.removeAll(); //remove old graph if exist to free heap 
				chartSpacePanel.add(chartPanel, BorderLayout.CENTER);
				String[] currentStatus = carPark.getStatus((int) spinner.getValue()).split("::");
				//format: (time)::(TotalVehicles)::(CurrentVehicles)::(CurrentCars)::(CurrentSmallCars)::(CurrentMotorcycles)::(TotalDissatisfied)::(TotalArchived)"::(Queue)(status if any)
				lblTotalCars.setText("Total Cars Visited: " + currentStatus[1]);
				lblRecentNoOf.setText("Recent no. of vehicles: " + currentStatus[2].substring(2));
				lblRecentNoOf_1.setText("Recent no. of cars: " + currentStatus[3].substring(2));
				lblRecentNoOf_2.setText("Recent no. of small cars: " + currentStatus[4].substring(2));
				lblRecentNoOf_3.setText("Recent no. of MotorCycles: " + currentStatus[5].substring(2));
				lblNoOfDissatisfied.setText("No. of dissatisfied customers: " + currentStatus[6].substring(2));
				lblNoOfVehicles.setText("No. of vehicles archived: "+ currentStatus[7].substring(2));
				int index = currentStatus[8].indexOf("|");
				int end = currentStatus[8].indexOf("\n");
				if (index != -1){
					lblRecentQueue.setText("Recent Queue: " + currentStatus[8].substring(2, index));
					txtEvent.setText(currentStatus[8].substring(index, end));
				}else {
					lblRecentQueue.setText("Recent Queue: " + currentStatus[8].substring(2, end));
					txtEvent.setText("No Events during last minute.");
				}
			}
		});
		spinner.setModel(new SpinnerNumberModel(1, 0, 1080, 1)); //set default value for 1 as to trigger spinner event later
		spinner.setBounds(48, 102, 55, 22);
		resultFrame.getContentPane().add(spinner);
		spinner.setValue(0); //trigger spinner event so time=0 information can be displayed
		
		//btnNewSimulation
		btnNewSimulation.setBounds(7, 444, 200, 23);
		btnNewSimulation.addActionListener(new ActionListener() {
			/**
			 * method to close innerFrame result and open a new innerFrmae configuration
			 * @param arg0 Event arg
			 */
			public void actionPerformed(ActionEvent arg0) {
				resultFrame.dispose();
				createConfigurationGUI();
			}
		});
		resultFrame.getContentPane().add(btnNewSimulation);
		
		//btnEndOfDay
		btnEndOfDay.addActionListener(new ActionListener() {
			/**
			 * method to shift to the end of the day
			 * @param e Event 
			 */
			public void actionPerformed(ActionEvent e) {
				spinner.setValue(1080);
			}
		});
		btnEndOfDay.setBounds(113, 101, 94, 23);
		resultFrame.getContentPane().add(btnEndOfDay);
		
		//btnSummary
		btnSummary.addActionListener(new ActionListener() {
			/**
			 * method to goto summary page which bar chart is shown
			 * then shift to button which user can change back to line graph view
			 */
			public void actionPerformed(ActionEvent arg0) {
				if (btnSummary.getText() == "line graph"){
					btnSummary.setText("Summary");
					plotLineGraph((int)spinner.getValue());
				}else{
					spinner.setValue(1080);
					btnSummary.setText("line graph");
					plotBarGraph();
				}
				chartSpacePanel.removeAll();
				chartSpacePanel.add(chartPanel, BorderLayout.CENTER);
			}
		});
		btnSummary.setBounds(7, 408, 200, 23);
		resultFrame.getContentPane().add(btnSummary);
		

		resultFrame.setVisible(true);
	}
	
	/** 
	 * method to create internal frame
	 * render the configuration page, allowing user to
	 * modify settings of the Carpark Simulator,
	 * and start Simulation on request
	 * 
	 */
	private void createConfigurationGUI(){
		//create InnerFrame
		this.setSize(500, 500);
		final JInternalFrame configureFrame = new JInternalFrame("Configuration");
		getContentPane().add(configureFrame, BorderLayout.CENTER);
		configureFrame.getContentPane().setLayout(null);
		
		//create objects
		JLabel lblSizeParameters = new JLabel("Size Parameters:");
		JLabel lblMaxCarSpaces = new JLabel("Max. Car Spaces:");
		JLabel lblMaxSmallCar = new JLabel("Max. Small Car Spaces:");
		JLabel lblSmaxMotorcycleSpace = new JLabel("Max. Motorcycle Spaces:");
		JLabel lblMaxQueueSize = new JLabel("Max. Queue Size:");
		JLabel lblRngAndProbs = new JLabel("RNG and Probs");
		JLabel lblSeed = new JLabel("Seed:");
		JLabel lblCarProb = new JLabel("Car Prob:");
		JLabel lblSmallCarProb = new JLabel("Small Car Prob:");
		JLabel lblMotorcycleProb = new JLabel("Motorcycle Prob:");
		JLabel lblIntendedStayMean = new JLabel("Intended Stay Mean:");
		JLabel lblIntendedStaySd = new JLabel("Intended Stay SD:");
		final JButton btnStart = new JButton("Start");
		final JTextField txtCarspaces = new JTextField();
		final JTextField txtSmallcarspaces = new JTextField();
		final JTextField txtMotorcyclespaces = new JTextField();
		final JTextField txtQueuesize = new JTextField();
		final JTextField txtSeed = new JTextField();
		final JTextField txtCarprob = new JTextField();
		final JTextField txtSmallcarprob = new JTextField();
		final JTextField txtMotorcycleprob = new JTextField();
		final JTextField txtStaymean = new JTextField();
		final JTextField txtStaysd = new JTextField();
		
		//lblSizeParameters
		lblSizeParameters.setBounds(7, 7, 196, 15);
		configureFrame.getContentPane().add(lblSizeParameters);

		//lblMaxCarSpaces
		lblMaxCarSpaces.setBounds(17, 29, 186, 15);
		lblMaxCarSpaces.setHorizontalAlignment(SwingConstants.RIGHT);
		configureFrame.getContentPane().add(lblMaxCarSpaces);

		//txtCarspaces
		txtCarspaces.setBounds(207, 26, 264, 21);
		txtCarspaces.setText(maxCarSpaces);
		configureFrame.getContentPane().add(txtCarspaces);
		txtCarspaces.setColumns(10);

		//lblMaxSmallCar
		lblMaxSmallCar.setBounds(7, 54, 196, 15);
		lblMaxSmallCar.setHorizontalAlignment(SwingConstants.RIGHT);
		configureFrame.getContentPane().add(lblMaxSmallCar);

		//txtSmallcarspaces
		txtSmallcarspaces.setBounds(207, 51, 264, 21);
		txtSmallcarspaces.setText(maxSmallCarSpaces);
		configureFrame.getContentPane().add(txtSmallcarspaces);
		txtSmallcarspaces.setColumns(10);

		//lblSmaxMotorcycleSpace
		lblSmaxMotorcycleSpace.setBounds(7, 79, 196, 15);
		lblSmaxMotorcycleSpace.setHorizontalAlignment(SwingConstants.RIGHT);
		configureFrame.getContentPane().add(lblSmaxMotorcycleSpace);

		//txtMotorcyclespaces
		txtMotorcyclespaces.setBounds(207, 76, 264, 21);
		txtMotorcyclespaces.setText(maxMotorCycleSpaces);
		configureFrame.getContentPane().add(txtMotorcyclespaces);
		txtMotorcyclespaces.setColumns(10);
		
		//lblMaxQueueSize
		lblMaxQueueSize.setBounds(10, 104, 193, 15);
		lblMaxQueueSize.setHorizontalAlignment(SwingConstants.RIGHT);
		configureFrame.getContentPane().add(lblMaxQueueSize);
		
		//txtQueuesize
		txtQueuesize.setBounds(207, 101, 264, 21);
		txtQueuesize.setText(maxQueueSize);
		configureFrame.getContentPane().add(txtQueuesize);
		txtQueuesize.setColumns(10);
		
		//lblRngAndProbs
		lblRngAndProbs.setBounds(7, 126, 196, 15);
		configureFrame.getContentPane().add(lblRngAndProbs);
		
		//lblSeed
		lblSeed.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSeed.setBounds(7, 148, 196, 15);
		configureFrame.getContentPane().add(lblSeed);
		
		//txtSeed
		txtSeed.setBounds(207, 145, 264, 21);
		txtSeed.setText(seed);
		configureFrame.getContentPane().add(txtSeed);
		txtSeed.setColumns(10);
		
		//lblCarProb
		lblCarProb.setBounds(7, 173, 196, 15);
		lblCarProb.setHorizontalAlignment(SwingConstants.RIGHT);
		configureFrame.getContentPane().add(lblCarProb);
		
		//txtCarprob
		txtCarprob.setBounds(207, 170, 264, 21);
		txtCarprob.setText(carProb);
		configureFrame.getContentPane().add(txtCarprob);
		txtCarprob.setColumns(10);
		
		//lblSmallCarProb
		lblSmallCarProb.setBounds(7, 198, 196, 15);
		lblSmallCarProb.setHorizontalAlignment(SwingConstants.RIGHT);
		configureFrame.getContentPane().add(lblSmallCarProb);
		
		//txtSmallcarprob
		txtSmallcarprob.setBounds(207, 195, 264, 21);
		txtSmallcarprob.setText(smallCarProb);
		configureFrame.getContentPane().add(txtSmallcarprob);
		txtSmallcarprob.setColumns(10);
		
		//lblMotorcycleProb
		lblMotorcycleProb.setBounds(7, 223, 196, 15);
		lblMotorcycleProb.setHorizontalAlignment(SwingConstants.RIGHT);
		configureFrame.getContentPane().add(lblMotorcycleProb);
		
		//txtMotorcycleprob
		txtMotorcycleprob.setBounds(207, 220, 264, 21);
		txtMotorcycleprob.setText(motorCycleProb);
		configureFrame.getContentPane().add(txtMotorcycleprob);
		txtMotorcycleprob.setColumns(10);
		
		//lblIntendedStayMean
		lblIntendedStayMean.setBounds(7, 248, 196, 15);
		lblIntendedStayMean.setHorizontalAlignment(SwingConstants.RIGHT);
		configureFrame.getContentPane().add(lblIntendedStayMean);
		
		//txtStaymean
		txtStaymean.setBounds(207, 245, 264, 21);
		txtStaymean.setText(stayMean);
		configureFrame.getContentPane().add(txtStaymean);
		txtStaymean.setColumns(10);

		//lblIntendedStaySd
		lblIntendedStaySd.setBounds(7, 273, 196, 15);
		lblIntendedStaySd.setHorizontalAlignment(SwingConstants.RIGHT);
		configureFrame.getContentPane().add(lblIntendedStaySd);

		//txtStaysd
		txtStaysd.setBounds(207, 270, 264, 21);
		txtStaysd.setText(staySD);
		configureFrame.getContentPane().add(txtStaysd);
		txtStaysd.setColumns(10);

		//btnStart
		btnStart.setBounds(7, 295, 464, 23);
		//maxCarSpaces, maxSmallCarSpaces, maxMotorCycleSpaces, maxQueueSize, seed, carProb, smallCarProb, motorCycleProb, stayMean, staySD
		btnStart.addActionListener(new ActionListener() {
			/**
			 * method to start simulation
			 * validate the data in the textField
			 * if valid, create classes for simulation and 
			 * process the program the innerFrame result
			 * @param e Event
			 */
			public void actionPerformed(ActionEvent e) {
				String msg =""; //error message
				try{
					//validate car park related parameters
					msg = "Max. Car Spaces";
					int intMaxCarSpaces = Integer.parseInt(txtCarspaces.getText());
					msg = "Max Small Car Spaces";
					int intMaxSmallCarSpaces = Integer.parseInt(txtSmallcarspaces.getText());
					msg ="Max MotorCycle Spaces";
					int intMaxMotorCycleSpaces = Integer.parseInt(txtMotorcyclespaces.getText());
					msg = "Max. Queue Size";
					int intMaxQueueSize = Integer.parseInt(txtQueuesize.getText());
					if ((intMaxCarSpaces < 0) || (intMaxSmallCarSpaces < 0) || (intMaxMotorCycleSpaces < 0) || (intMaxQueueSize < 0)) { throw new SimulationException("Car park property should not smaller than zero.");}
					if (intMaxSmallCarSpaces > intMaxCarSpaces){ throw new SimulationException("Car park spaces should not be smaller than small car spaces");}
					carPark = new CarPark(intMaxCarSpaces, intMaxSmallCarSpaces,intMaxMotorCycleSpaces, intMaxQueueSize);
					//validate Simulator related parameters
					msg = "SEED";
					int intSeed = Integer.parseInt(txtSeed.getText());
					msg = "Car Prob";
					double deciCarProb = Double.parseDouble(txtCarprob.getText());
					msg = "Small Car Prob";
					double deciSmallCarProb = Double.parseDouble(txtSmallcarprob.getText());
					msg = "Motorcycle Prob";
					double deciMotorCycleProb = Double.parseDouble(txtMotorcycleprob.getText());
					msg = "Intended Stay Mean";
					double deciStayMean = Double.parseDouble(txtStaymean.getText());
					msg = "Intended Stay SD";
					double deciStaySD = Double.parseDouble(txtStaysd.getText());
					sim = new Simulator(intSeed, deciStayMean, deciStaySD, deciCarProb, deciSmallCarProb, deciMotorCycleProb);
					log = new Log();
					configureFrame.dispose(); //close current innerFrame
					//store user inputs
					maxCarSpaces = txtCarspaces.getText();
					maxSmallCarSpaces = txtSmallcarspaces.getText();
					maxMotorCycleSpaces = txtMotorcyclespaces.getText();
					maxQueueSize = txtQueuesize.getText();
					seed = txtSeed.getText();
					carProb = txtCarprob.getText();
					smallCarProb = txtSmallcarprob.getText();
					motorCycleProb = txtMotorcycleprob.getText();
					stayMean = txtStaymean.getText();
					staySD = txtStaysd.getText();
					resultGUI(); //call innerFrame result
				} catch(NumberFormatException ne){
					btnStart.setText(msg + " is not a valid number format.");
				} catch (Exception se){
					btnStart.setText(se.getMessage());
				}
			}
		});
		configureFrame.getContentPane().add(btnStart);
		configureFrame.setVisible(true);
	}

	
	
	/** 
	 *method to run as program is executed
	 *render the initial frame
	 */
	@Override
	public void run() {
		createConfigurationGUI();
		this.setVisible(true);
	}

	/**
	 * accept command line arguments
	 * if number of arguments not equals to 10, use default value instead
	 * then generate GUI
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length != 10){
			maxCarSpaces = Integer.toString(Constants.DEFAULT_MAX_CAR_SPACES);
			maxSmallCarSpaces = Integer.toString(Constants.DEFAULT_MAX_SMALL_CAR_SPACES);
			maxMotorCycleSpaces = Integer.toString(Constants.DEFAULT_MAX_MOTORCYCLE_SPACES);
			maxQueueSize = Integer.toString(Constants.DEFAULT_MAX_QUEUE_SIZE);
			seed = Integer.toString(Constants.DEFAULT_SEED);
			carProb = Double.toString(Constants.DEFAULT_CAR_PROB);
			smallCarProb = Double.toString(Constants.DEFAULT_SMALL_CAR_PROB);
			motorCycleProb = Double.toString(Constants.DEFAULT_MOTORCYCLE_PROB);
			stayMean = Double.toString(Constants.DEFAULT_INTENDED_STAY_MEAN);
			staySD = Double.toString(Constants.DEFAULT_INTENDED_STAY_SD);
		}else{
			maxCarSpaces = args[0];
			maxSmallCarSpaces = args[1];
			maxMotorCycleSpaces = args[2];
			maxQueueSize = args[3];
			seed = args[4];
			carProb = args[5];
			smallCarProb = args[6];
			motorCycleProb = args[7];
			stayMean = args[8];
			staySD = args[9];
		}
	    JFrame.setDefaultLookAndFeelDecorated(true);
        SwingUtilities.invokeLater(new GUISimulator("Car Simulator"));

	}
}
