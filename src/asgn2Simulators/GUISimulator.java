/**
 * 
 * This file is part of the CarParkSimulator Project, written as 
 * part of the assessment for INB370, semester 1, 2014. 
 *
 * CarParkSimulator
 * asgn2Simulators 
 * 20/04/2014
 * 
 */
package asgn2Simulators;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import asgn2CarParks.CarPark;


import asgn2Exceptions.*;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JInternalFrame;









import javax.swing.ScrollPaneConstants;

import net.miginfocom.swing.MigLayout;

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

/**
 * @author hogan
 *
 */
@SuppressWarnings("serial")
public class GUISimulator extends JFrame implements Runnable {
	private JTextField txtCarspaces;
	private JTextField txtSmallcarspaces;
	private JTextField txtMotorcyclespaces;
	private JTextField txtQueuesize;
	private JTextField txtSeed;
	private JTextField txtCarprob;
	private JTextField txtSmallcarprob;
	private JTextField txtMotorcycleprob;
	private JTextField txtStaymean;
	private JTextField txtStaysd;
	private static String maxCarSpaces, maxSmallCarSpaces, maxMotorCycleSpaces, maxQueueSize, seed, carProb, smallCarProb, motorCycleProb, stayMean, staySD;
	private CarPark carPark;
	private  Simulator sim;
	private  Log log;
	private ChartPanel chartPanel;

	/**
	 * @param arg0
	 * @throws HeadlessException
	 */
	public GUISimulator(String arg0) throws HeadlessException {
		super(arg0);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setSize(500,500);
		createConfigurationGUI();
	}
	private void plotBarGraph(){
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		String[] currentStatus = carPark.getStatus(1080).split("::");
		dataset.addValue(Integer.parseInt(currentStatus[1]), "Total Vehicles", "");
		dataset.addValue(Integer.parseInt(currentStatus[6].substring(2)), "Total Dissatisfied", "");
		JFreeChart chart = ChartFactory.createBarChart("Summary", "Customers", "number", dataset);
		chartPanel = new ChartPanel(chart);
	}
	
	private void plotLineGraph(int time){
		XYSeries seriesTotalVehicles = new XYSeries("TotalVehicles");
		XYSeries seriesCurrentVehicles = new XYSeries("CurrentVehicles");
		XYSeries seriesCurrentCars = new XYSeries("CurrentCars");
		XYSeries seriesCurrentSmallCars = new XYSeries("CurrentSmallCars");
		XYSeries seriesCurrentMotorcycles = new XYSeries("CurrentMotorcycles");
		XYSeries seriesTotalDissatisfied = new XYSeries("TotalDissatisfied");
		XYSeries seriesTotalArchived = new XYSeries("TotalArchived");
		XYSeries seriesCurrentQueue = new XYSeries("CurrentQueue");
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
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(seriesTotalVehicles);
		dataset.addSeries(seriesCurrentVehicles);
		dataset.addSeries(seriesCurrentCars);
		dataset.addSeries(seriesCurrentSmallCars);
		dataset.addSeries(seriesCurrentMotorcycles);
		dataset.addSeries(seriesTotalDissatisfied);
		dataset.addSeries(seriesTotalArchived);
		dataset.addSeries(seriesCurrentQueue);
		JFreeChart chart = ChartFactory.createXYLineChart("Statistics", "Time", "No. of Vehicles", null);
		XYPlot plot = (XYPlot)chart.getPlot();
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
		chartPanel = new ChartPanel(chart);
	}
	
