/**
 * 
 * This file is part of the CarParkSimulator Project, written as 
 * part of the assessment for INB370, semester 1, 2014. 
 *
 * CarParkSimulator
 * asgn2Tests 
 * 19/05/2014/**
 * 
 * This file is part of the CarParkSimulator Project, written as 
 * part of the assessment for INB370, semester 1, 2014. 
 *
 * CarParkSimulator
 * asgn2Tests 
 * 19/05/2014
 * 
 * @author Chun Hung Chung
 * 
 */
package asgn2Tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import asgn2CarParks.CarPark;
import asgn2Exceptions.*;
import asgn2Simulators.Constants;
import asgn2Vehicles.*;

/**
 * This class includes unit testing for class CarPark.
 */
public class CarParkTests {

	CarPark park;
	MotorCycle mc;
	Car c;
	Car smallC;
	
	/**
	 * Construct car park with values before each test.
	 * @throws java.lang.Exception
	 * @author Chun Hung Chung
	 */
	@Before	
	public void setUp() throws Exception {
		//maxCarSpaces,maxSmallCarSpaces, maxMotorCycleSpaces, maxQueueSize
		park = new CarPark(200,50, 40, 20);
		mc = new MotorCycle("MC1",10);
		c = new Car("C1",10,false);
		smallC = new Car("C1",10,true);
	}

	/**
	 * Construct default value car park after each test.
	 * @throws java.lang.Exception
	 * @author Chun Hung Chung
	 */
	@After
	public void tearDown() throws Exception {
		park = new CarPark();
	}

	/**
	 * Test method for {@link asgn2CarParks.CarPark#archiveDepartingVehicles(int, boolean)}.
	 * 
	 * archiveDepartingVehiclesArchivedStateTest: set archived state for a vehicle,
	 * expected to throw a VehicleException
	 * 
	 * archiveDepartingVehiclesQueuedStateTest: set queued state for a vehicle,
	 * expected to throw a VehicleException
	 * 
	 * archiveDepartingVehiclesNewStateTest: set new arrival state for a vehicle,
	 * expected to throw a VehicleException
	 * 
	 * 
	 * 
	 * @author Chun Hung Chung
	 */
	@Test
	public void testArchiveDepartingVehicles() {
		fail("Not yet implemented"); // TODO
	}
	
	
	//?????????????????????????????
	@Test(expected = SimulationException.class)
	public void archiveDepartVehiclesNotInCarParkTest() {
		
	}
	
	
	
	
	

	/**
	 * Test method for {@link asgn2CarParks.CarPark#archiveNewVehicle(asgn2Vehicles.Vehicle)}.
	 * 
	 * archiveNewVehicleIsInQueueTest: set a vehicle in queued state,
	 * expected to throw a SimulationException
	 * 
	 * archiveNewVehicleIsInParkTest: set a vehicle in parked state,
	 * expected to throw a SimulationException
	 * 
	 * @author Chun Hung Chung
	 */
	@Test
	public void testArchiveNewVehicle() {
		fail("Not yet implemented"); // TODO
	}
	
	
	@Test(expected = SimulationException.class)
	public void archiveNewVehicleIsInQueueTest() {
		
	}
	@Test(expected = SimulationException.class)
	public void archiveNewVehicleIsInParkTest() {
		
	}
	
	
	

	/**
	 * Test method for {@link asgn2CarParks.CarPark#archiveQueueFailures(int)}.
	 * 
	 * archiveQueueFailuresInNewTest: set vehicle in new state,
	 * expected to throw a VehicleException 
	 * 
	 * archiveQueueFailuresInParkedTest: set vehicle in parked state,
	 * expected to throw a VehicleException 
	 * 
	 * archiveQueueFailuresInArchivedTest: set vehicle in parked state,
	 * expected to throw a VehicleException 
	 * 
	 * 
	 * 
	 * 
	 * @author Chun Hung Chung
	 */
	@Test
	public void testArchiveQueueFailures() {
		fail("Not yet implemented"); // TODO
	}

	
	
	
	
	
	
	
	/**
	 * Test method for {@link asgn2CarParks.CarPark#carParkEmpty()}.
	 * 
	 * Start with an empty car park, test if true.
	 * Then add vehicle into parking state, test if false.
	 * 
	 * @author Chun Hung Chung
	 */
	@Test 
	public void testCarParkEmpty() {
		//start...
		assertTrue("Fail to determine if car park is empty",park.carParkEmpty());		
		//TODO
		//add vehicle...
		assertFalse("Fail to determine if car park is empty",park.carParkEmpty());
	}

	
	
