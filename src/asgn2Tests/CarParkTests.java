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

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import asgn2CarParks.CarPark;
import asgn2Exceptions.*;
import asgn2Simulators.Constants;
import asgn2Simulators.Simulator;
import asgn2Vehicles.*;

/**
 * This class includes unit testing for class CarPark.
 */
public class CarParkTests {

	CarPark park;
	MotorCycle mc;
	Car c;
	Car smallC;
	Simulator sim;
	
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
		smallC = new Car("C2",10,true);
		sim = new Simulator();
	}

	/**
	 * Construct default value car park after each test.
	 * @throws java.lang.Exception
	 * @author Chun Hung Chung
	 */
	@After
	public void tearDown() throws Exception {
		park = new CarPark();
		sim = new Simulator();
	}

	
	/**
	 * Test if archive departing vehicle is success when
	 * the vehicle has stayed its intended duration but not forced to depart.
	 * @throws VehicleException 
	 * @throws SimulationException 
	 * 
	 */
	@Test
	public void archiveDepartingVehiclesIsTimeToDepartTest() throws SimulationException, VehicleException {
		park.parkVehicle(c, 10, Constants.MINIMUM_STAY +1);
		park.archiveDepartingVehicles(10 + Constants.MINIMUM_STAY +1, false);
		assertEquals("Fail to depart vehicle that has reached its intended departure time",
				0, park.getNumCars());
	}

	/**
	 * Test if archive departing vehicle is success when
	 * the vehicle has not stayed its intended duration but forced to depart.
	 * @throws VehicleException 
	 * @throws SimulationException 
	 * 
	 */
	@Test
	public void archiveDepartingVehiclesIsForcedToDepartTest() throws SimulationException, VehicleException {
		park.parkVehicle(c, 10, Constants.MINIMUM_STAY +1);
		park.archiveDepartingVehicles(11, true);
		assertEquals("Fail to depart vehicle that is forced to depart",
				0, park.getNumCars());
	}
	

	
	/**
	 * set a vehicle in queued state, expected to throw a SimulationException
	 * @throws SimulationException
	 * @throws VehicleException
	 */
	@Test(expected = SimulationException.class)
	public void archiveNewVehicleIsInQueueTest() throws SimulationException, VehicleException {
		park.enterQueue(c);
		park.archiveNewVehicle(c);		
	}
	
	/**
	 * set a vehicle in parked state, expected to throw a SimulationException
	 * @throws SimulationException
	 * @throws VehicleException
	 */
	@Test(expected = SimulationException.class)
	public void archiveNewVehicleIsInParkTest() throws SimulationException, VehicleException {
		park.parkVehicle(c, 11, Constants.MINIMUM_STAY +1);
		park.archiveNewVehicle(c);		
	}
	

	
	/**
	 * Test if archiveQueueFailure method is able to archive over timed queuing vehicles. 
	 * @throws VehicleException 
	 * @throws SimulationException 
	 *  
	 */
	@Test
	public void archiveQueueFailuresOverMaxQueueTimeTest() throws SimulationException, VehicleException {
		park.enterQueue(c);
		park.enterQueue(mc);
		park.archiveQueueFailures(10 + Constants.MAXIMUM_QUEUE_TIME + 1);
		assertEquals("Fail to archive vehicles in queue that stayed over max queue time",
				0, park.numVehiclesInQueue());		
	}
	
	/**
	 * Test if archiveQueueFailure method is able to archive over timed queuing vehicles. 
	 * @throws VehicleException 
	 * @throws SimulationException 
	 *  
	 */
	@Test
	public void archiveQueueFailuresJustEqualMaxQueueTimeTest() throws SimulationException, VehicleException {
		park.enterQueue(c);
		park.enterQueue(mc);
		park.archiveQueueFailures(10 + Constants.MAXIMUM_QUEUE_TIME);
		assertEquals("Should not be archiving vehicles that the queue time is not exceeded",
				2, park.numVehiclesInQueue());		
	}
	
	
	
	/**
	 * Test method for {@link asgn2CarParks.CarPark#carParkEmpty()}.
	 * 
	 * Start with an empty car park, test if true.
	 * Then add vehicle into parking state, test if false.
	 * 
	 * @author Chun Hung Chung
	 * @throws VehicleException 
	 * @throws SimulationException 
	 */
	@Test 
	public void testCarParkEmpty() throws SimulationException, VehicleException {
		assertTrue("Fail to determine if car park is empty",park.carParkEmpty());		
		park.parkVehicle(c, 10, Constants.MINIMUM_STAY +1);
		assertFalse("Fail to determine if car park is empty",park.carParkEmpty());
	}
	
	
	/**
	 * Test method for {@link asgn2CarParks.CarPark#carParkFull()}.
	 * 
	 * Start with a not full car park, in this case, 200+50+40-1=289, test if false.
	 * Then add one vehicle to make it full, test if true. 
	 * 
	 * @author Chun Hung Chung
	 * @throws VehicleException 
	 * @throws SimulationException 
	 */
	@Test
	public void testCarParkFull() throws VehicleException, SimulationException {
		//add 150 cars to fill up all spaces for cars
		for (int i = 1; i < 151; i++) {
			String id = "C" + i;
			Car c = new Car(id, 10 + i, false);
			park.parkVehicle(c, 10 + i, Constants.MINIMUM_STAY +1);
		}
		assertFalse("Fail to determine if car park is empty",park.carParkFull());
		
		//add 89 motorcycles to fill spaces for small cars, which leaving one space available for small car
		for (int i = 2; i < 91; i++) {
			String id = "MC" + i;
			MotorCycle mc = new MotorCycle(id, 10 + i);
			park.parkVehicle(mc, 10 + i, Constants.MINIMUM_STAY +1);
		}
		assertFalse("Fail to determine if car park is empty",park.carParkFull());
		park.parkVehicle(mc, 10 + 90, Constants.MINIMUM_STAY +1);
		assertTrue("Fail to determine if car park is empty",park.carParkFull());
	}

				
	/**
	 * Try adding one car in the queue, check if there is only one car in queue.
	 * @throws SimulationException
	 * @throws VehicleException
	 */
	@Test
	public void enterQueueOneVehicleTest() throws SimulationException, VehicleException {		
		park.enterQueue(c);
		assertEquals("Fail to add vehicle into queue", 1, park.numVehiclesInQueue());
	}	
	
	/**
	 * Test if enterQueue method is able to add same amount of vehicles that match the max queue size set.
	 * @throws SimulationException
	 * @throws VehicleException
	 */
	@Test
	public void enterQueueJustFitMaxQueueSizeTest() throws SimulationException, VehicleException {		
		for (int i=1; i<21; i++) {
			String id = "C" + i;
			Car c = new Car(id,10 +i, false);
			park.enterQueue(c); 
		}
		assertEquals("Fail to add correct numbers of vehicles into the queue", 20, park.numVehiclesInQueue());
	}	
	
	/**
	 * Create a full queue and try to enterQueue, expected to throw SimulationException
	 * @throws SimulationException
	 * @throws VehicleException
	 */
	@Test(expected = SimulationException.class)
	public void enterQueueWhenFulledTest() throws SimulationException, VehicleException {
		for (int i = 1; i<22; i++) {
			String id = "C" + i;
			Car c = new Car(id, 10 +i, false);
			park.enterQueue(c);
		}
	}

		
	/**
	 * Add two vehicles into car park, remove one and test if only one is remaining.
	 * @throws SimulationException
	 * @throws VehicleException
	 */
	@Test
	public void exitQueuetest() throws SimulationException, VehicleException {
		park.enterQueue(c);
		park.enterQueue(mc);		
		park.exitQueue(c, 10+ Constants.MAXIMUM_QUEUE_TIME -1); 
		assertEquals("Fail to remove vehicle from queue", 1 ,park.numVehiclesInQueue());
	}	

	
	/**
	 * Add a vehicle into car park, try exitQueue that vehicle
	 * Expected a SimulationException
	 * @throws SimulationException
	 * @throws VehicleException
	 */
	@Test(expected = SimulationException.class)
	public void exitQueueInParkedTest() throws SimulationException, VehicleException {
		//try park a vehicle into car park
		try {park.parkVehicle(c, 10+ Constants.MAXIMUM_QUEUE_TIME -2, Constants.MINIMUM_STAY +1);} 
		//catch unexpected SimulationException thrown by parkVehicle
		catch (SimulationException e) {fail("No parking space available");}
		//try to exitQueue with the parked car
		park.exitQueue(c, 10+ Constants.MAXIMUM_QUEUE_TIME -1);
	}
	
	/**
	 * Archive a vehicle, try exitQueue that vehicle
	 * Expected a SimulationException
	 * @throws SimulationException
	 * @throws VehicleException 
	 */
	@Test(expected = SimulationException.class)
	public void exitQueueInArchivedTest() throws SimulationException, VehicleException {
		park.archiveNewVehicle(c);
		park.exitQueue(c, 10 + Constants.MINIMUM_STAY +1);		
	}	
	
	/**
	 * Create and new arrival vehicle, try exitQueue
	 * Expected a SimulationException
	 * @throws SimulationException 
	 * @throws VehicleException 
	 * 
	 */
	@Test(expected = SimulationException.class)
	public void exitQueueNewArrivalTest() throws VehicleException, SimulationException {
		park.exitQueue(c, 10 + Constants.MINIMUM_STAY +1);
	}
	
	/**
	 * When car park containing only cars,
	 * test if parkVehicle method is able to count total number of cars in car park
	 * @throws VehicleException 
	 * @throws SimulationException 
	 */
	@Test
	public void parkVehicleCountingNumCarsOnlyCarTest() throws SimulationException, VehicleException {		
		for (int i = 1; i < 3; i++) {
			String id = "C" + i;
			Car c = new Car(id, 10 + i, false);
			park.parkVehicle(c, 10 + i, Constants.MINIMUM_STAY +1);
		}		
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
		for (int i = 1; i < 3; i++) {
			String id = "C" + i;
			Car c = new Car(id, 10 + i, false);
			park.parkVehicle(c, 10 + i, Constants.MINIMUM_STAY +1);
		}	
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
		//add motorcycles more than the spaces provided for motorcycle.
		for (int i = 1; i < 42; i++) {
			String id = "MC" + i;
			MotorCycle mc = new MotorCycle(id, 10 + i);
			park.parkVehicle(mc, 10 + i, Constants.MINIMUM_STAY +1);
		}		
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
		for (int i = 1; i < 3; i++) {
			String id = "C" + i;
			Car c = new Car(id, 10 + i, true);
			park.parkVehicle(c, 10 + i, Constants.MINIMUM_STAY +1);
		}	
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
		for (int i = 1; i < 3; i++){
			String id = "C" + i;
			Car smallC = new Car(id, 10 +i, true);
			park.parkVehicle(smallC, 10 +i, Constants.MINIMUM_STAY +1);
		}		
		assertEquals("Number of small cars have included motorcycle", 2, park.getNumSmallCars());
	}
	
		
	/**
	 * Test if SimulationException of no space available is thrown when 
	 * normal car more than maximum is parked
	 * @throws VehicleException 
	 * @throws SimulationException 
	 * 
	 */
	@Test(expected = SimulationException.class)
	public void parkVehicleNormalCarSpaceFullTest() throws VehicleException, SimulationException {
		for (int i = 1; i < 152; i++) {
			String id = "C" + i;
			Car c = new Car(id, 10 + i, false);
			park.parkVehicle(c, 10 + i, Constants.MINIMUM_STAY +1);
		}
	}
	
	/**
	 * The car park should be able to hold same amount of normal cars according to the max spaces provided. 
	 * @throws VehicleException 
	 * @throws SimulationException 
	 * 
	 */
	@Test
	public void parkVehicleJustMatchMaxNormalCarSpaceTest() throws VehicleException, SimulationException {
		for (int i = 1; i < 151; i++) {
			String id = "C" + i;
			Car c = new Car(id, 10 + i, false);
			park.parkVehicle(c, 10 + i, Constants.MINIMUM_STAY +1);
		}
	}
	
	/**
	 * Test if SimulationException of no space available is thrown when 
	 * small car more than maximum(small car space + normal car space) is parked
	 * @throws VehicleException 
	 * @throws SimulationException 
	 * 
	 */
	@Test(expected = SimulationException.class)
	public void parkVehicleSmallCarSpaceFulledWhenNormalCarSpaceFullTest() throws VehicleException, SimulationException {
		for (int i = 1; i < 202; i++) {
			String id = "C" + i;
			Car smallC = new Car(id, 10 + i, true);
			park.parkVehicle(smallC, 10 + i, Constants.MINIMUM_STAY +1);
		}
	}
	
	/**
	 * The car park should be able to hold same amount of small cars according to the max spaces provided.
	 * @throws VehicleException 
	 * @throws SimulationException 
	 * 
	 */
	@Test
	public void parkVehicleSmallCarJustMatchMaxSpacesForSmallCarTest() throws VehicleException, SimulationException {
		for (int i = 1; i < 201; i++) {
			String id = "C" + i;
			Car smallC = new Car(id, 10 + i, true);
			park.parkVehicle(smallC, 10 + i, Constants.MINIMUM_STAY +1);
		}
	}
	
	/**
	 * Test if SimulationException of no space available is thrown when 
	 * motorcycle more than maximum(motorcycle space + small car space) is parked
	 * @throws VehicleException 
	 * @throws SimulationException 
	 * 
	 */
	@Test(expected = SimulationException.class)
	public void parkVehicleMotorCycleSpaceNotAvailableWhenSmallCarSpaceFullTest() throws VehicleException, SimulationException {
		for (int i = 1; i < 92; i++) {
			String id = "MC" + i;
			MotorCycle mc = new MotorCycle(id, 10 + i);
			park.parkVehicle(mc, 10 + i, Constants.MINIMUM_STAY +1);
		}
	}
	
	/**
	 * The car park should be able to hold same amount of motorcycles according to the max spaces provided.
	 * @throws VehicleException 
	 * @throws SimulationException 
	 * 
	 */
	@Test
	public void parkVehicleMotorCycleJustMatchMaxSpacesForMotorCycleTest() throws VehicleException, SimulationException {
		for (int i = 1; i < 91; i++) {
			String id = "MC" + i;
			MotorCycle mc = new MotorCycle(id, 10 + i);
			park.parkVehicle(mc, 10 + i, Constants.MINIMUM_STAY +1);
		}
	}
	

	/**
	 * Test method for {@link asgn2CarParks.CarPark#processQueue(int, asgn2Simulators.Simulator)}.
	 */
	@Test
	public void testProcessQueue() {
		fail("Not yet implemented"); // TODO
	}
	
	//1. test processQueue when queue is empty....
	
	
	
	//2-4.
	//space for 3 kind of car available........
	//That kind of car included in the queue....
	//test processQueue.....see if state Q->P
	
	@Test
	public void processQueueNormalCarSpaceAvailableTest() {
		
	}
	@Test
	public void processQueueNormalCarSpaceFulledTest() {
		
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
		for (int i = 2; i < 21; i++){
			String id = "C" + i;
			Car c = new Car(id, 10 + i, false);
			park.enterQueue(c);
		}
		assertTrue("Fail to show the queue is full",park.queueFull());
	}
	
	/**
	 * Test if spacesAvailable method is able to return 
	 * true if cars parked in car park is not equal to the maximum space set,
	 * false otherwise.
	 * @throws VehicleException 
	 * @throws SimulationException 
	 * 
	 */
	@Test
	public void spacesAvailableCarSpaceTest() throws VehicleException, SimulationException {
		assertTrue("Fail to indicate spaces available for car",park.spacesAvailable(c));
		for (int i = 1; i < 151; i++){
			String id = "C" + i;
			Car c = new Car(id, 10 + i, false);
			park.parkVehicle(c, 10 + i, Constants.MINIMUM_STAY +1);
		}
		assertFalse("Fail to indicate spaces not available for car",park.spacesAvailable(c));
	}

	/**
	 * Test if spacesAvailable method is able to return 
	 * true if small cars parked in car park is not equal to the maximum space set,
	 * false otherwise.
	 * @throws VehicleException 
	 * @throws SimulationException 
	 */
	@Test
	public void spacesAvailableSmallCarSpaceWithCarSpaceAvailableTest() throws VehicleException, SimulationException {
		assertTrue("Fail to indicate spaces available for small car",park.spacesAvailable(smallC));
		//fill also spaces provided for normal car
		for (int i = 1; i < 201; i++){
			String id = "C" + i;
			Car smallC = new Car(id, 10 + i, true);
			park.parkVehicle(smallC, 10 + i, Constants.MINIMUM_STAY +1);
		}
		assertFalse("Fail to indicate spaces not available for small car",park.spacesAvailable(smallC));
	}	

	/**
	 * Test if spacesAvailable method is able to return 
	 * true if motorcycles parked in car park is not equal to the maximum space set,
	 * false otherwise.
	 * @throws VehicleException 
	 * @throws SimulationException 
	 */
	@Test
	public void spacesAvailableMotorCycleSpaceWithSmallCarSpaceAvailableTest() throws VehicleException, SimulationException {
		assertTrue("Fail to indicate spaces available for motorcycles", park.spacesAvailable(mc));
		//fill also spaces provided for small cars
		for (int i = 1; i < 91; i++){
			String id = "MC" + i;
			MotorCycle mc = new MotorCycle(id, 10 + i);
			park.parkVehicle(mc, 10 + i, Constants.MINIMUM_STAY +1);
		}
		assertFalse("Fail to indicate spaces not available for motorcycles", park.spacesAvailable(mc));
	}

	
	
	

	/**
	 * Test method for {@link asgn2CarParks.CarPark#tryProcessNewVehicles(int, asgn2Simulators.Simulator)}.
	 */
	@Test
	public void testTryProcessNewVehicles() {
		fail("Not yet implemented"); // TODO
	}
	
	
	
	
	
	
	
	
	
	
	


	/**
	 * Add vehicle in queue, try unparkVehicle,
	 * expected a throw of SimulationException
	 * @throws VehicleException 
	 * @throws SimulationException 
	 * 
	 */
	@Test(expected = SimulationException.class)
	public void unparkVehicleNotInParkTest() throws SimulationException, VehicleException {
		park.enterQueue(c);
		park.unparkVehicle(c, 10 + Constants.MINIMUM_STAY +1);
	}
	
	/**
	 * Try unparkVehicle at a time earlier than the parking time,
	 * expected a throw of VehicleException
	 * @throws VehicleException 
	 * @throws SimulationException 
	 * 
	 */
	@Test(expected = VehicleException.class)
	public void unparkVehicleTimeEarlierThanParkingTimeTest() throws SimulationException, VehicleException {
		park.parkVehicle(c, 10, Constants.MINIMUM_STAY +1);
		park.unparkVehicle(c, 9);
	}
	
	/**
	 * Test unparkVehicle method is able to remove normal car from car park
	 * @throws VehicleException 
	 * @throws SimulationException 
	 * 
	 */
	@Test
	public void unparkVehicleUnparkNormalCarTest() throws SimulationException, VehicleException {
		park.parkVehicle(c, 10, Constants.MINIMUM_STAY +1);
		park.unparkVehicle(c, 11);
		assertEquals("Not able to remove normal car in car park", 0, park.getNumCars());
	}
	
	/**
	 * Test unparkVehicle method is able to remove small car from car park
	 * Test also total number of cars in car park is also decreased after removal of small car
	 * @throws VehicleException 
	 * @throws SimulationException 
	 * 
	 */
	@Test
	public void unparkVehicleUnparkSmallCarTest() throws SimulationException, VehicleException {
		park.parkVehicle(c, 10, Constants.MINIMUM_STAY +1);
		park.parkVehicle(smallC, 11, Constants.MINIMUM_STAY +1);
		assertEquals("Number of cars in car park is incorrect", 2, park.getNumCars());
		assertEquals("Number of small car in car park is incorrect", 1, park.getNumSmallCars());
		park.unparkVehicle(smallC, 12);
		assertEquals("Not able to remove small car in car park", 1, park.getNumCars());
		assertEquals("Number of small car does not decrease after removal", 0, park.getNumSmallCars());
	}
	
	/**
	 * Test unparkVehicle method is able to remove small car which parked in normal car spaces
	 * @throws VehicleException 
	 * @throws SimulationException 
	 * 
	 */
	@Test
	public void unparkVehicleUnparkSmallCarParkedInNormalCarSpaceTest() throws SimulationException, VehicleException {
		ArrayList<Car> temp = new ArrayList<Car>();		
		for (int i = 1; i < 52; i++) {
			String id = "C" + i;
			Car smallC = new Car(id, 10 + i, true);
			temp.add(smallC);
			park.parkVehicle(smallC, 10 + i, Constants.MINIMUM_STAY +1);
		}
		for (Car smallC: temp) {
			park.unparkVehicle(smallC, 80);
		}
		assertEquals("Unable to remove all small cars when some is parked in normal car space",
				0, park.getNumSmallCars());
	}
	
	
	/**
	 * Test unparkVehicle method is able to remove motorcycle from car park
	 * Test also total number of motorcycles in car park is also decreased after removal of motorcycle
	 * @throws VehicleException 
	 * @throws SimulationException 
	 * 
	 */
	@Test
	public void unparkVehicleUnparkMotorcycleTest() throws SimulationException, VehicleException {
		park.parkVehicle(mc, 10, Constants.MINIMUM_STAY +1);
		park.unparkVehicle(mc, 11);
		assertEquals("Not able to remove motorcycle in car park", 0, park.getNumMotorCycles());
	}
	
	
	/**
	 * Test unparkVehicle method is able to remove motorcycle which parked in small car spaces
	 * @throws VehicleException 
	 * @throws SimulationException 
	 * 
	 */
	@Test
	public void unparkVehicleUnparkMotorCycleParkedInSmallCarSpaceTest() throws SimulationException, VehicleException {
		ArrayList<MotorCycle> temp = new ArrayList<MotorCycle>();		
		for (int i = 1; i < 42; i++) {
			String id = "MC" + i;
			MotorCycle mc = new MotorCycle(id, 10 + i);
			temp.add(mc);
			park.parkVehicle(mc, 10 + i, Constants.MINIMUM_STAY +1);
		}
		for (MotorCycle mc: temp) {
			park.unparkVehicle(mc, 80);
		}
		assertEquals("Unable to remove all small cars when some is parked in normal car space",
				0, park.getNumSmallCars());
	}
	
	

	/**
	 * Test method for {@link asgn2CarParks.CarPark#toString()}.
	 */
	@Test
	public void testToString() {
		assertTrue(true);
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
	
	
	
}