	private void resultGUI(){
		this.setSize(1000, 600);
		SimulationRunner runner = new SimulationRunner(carPark, sim, log);
		try {
			runner.runSimulation();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		final JInternalFrame resultFrame = new JInternalFrame("Result");
		getContentPane().add(resultFrame, BorderLayout.CENTER);
		resultFrame.getContentPane().setLayout(null);
		
		JLabel lblMaxCarSpaces = new JLabel("Max. Car Spaces: " + maxCarSpaces);
		lblMaxCarSpaces.setBounds(7, 7, 172, 15);
		resultFrame.getContentPane().add(lblMaxCarSpaces);
		
		JLabel lblSeed = new JLabel("Seed: " + seed);
		lblSeed.setBounds(189, 7, 138, 15);
		resultFrame.getContentPane().add(lblSeed);
		
		JLabel lblStayMean = new JLabel("Stay Mean: " + stayMean);
		lblStayMean.setBounds(337, 7, 131, 15);
		resultFrame.getContentPane().add(lblStayMean);
		
		JLabel lblMaxMotorcycleSpaces = new JLabel("Max. Motorcycle Spaces: " + maxMotorCycleSpaces);
		lblMaxMotorcycleSpaces.setBounds(7, 26, 172, 15);
		resultFrame.getContentPane().add(lblMaxMotorcycleSpaces);
		
		JLabel lblCarProb = new JLabel("Car Prob: " + carProb);
		lblCarProb.setBounds(189, 26, 138, 15);
		resultFrame.getContentPane().add(lblCarProb);
		
		JLabel lblStaySd = new JLabel("Stay SD: " + staySD);
		lblStaySd.setBounds(337, 26, 131, 15);
		resultFrame.getContentPane().add(lblStaySd);
		
		JLabel lblMaxQueueSize = new JLabel("Max. Queue Size: " + maxQueueSize);
		lblMaxQueueSize.setBounds(7, 45, 172, 15);
		resultFrame.getContentPane().add(lblMaxQueueSize);
		
		JLabel lblSmallCarProb = new JLabel("Small Car Prob: " + smallCarProb);
		lblSmallCarProb.setBounds(189, 45, 138, 15);
		resultFrame.getContentPane().add(lblSmallCarProb);
		
		JLabel lblMaxSmallCarSpaces = new JLabel("Max. Small Car Spaces: " + maxSmallCarSpaces);
		lblMaxSmallCarSpaces.setBounds(7, 64, 172, 15);
		resultFrame.getContentPane().add(lblMaxSmallCarSpaces);
		
		JLabel lblMotorcycleProb = new JLabel("MotorCycle Prob: " + motorCycleProb);
		lblMotorcycleProb.setBounds(189, 64, 138, 15);
		resultFrame.getContentPane().add(lblMotorcycleProb);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(7, 83, 464, 5);
		separator.setPreferredSize(new Dimension(480, 5));
		separator.setForeground(Color.RED);
		resultFrame.getContentPane().add(separator);
		
		JLabel lblTime = new JLabel("Time:");
		lblTime.setBounds(7, 105, 46, 15);
		resultFrame.getContentPane().add(lblTime);
		
		final JLabel lblTotalCars = new JLabel("Total Cars Visited:");
		lblTotalCars.setBounds(7, 134, 200, 15);
		resultFrame.getContentPane().add(lblTotalCars);
		
		final JLabel lblRecentNoOf = new JLabel("Recent no. of vehicles: ");
		lblRecentNoOf.setBounds(7, 159, 200, 15);
		resultFrame.getContentPane().add(lblRecentNoOf);
		
		final JLabel lblRecentNoOf_1 = new JLabel("Recent no. of Cars:");
		lblRecentNoOf_1.setBounds(7, 184, 200, 15);
		resultFrame.getContentPane().add(lblRecentNoOf_1);
		
		final JLabel lblRecentNoOf_2 = new JLabel("Recent no. of Small Cars:");
		lblRecentNoOf_2.setBounds(7, 209, 200, 15);
		resultFrame.getContentPane().add(lblRecentNoOf_2);
		
		final JLabel lblRecentNoOf_3 = new JLabel("Recent no. of Motorcycles:");
		lblRecentNoOf_3.setBounds(7, 233, 200, 15);
		resultFrame.getContentPane().add(lblRecentNoOf_3);
		
		final JLabel lblNoOfDissatisfied = new JLabel("No. of dissatisfied customers:");
		lblNoOfDissatisfied.setBounds(7, 283, 200, 15);
		resultFrame.getContentPane().add(lblNoOfDissatisfied);
		
		final JLabel lblNoOfVehicles = new JLabel("No. of vehicles archived: ");
		lblNoOfVehicles.setBounds(7, 308, 200, 15);
		resultFrame.getContentPane().add(lblNoOfVehicles);
		
		final JLabel lblRecentQueue = new JLabel("Recent Queue:");
		lblRecentQueue.setBounds(7, 258, 200, 15);
		resultFrame.getContentPane().add(lblRecentQueue);
		
		JLabel lblEventsInThe = new JLabel("Events in the past minute:");
		lblEventsInThe.setBounds(7, 333, 200, 15);
		resultFrame.getContentPane().add(lblEventsInThe);
		
		final JTextArea txtEvent = new JTextArea("Events");
		txtEvent.setEditable ( false ); // set textArea non-editable
	    JScrollPane scroll = new JScrollPane ( txtEvent );
	    scroll.setHorizontalScrollBarPolicy ( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS );
	    scroll.setBounds(7, 358, 200, 40);
		resultFrame.getContentPane().add(scroll);
		final JPanel chartSpacePanel = new JPanel();
		chartSpacePanel.setBounds(217, 89, 751, 444);
		resultFrame.getContentPane().add(chartSpacePanel);
		
		final JSpinner spinner = new JSpinner();
		spinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				plotLineGraph((int)spinner.getValue());
				chartSpacePanel.removeAll();
				chartSpacePanel.add(chartPanel, BorderLayout.CENTER);
				String[] currentStatus = carPark.getStatus((int) spinner.getValue()).split("::");
				//format: (time)::(TotalVehicles)::(CurrentVehicles)::(CurrentCars)::(CurrentSmallCars)::(CurrentMotorcycles)::(TotalDissatisfied)::(TotalArchived)"::(Queue)(status if any)
				lblTotalCars.setText("Total Cars Visited: " + currentStatus[1]);
				lblRecentNoOf.setText("Recent no. of vehicles: " + currentStatus[2]);
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
		spinner.setModel(new SpinnerNumberModel(1, 0, 1080, 1));
		spinner.setBounds(48, 102, 55, 22);
		resultFrame.getContentPane().add(spinner);
		spinner.setValue(0);
		
		JButton btnNewSimulation = new JButton("New Simulation");
		btnNewSimulation.setBounds(7, 444, 200, 23);
		btnNewSimulation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				resultFrame.dispose();
//				carPark=null;
//				log=null;
//				sim=null;
				createConfigurationGUI();
			}
		});
		resultFrame.getContentPane().add(btnNewSimulation);
		
		JButton btnEndOfDay = new JButton("EndofDay");
		btnEndOfDay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				spinner.setValue(1080);
			}
		});
		btnEndOfDay.setBounds(113, 101, 94, 23);
		resultFrame.getContentPane().add(btnEndOfDay);
		
		
		final JButton btnSummary = new JButton("Summary");
		btnSummary.addActionListener(new ActionListener() {
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
	
	private void createConfigurationGUI(){
		this.setSize(500, 500);
		final JInternalFrame configureFrame = new JInternalFrame("Configuration");
		getContentPane().add(configureFrame, BorderLayout.CENTER);
		configureFrame.getContentPane().setLayout(new MigLayout("", "[][][grow]", "[][][][][][][][][][][][][]"));
		
		JLabel lblSizeParameters = new JLabel("Size Parameters:");
		configureFrame.getContentPane().add(lblSizeParameters, "cell 0 0");
		
		JLabel lblMaxCarSpaces = new JLabel("Max. Car Spaces:");
		configureFrame.getContentPane().add(lblMaxCarSpaces, "cell 1 1,alignx trailing");
		
		txtCarspaces = new JTextField();
		txtCarspaces.setText(maxCarSpaces);
		configureFrame.getContentPane().add(txtCarspaces, "cell 2 1,growx");
		txtCarspaces.setColumns(10);
		
		JLabel lblMaxSmallCar = new JLabel("Max. Small Car Spaces:");
		configureFrame.getContentPane().add(lblMaxSmallCar, "cell 1 2,alignx trailing");
		
		txtSmallcarspaces = new JTextField();
		txtSmallcarspaces.setText(maxSmallCarSpaces);
		configureFrame.getContentPane().add(txtSmallcarspaces, "cell 2 2,growx");
		txtSmallcarspaces.setColumns(10);
		
		JLabel lblSmaxMotorcycleSpace = new JLabel("Max. Motorcycle Spaces:");
		configureFrame.getContentPane().add(lblSmaxMotorcycleSpace, "cell 1 3,alignx trailing");
		
		txtMotorcyclespaces = new JTextField();
		txtMotorcyclespaces.setText(maxMotorCycleSpaces);
		configureFrame.getContentPane().add(txtMotorcyclespaces, "cell 2 3,growx");
		txtMotorcyclespaces.setColumns(10);
		
		JLabel lblMaxQueueSize = new JLabel("Max. Queue Size:");
		configureFrame.getContentPane().add(lblMaxQueueSize, "cell 1 4,alignx trailing");
		
		txtQueuesize = new JTextField();
		txtQueuesize.setText(maxQueueSize);
		configureFrame.getContentPane().add(txtQueuesize, "cell 2 4,growx");
		txtQueuesize.setColumns(10);
		
		JLabel lblRngAndProbs = new JLabel("RNG and Probs");
		configureFrame.getContentPane().add(lblRngAndProbs, "cell 0 5");
		
		JLabel lblSeed = new JLabel("Seed:");
		configureFrame.getContentPane().add(lblSeed, "cell 1 6,alignx trailing");
		
		txtSeed = new JTextField();
		txtSeed.setText(seed);
		configureFrame.getContentPane().add(txtSeed, "cell 2 6,growx");
		txtSeed.setColumns(10);
		
		JLabel lblCarProb = new JLabel("Car Prob:");
		configureFrame.getContentPane().add(lblCarProb, "cell 1 7,alignx trailing");
		
		txtCarprob = new JTextField();
		txtCarprob.setText(carProb);
		configureFrame.getContentPane().add(txtCarprob, "cell 2 7,growx");
		txtCarprob.setColumns(10);
		
		JLabel lblSmallCarProb = new JLabel("Small Car Prob:");
		configureFrame.getContentPane().add(lblSmallCarProb, "cell 1 8,alignx trailing");
		
		txtSmallcarprob = new JTextField();
		txtSmallcarprob.setText(smallCarProb);
		configureFrame.getContentPane().add(txtSmallcarprob, "cell 2 8,growx");
		txtSmallcarprob.setColumns(10);
		
		JLabel lblMotorcycleProb = new JLabel("Motorcycle Prob:");
		configureFrame.getContentPane().add(lblMotorcycleProb, "cell 1 9,alignx trailing");
		
		txtMotorcycleprob = new JTextField();
		txtMotorcycleprob.setText(motorCycleProb);
		configureFrame.getContentPane().add(txtMotorcycleprob, "cell 2 9,growx");
		txtMotorcycleprob.setColumns(10);
		
		JLabel lblIntendedStayMean = new JLabel("Intended Stay Mean:");
		configureFrame.getContentPane().add(lblIntendedStayMean, "cell 1 10,alignx trailing");
		
		txtStaymean = new JTextField();
		txtStaymean.setText(stayMean);
		configureFrame.getContentPane().add(txtStaymean, "cell 2 10,growx");
		txtStaymean.setColumns(10);
		
		JLabel lblIntendedStaySd = new JLabel("Intended Stay SD:");
		configureFrame.getContentPane().add(lblIntendedStaySd, "cell 1 11,alignx trailing");
		
		txtStaysd = new JTextField();
		txtStaysd.setText(staySD);
		configureFrame.getContentPane().add(txtStaysd, "cell 2 11,growx");
		txtStaysd.setColumns(10);
		
		final JButton btnStart = new JButton("Start");
		//maxCarSpaces, maxSmallCarSpaces, maxMotorCycleSpaces, maxQueueSize, seed, carProb, smallCarProb, motorCycleProb, stayMean, staySD
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String msg ="";
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
					configureFrame.dispose();
					resultGUI();
				} catch(NumberFormatException ne){
					btnStart.setText(msg + " is not a valid number format.");
				} catch (Exception se){
					btnStart.setText(se.getMessage());
				}
			}
		});
		configureFrame.getContentPane().add(btnStart, "cell 0 12 3 1,growx");
		configureFrame.setVisible(true);
	}

	
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		this.setVisible(true);

	}

	/**
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
	    //JFrame.setDefaultLookAndFeelDecorated(true);
        SwingUtilities.invokeLater(new GUISimulator("Car Simulator"));

	}
}