	/**
	 * Test method for {@link asgn2CarParks.CarPark#carParkFull()}.
	 * 
	 * Start with a not full car park, in this case, 200+50+40-1=289, test if false.
	 * Then add one vehicle to make it full, test if true. 
	 * 
	 * @author Chun Hung Chung
	 */
	@Test
	public void testCarParkFull() {
		// TODO
		//289vehicles in park...
		assertFalse("Fail to determine if car park is empty",park.carParkFull());
		//add vehicle..
		assertTrue("Fail to determine if car park is empty",park.carParkFull());
	}

	/**
	 * Test method for {@link asgn2CarParks.CarPark#enterQueue(asgn2Vehicles.Vehicle)}.
	 * 
	 * enterQueueOneVehicleTest: Try adding one car in the queue, check if there is only one car in queue.
	 * 
	 * enterQueueFulledTest: Create a full queue and try to enterQueue,
	 * expected to throw SimulationException
	 * 
	 */
	@Test
	public void testEnterQueue() {
		fail("Not yet implemented"); // TODO
		//add a vehicle into queue
		//check if the vehicle is in the queue
	}
			
	@Test
	public void enterQueueOneVehicleTest() throws SimulationException, VehicleException {		
		park.enterQueue(c);
		assertEquals("Fail to add vehicle into queue",1,park.numVehiclesInQueue());
	}	
	
	@Test
	public void enterQueueJustFitMaxQueueSizeTest() throws SimulationException, VehicleException {		
		//maxQueueSize = 20
		for (int i=1; i<21; i++) {
			park.enterQueue(c); //do 20 times
		}
		assertEquals("Fail to add correct numbers of vehicles into the queue",20,park.numVehiclesInQueue());
	}	
	
	@Test(expected = SimulationException.class)
	public void enterQueueWhenFulledTest() throws SimulationException, VehicleException {
		//max size of queue is 20
		for (int i = 1; i<22; i++) {
			park.enterQueue(c);//do 21 times
		}
	}

		
		
	/**
	 * Test method for {@link asgn2CarParks.CarPark#exitQueue(asgn2Vehicles.Vehicle, int)}.
	 */
	@Test
	public void testExitQueue() {
		fail("Not yet implemented"); // TODO
	}
	
	@Test
	public void exitQueuetest() throws SimulationException, VehicleException {
		park.enterQueue(c);
		park.enterQueue(c);
		//exitTime = arrivalTime + time under maximum queue time.
		park.exitQueue(c, 10+ Constants.MAXIMUM_QUEUE_TIME -1); 
		assertEquals("Fail to remove vehicle from queue", 1 ,park.numVehiclesInQueue());
	}
	
	@Test
	public void exitQueueExitCorrectVehicletest() throws SimulationException, VehicleException {
		//add two different vehicle in the queue
		park.enterQueue(c);
		park.enterQueue(mc);
		//remove the car but not motorcycle
		park.exitQueue(c, 10+ Constants.MAXIMUM_QUEUE_TIME -1); 
		assertEquals("", "MC1" ,mc.getVehID()); // TODO
	}
	

	@Test(expected = SimulationException.class)
	public void exitQueueInParkedTest() throws SimulationException, VehicleException {
		//try park a vehicle into car park
		try {park.parkVehicle(c, 10+ Constants.MAXIMUM_QUEUE_TIME -2, Constants.MINIMUM_STAY +1);} 
		//catch unexpected SimulationException thrown by parkVehicle
		catch (SimulationException e) {fail("No parking space available");}
		//try to exitQueue with the parked car
		park.exitQueue(c, 10+ Constants.MAXIMUM_QUEUE_TIME -1);
	}
	
	@Test(expected = SimulationException.class)
	public void exitQueueInArchivedTest() {
		//
		//put vehicle in archived status
		//try to exit the queue
	}
	@Test(expected = SimulationException.class)
	public void exitQueueNewArrivalTest() {
		//put vehicle in new arrival status
		//try to exit the queue
	}
	
	/**
	 * When car park containing only cars,
	 * test if parkVehicle method is able to count total number of cars in car park
	 * @throws VehicleException 
	 * @throws SimulationException 
	 */
	@Test
	public void parkVehicleCountingNumCarsOnlyCarTest() throws SimulationException, VehicleException {		
		park.parkVehicle(c, 10 +1, Constants.MINIMUM_STAY +1);
		park.parkVehicle(c, 10 +2, Constants.MINIMUM_STAY +1);		
		assertEquals("Not able to count cars in car park", 2, park.getNumCars());
	}	
	
	
	/**
	 * Test method for {@link asgn2CarParks.CarPark#getNumCars()}.
	 * When car park containing small cars and cars,
	 * test if parkVehicle method is able to count also small cars for total number of cars in car park
	 * @throws VehicleException 
	 * @throws SimulationException 
	 */
	@Test
	public void parkVehicleCountingNumCarsIncludeSmallCarTest() throws SimulationException, VehicleException {		
		park.parkVehicle(c, 10 +1, Constants.MINIMUM_STAY +1);
		park.parkVehicle(c, 10 +2, Constants.MINIMUM_STAY +1);
		park.parkVehicle(smallC, 10 +3, Constants.MINIMUM_STAY +1);				
		assertEquals("Number of cars did not include small cars", 3, park.getNumCars());
	}
	
	/**
	 * When car park containing car and motorcycle,
	 * test if parkVehicle method count only number of cars but not including motorcycle for numbers of cars.
	 * @throws SimulationException
	 * @throws VehicleException
	 */
	
	@Test
	public void parkVehicleCountingNumCarsIncludeMotorCycleTest() throws SimulationException, VehicleException {
		//parking one car and motorcycle
		park.parkVehicle(c, 10 +1, Constants.MINIMUM_STAY +1);
		park.parkVehicle(mc, 10 +1, Constants.MINIMUM_STAY +1);
		assertEquals("Number of cars have included motorcycle", 1, park.getNumCars());
	}
	

	/**
	 * Test method for {@link asgn2CarParks.CarPark#getNumMotorCycles()}.
	 * When car park containing only motorcycle,
	 * test if parkVehicle method is able to count total number of motorcycle in car park
	 * @throws VehicleException 
	 * @throws SimulationException 
	 */
	@Test
	public void parkVehicleCountingNumMotorCyclesTest() throws SimulationException, VehicleException {
		park.parkVehicle(mc, 10, Constants.MINIMUM_STAY +1);
		assertEquals("Not able to count number of motorcycles in car park correctly", 1, park.getNumMotorCycles());			
	}
	
	/**
	 * When car park containing motorcycles that exceeding the max capacity assigned for motorcycles,
	 * and there is motorcycle parked in small car space,
	 * test if parkVehicle method is able to count total number of motorcycle in car park
	 * @throws SimulationException
	 * @throws VehicleException
	 */
	@Test
	public void parkVehicleCountingNumMotorCyclesHavingMotorParkedInSmallCarSpace() throws SimulationException, VehicleException {
		//add motorcycles to fill up the spaces provided for motorcycle.
		for (int i = 0; i < 40; i++) {
			park.parkVehicle(mc, 10 + i, Constants.MINIMUM_STAY +1);
		}
		//add one more motorcycle
		park.parkVehicle(mc, 10 + 41, Constants.MINIMUM_STAY +1);
		assertEquals("Motorcycles parked in small car space is not counted", 41, park.getNumMotorCycles());		
	}

	/**
	 * When car park containing only small car,
	 * test if parkVehicle method is able to count total number of small cars in car park
	 * @throws VehicleException 
	 * @throws SimulationException 
	 */
	@Test
	public void parkVehicleCountingNumSmallCarsTest() throws SimulationException, VehicleException {		
		park.parkVehicle(smallC, 10 +1, Constants.MINIMUM_STAY +1);	
		assertEquals("Number of small cars have included cars", 1, park.getNumSmallCars());
	}	
	
	/**
	 * When car park containing car and small car,
	 * test if parkVehicle method is able to count total number of small cars in car park
	 * @throws VehicleException 
	 * @throws SimulationException 
	 */
	@Test
	public void parkVehicleCountingNumSmallCarsIncludeCarsTest() throws SimulationException, VehicleException {
		park.parkVehicle(c, 10 +1, Constants.MINIMUM_STAY +1);
		park.parkVehicle(smallC, 10 +2, Constants.MINIMUM_STAY +1);
		park.parkVehicle(smallC, 10 +3, Constants.MINIMUM_STAY +1);	
		assertEquals("Number of small cars have included cars", 2, park.getNumSmallCars());
	}
	
	/**
	 * When car park containing motorcycle and small car,
	 * test if parkVehicle method is able to count total number of small cars in car park
	 * @throws VehicleException 
	 * @throws SimulationException 
	 */
	@Test
	public void parkVehicleCountingNumSmallCarsIncludeMotorCycleTest() throws SimulationException, VehicleException {
		park.parkVehicle(mc, 10 +1, Constants.MINIMUM_STAY +1);
		park.parkVehicle(smallC, 10 +2, Constants.MINIMUM_STAY +1);
		park.parkVehicle(smallC, 10 +3, Constants.MINIMUM_STAY +1);	
		assertEquals("Number of small cars have included motorcycle", 2, park.getNumSmallCars());
	}
	
	/**
	 * Test method for {@link asgn2CarParks.CarPark#parkVehicle(asgn2Vehicles.Vehicle, int, int)}.
	 */
	@Test
	public void testParkVehicle() {
		fail("Not yet implemented"); // TODO
		//something about no space available exception
	}
	
	
	
	

	/**
	 * Test method for {@link asgn2CarParks.CarPark#getStatus(int)}.
	 */
	@Test
	public void testGetStatus() {
		assertTrue(true);
	}

	/**
	 * Test method for {@link asgn2CarParks.CarPark#initialState()}.
	 */
	@Test
	public void testInitialState() {
		assertTrue(true);
	}

	/**
	 * Test method for {@link asgn2CarParks.CarPark#numVehiclesInQueue()}.
	 */
	@Test
	public void testNumVehiclesInQueue() {
		assertTrue(true);
	}
	
	
	

	/**
	 * Test method for {@link asgn2CarParks.CarPark#processQueue(int, asgn2Simulators.Simulator)}.
	 */
	@Test
	public void testProcessQueue() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link asgn2CarParks.CarPark#queueEmpty()}.
	 * Test if queueEmpty method is able to show if queue is empty or not
	 * @throws VehicleException 
	 * @throws SimulationException 
	 */
	@Test
	public void testQueueEmpty() throws SimulationException, VehicleException {		
		assertTrue("Fail to show queue is empty",park.queueEmpty());
		park.enterQueue(c);
		assertFalse("Fail to show queue is not empty",park.queueEmpty());
	}

	/**
	 * Test method for {@link asgn2CarParks.CarPark#queueFull()}.
	 * @throws VehicleException 
	 * @throws SimulationException 
	 */
	@Test
	public void testQueueFull() throws SimulationException, VehicleException {		
		park.enterQueue(c);
		assertFalse("Fail to show queue is not full",park.queueFull());
		//add 19 more to fill up the queue
		for (int i = 1; i < 20; i++){
			park.enterQueue(c);
		}
		assertTrue("Fail to show the queue is full",park.queueFull());
	}

	/**
	 * Test method for {@link asgn2CarParks.CarPark#spacesAvailable(asgn2Vehicles.Vehicle)}.
	 */
	@Test
	public void testSpacesAvailable() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link asgn2CarParks.CarPark#toString()}.
	 */
	@Test
	public void testToString() {
		assertTrue(true);
	}

	/**
	 * Test method for {@link asgn2CarParks.CarPark#tryProcessNewVehicles(int, asgn2Simulators.Simulator)}.
	 */
	@Test
	public void testTryProcessNewVehicles() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link asgn2CarParks.CarPark#unparkVehicle(asgn2Vehicles.Vehicle, int)}.
	 */
	@Test
	public void testUnparkVehicle() {
		fail("Not yet implemented"); // TODO
	}

}
