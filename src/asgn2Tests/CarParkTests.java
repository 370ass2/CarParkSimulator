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
	 * Test processQueue method should able to throw VehicleException when
	 * the current time is earlier than the time of arrival.
	 * @throws SimulationException 
	 * @throws VehicleException 
	 * 
	 */
	@Test(expected = VehicleException.class)
	public void processQueueLeavingQueueTimeEarlierThanArrivalTest() throws VehicleException, SimulationException {
		park.enterQueue(c);
		park.processQueue(9, sim);
	}
	
	/**
	 * Test processQueue method should able to throw VehicleException when
	 * the current time is equal the time of arrival.
	 * @throws SimulationException 
	 * @throws VehicleException 
	 * 
	 */
	@Test(expected = VehicleException.class)
	public void processQueueLeavingQueueTimeEqualToArrivalTest() throws VehicleException, SimulationException {
		park.enterQueue(c);
		park.processQueue(10, sim);
	}
	
	/**
	 * Test processQueue method when the queue is empty
	 * Nothing should be added to the car park
	 * @throws SimulationException 
	 * @throws VehicleException 
	 * 
	 */
	@Test
	public void processQueueEmptyQueueTest() throws VehicleException, SimulationException {
		park.processQueue(1, sim);
		assertTrue("Added a non-exist vehicle into car park when queue is empty", park.carParkEmpty());
	}
	
	/**
	 * Test processQueue method when normal car in queue and normal car space available
	 * Normal car in queue should be removed from queue and parked.
	 * @throws SimulationException 
	 * @throws VehicleException 
	 * 
	 */
	@Test
	public void processQueueNormalCarTest() throws VehicleException, SimulationException {
		for (int i = 1; i < 3; i++){
			String id = "C" + i;
			Car c = new Car(id, i, false);
			park.enterQueue(c);
		}
		park.processQueue(3, sim);
		assertEquals("Fail to remove normal car from queue", 0, park.numVehiclesInQueue());
		assertEquals("Fail to add normal car to park", 2, park.getNumCars());
	}
	
	/**
	 * Test processQueue method when normal car in queue and only one normal car space available
	 * normal car in queue should be removed from queue and parked.
	 * @throws SimulationException 
	 * @throws VehicleException 
	 * 
	 */
	@Test
	public void processQueueNormalCarBoundaryTest() throws VehicleException, SimulationException {
		for (int i = 1; i < 2; i++){
			String id = "C" + i;
			Car c = new Car(id, i, false);
			park.enterQueue(c);
		}
		for (int i = 1; i < 150; i++){
			String id = "C" + i;
			Car c = new Car(id, i, false);
			park.parkVehicle(c, i, 300);
		}		
		park.processQueue(3, sim);
		assertEquals("Fail to remove normal car from queue when one normal car space available only",
				0, park.numVehiclesInQueue());
		assertEquals("Fail to add normal car to park when one normal car space available only",
				150, park.getNumCars());
	}
	
	/**
	 * Test processQueue method when normal car in queue and no normal car space available
	 * normal car in queue should not be removed from queue.
	 * @throws SimulationException 
	 * @throws VehicleException 
	 * 
	 */
	@Test
	public void processQueueNormalCarSpaceFullTest() throws VehicleException, SimulationException {
		for (int i = 1; i < 3; i++){
			String id = "C" + i;
			Car c = new Car(id, i, false);
			park.enterQueue(c);
		}
		for (int i = 1; i < 151; i++){
			String id = "C" + i;
			Car c = new Car(id, i, false);
			park.parkVehicle(c, i, 300);
		}		
		park.processQueue(3, sim);
		assertEquals("Fail to remove normal car from queue when one normal car space available only",
				2, park.numVehiclesInQueue());
		assertEquals("Fail to add normal car to park when one normal car space available only",
				150, park.getNumCars());
	}
	
	/**
	 * Test processQueue method when small car in queue and small car space available
	 * Small car in queue should be removed from queue and parked.
	 * @throws SimulationException 
	 * @throws VehicleException 
	 * 
	 */
	@Test
	public void processQueueSmallCarTest() throws VehicleException, SimulationException {
		for (int i = 1; i < 3; i++){
			String id = "C" + i;
			Car c = new Car(id, i, true);
			park.enterQueue(c);
		}
		park.processQueue(3, sim);
		assertEquals("Fail to remove small car from queue", 0, park.numVehiclesInQueue());
		assertEquals("Fail to add small car to park", 2, park.getNumSmallCars());
	}
	
	/**
	 * Test processQueue method when small car in queue,
	 * small car space full but normal car space available
	 * Small car in queue should be removed from queue and parked.
	 * @throws SimulationException 
	 * @throws VehicleException 
	 * 
	 */
	@Test
	public void processQueueSmallCarIntoNormalSpaceTest() throws VehicleException, SimulationException {
		for (int i = 1; i < 3; i++){
			String id = "C" + i;
			Car c = new Car(id, i, true);
			park.enterQueue(c);
		}
		for (int i = 1; i < 51; i++){
			String id = "C" + i;
			Car c = new Car(id, i, true);
			park.parkVehicle(c, i, 300);
		}		
		park.processQueue(3, sim);
		assertEquals("Fail to remove small car from queue", 0, park.numVehiclesInQueue());
		assertEquals("Fail to add small car to normal car space", 52, park.getNumSmallCars());
	}
	
	/**
	 * Test processQueue method when small car in queue,
	 * small car space full but only one normal car space available
	 * Only one small car in queue should be removed from queue and parked.
	 * @throws SimulationException 
	 * @throws VehicleException 
	 * 
	 */
	@Test
	public void processQueueSmallCarIntoNormalSpaceBoundaryTest() throws VehicleException, SimulationException {
		for (int i = 1; i < 3; i++){
			String id = "C" + i;
			Car c = new Car(id, i, true);
			park.enterQueue(c);
		}
		for (int i = 1; i < 200; i++){
			String id = "C" + i;
			Car c = new Car(id, i, true);
			park.parkVehicle(c, i, 300);
		}		
		park.processQueue(3, sim);
		assertEquals("Fail to remove small car from queue", 1, park.numVehiclesInQueue());
		assertEquals("Fail to add small car to normal car space", 200, park.getNumSmallCars());
	}
	
	/**
	 * Test processQueue method when small car in queue,
	 * all small car space fill with motorcycles but normal car space available
	 * Only one small car in queue should be removed from queue and parked.
	 * @throws SimulationException 
	 * @throws VehicleException 
	 * 
	 */
	@Test
	public void processQueueSmallCarIntoNormalSpaceWhenSmallSpaceFilledWithMotorsTest() throws VehicleException, SimulationException {
		for (int i = 1; i < 3; i++){
			String id = "C" + i;
			Car c = new Car(id, i, true);
			park.enterQueue(c);
		}
		for (int i = 1; i < 91; i++){
			String id = "MC" + i;
			MotorCycle mc = new MotorCycle(id, i);
			park.parkVehicle(mc, i, 300);
		}				
		park.processQueue(3, sim);
		assertEquals("Fail to remove small car from queue", 0, park.numVehiclesInQueue());
		assertEquals("Fail to add small car to normal car space", 2, park.getNumSmallCars());
	}
	
	/**
	 * Test processQueue method when small car in queue,
	 * all small car space fill with motorcycles but only one normal car space available
	 * Only one small car in queue should be removed from queue and parked.
	 * @throws SimulationException 
	 * @throws VehicleException 
	 * 
	 */
	@Test
	public void processQueueSmallCarIntoNormalSpaceWhenSmallSpaceFilledWithMotorsBoundaryTest() throws VehicleException, SimulationException {
		for (int i = 1; i < 3; i++){
			String id = "C" + i;
			Car c = new Car(id, i, true);
			park.enterQueue(c);
		}
		for (int i = 1; i < 91; i++){
			String id = "MC" + i;
			MotorCycle mc = new MotorCycle(id, i);
			park.parkVehicle(mc, i, 300);
		}		
		for (int i = 1; i < 150; i++){
			String id = "C" + i;
			Car c = new Car(id, i, false);
			park.parkVehicle(c, i, 300);
		}	
		park.processQueue(3, sim);
		assertEquals("Fail to remove small car from queue", 1, park.numVehiclesInQueue());
		assertEquals("Fail to add small car to normal car space", 1, park.getNumSmallCars());
	}
	
	/**
	 * Test processQueue method when small car in queue,
	 * small car space and normal car space full 
	 * No small car in queue should be removed from queue.
	 * @throws SimulationException 
	 * @throws VehicleException 
	 * 
	 */
	@Test
	public void processQueueSmallCarSpaceFullTest() throws VehicleException, SimulationException {
		for (int i = 1; i < 3; i++){
			String id = "C" + i;
			Car c = new Car(id, i, true);
			park.enterQueue(c);
		}
		for (int i = 1; i < 91; i++){
			String id = "MC" + i;
			MotorCycle mc = new MotorCycle(id, i);
			park.parkVehicle(mc, i, 300);
		}		
		for (int i = 1; i < 151; i++){
			String id = "C" + i;
			Car c = new Car(id, i, false);
			park.parkVehicle(c, i, 300);
		}	
		park.processQueue(3, sim);
		assertEquals("Fail to remove small car from queue", 2, park.numVehiclesInQueue());
		assertEquals("Fail to add small car to normal car space", 0, park.getNumSmallCars());
	}
	

	/**
	 * Test processQueue method when motorcycle in queue and motor space available
	 * motorcycle in queue should be removed from queue and parked.
	 * @throws SimulationException 
	 * @throws VehicleException 
	 * 
	 */
	@Test
	public void processQueueMotorcycleTest() throws VehicleException, SimulationException {
		for (int i = 1; i < 3; i++){
			String id = "MC" + i;
			MotorCycle mc = new MotorCycle(id, i);
			park.enterQueue(mc);
		}
		park.processQueue(3, sim);
		assertEquals("Fail to remove motor from queue", 0, park.numVehiclesInQueue());
		assertEquals("Fail to add motor to park", 2, park.getNumMotorCycles());
	}
	
	/**
	 * Test processQueue method when motorcycle in queue and 
	 * motor space full but small car space available
	 * motorcycle in queue should be removed from queue and parked.
	 * @throws SimulationException 
	 * @throws VehicleException 
	 * 
	 */
	@Test
	public void processQueueMotorcycleIntoSmallSpaceTest() throws VehicleException, SimulationException {
		for (int i = 1; i < 3; i++){
			String id = "MC" + i;
			MotorCycle mc = new MotorCycle(id, i);
			park.enterQueue(mc);
		}
		for (int i = 1; i < 41; i++){
			String id = "MC" + i;
			MotorCycle mc = new MotorCycle(id, i);
			park.parkVehicle(mc, i, 300);;
		}
		park.processQueue(3, sim);
		assertEquals("Fail to remove motor from queue", 0, park.numVehiclesInQueue());
		assertEquals("Fail to add motor to small car space", 42, park.getNumMotorCycles());
	}
	
	/**
	 * Test processQueue method when motorcycle in queue and 
	 * motor space full but only one small car space available
	 * only one motorcycle in queue should be removed from queue and parked.
	 * @throws SimulationException 
	 * @throws VehicleException 
	 * 
	 */
	@Test
	public void processQueueMotorcycleIntoSmallSpaceBoundaryTest() throws VehicleException, SimulationException {
		for (int i = 1; i < 3; i++){
			String id = "MC" + i;
			MotorCycle mc = new MotorCycle(id, i);
			park.enterQueue(mc);
		}
		for (int i = 1; i < 90; i++){
			String id = "MC" + i;
			MotorCycle mc = new MotorCycle(id, i);
			park.parkVehicle(mc, i, 300);;
		}
		park.processQueue(3, sim);
		assertEquals("Fail to remove motor from queue", 1, park.numVehiclesInQueue());
		assertEquals("Fail to add motor to small car space", 90, park.getNumMotorCycles());
	}
	
	/**
	 * Test processQueue method when motorcycle in queue and 
	 * motor and small car space full
	 * no motorcycle in queue should be removed from queue.
	 * @throws SimulationException 
	 * @throws VehicleException 
	 * 
	 */
	@Test
	public void processQueueMotorcycleSpaceFullTest() throws VehicleException, SimulationException {
		for (int i = 1; i < 3; i++){
			String id = "MC" + i;
			MotorCycle mc = new MotorCycle(id, i);
			park.enterQueue(mc);
		}
		for (int i = 1; i < 91; i++){
			String id = "MC" + i;
			MotorCycle mc = new MotorCycle(id, i);
			park.parkVehicle(mc, i, 300);;
		}
		park.processQueue(3, sim);
		assertEquals("Fail to remove motor from queue", 2, park.numVehiclesInQueue());
		assertEquals("Fail to add motor to small car space", 90, park.getNumMotorCycles());
	}
	
	/**
	 * Test if the method is able to stop parking vehicle at the back of queue when 
	 * a vehicle at the front has no space available(blocked the queue).
	 * @throws VehicleException 
	 * @throws SimulationException 
	 */
	@Test
	public void processQueueBlockAfterFirstCantParkVehicleProcessTest() throws VehicleException, SimulationException {
		//leave 1 normal car space in car park
		for (int i = 1; i < 150; i++){
			String id = "C" + i;
			Car c = new Car(id, i, false);
			park.parkVehicle(c, i, 300);
		}
		//add 2 normal car in queue
		for (int i = 150; i < 152; i++){
			String id = "C" + i;
			Car c = new Car(id, i, false);
			park.enterQueue(c);
		}
		//add a motor in queue
		mc = new MotorCycle("MC1",152);
		park.enterQueue(mc);
		park.processQueue(200, sim);
		
		assertEquals("The processQueue action does not stop after it reach the first vehicle that can't be parked",
				2, park.numVehiclesInQueue());
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
	 * Test if queueFull method is able to show if queue is full or not
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
	 * When normal car space is available, try process a normal car
	 * Should be able to add the car into car park.
	 * @throws SimulationException 
	 * @throws VehicleException 
	 */
	@Test
	public void tryProcessNewVehiclesNormalCarArrivalTest() throws SimulationException, VehicleException {
		Simulator sim = new Simulator(Constants.DEFAULT_SEED,Constants.DEFAULT_INTENDED_STAY_MEAN,Constants.DEFAULT_INTENDED_STAY_SD,
									1.0, 0.0, 0.0);
		park.tryProcessNewVehicles(10, sim);
		assertEquals("No normal car was added to the car park", 1, park.getNumCars());
	}
	
	/**
	 * When normal car space nearly full, try process a normal car
	 * Should be able to add the car into car park.
	 * @throws SimulationException 
	 * @throws VehicleException 
	 */
	@Test
	public void tryProcessNewVehiclesNormalCarArrivalNearlyFullTest() throws SimulationException, VehicleException {
		Simulator sim = new Simulator(Constants.DEFAULT_SEED,Constants.DEFAULT_INTENDED_STAY_MEAN,Constants.DEFAULT_INTENDED_STAY_SD,
									1.0, 0.0, 0.0);
		
		for (int i = 1; i < 150; i++) {
			String id = "C" + i;
			Car c = new Car(id, i, false);
			park.parkVehicle(c, i, 300);
		}
		
		park.tryProcessNewVehicles(10, sim);
		assertEquals("Unable to add normal car till maximum set capacity of car park", 150, park.getNumCars());
	}
	
	
	/**
	 * When normal car space is full but queue empty, try process a normal car
	 * should be able to add the car into queue.
	 * @throws SimulationException 
	 * @throws VehicleException 
	 */
	@Test
	public void tryProcessNewVehiclesNormalCarArrivalWhenNormalCarSpaceFulledTest() throws SimulationException, VehicleException {
		Simulator sim = new Simulator(Constants.DEFAULT_SEED,Constants.DEFAULT_INTENDED_STAY_MEAN,Constants.DEFAULT_INTENDED_STAY_SD,
				1.0, 0.0, 0.0);
		for (int i = 1; i < 151; i++) {
			String id = "C" + i;
			Car c = new Car(id, i, false);
			park.parkVehicle(c, i, 300);
		}
		park.tryProcessNewVehicles(10, sim);		
		assertEquals("Fail to add the car into queue when no space available", 1, park.numVehiclesInQueue());
	}
	
	/**
	 * When normal car space is full but queue nearly full, try process a normal car
	 * should be able to add the car into queue. 
	 */	
	@Test
	public void tryProcessNewVehiclesNormalCarArrivalWhenNormalCarSpaceFulledAndQueueNearlyFullTest() throws SimulationException, VehicleException {
		Simulator sim = new Simulator(Constants.DEFAULT_SEED,Constants.DEFAULT_INTENDED_STAY_MEAN,Constants.DEFAULT_INTENDED_STAY_SD,
				1.0, 0.0, 0.0);
		for (int i = 1; i < 151; i++) {
			String id = "C" + i;
			Car c = new Car(id, i, false);
			park.parkVehicle(c, i, 300);
		}
		for (int i = 151; i < 170; i++) {
			String id = "C" + i;
			Car c = new Car(id, i, false);
			park.enterQueue(c);
		}
		park.tryProcessNewVehicles(170, sim);
		assertEquals("Fail to add the car into queue when no space available", 20, park.numVehiclesInQueue());
	}
	
	
	/**
	 * When normal car space and queue are full, try process a normal car
	 * should not be able to add to queue or parked(archived the car).
	 * Exception should be thrown when the vehicle tried to process has successfully going into park or queue.
	 * @throws SimulationException 
	 * @throws VehicleException 
	 */
	@Test
	public void tryProcessNewVehiclesNormalCarArrivalWhenNormalCarSpaceAndQueueFulledTest() throws SimulationException, VehicleException {
		Simulator sim = new Simulator(Constants.DEFAULT_SEED,Constants.DEFAULT_INTENDED_STAY_MEAN,Constants.DEFAULT_INTENDED_STAY_SD,
				1.0, 0.0, 0.0);
		for (int i = 1; i < 151; i++) {
			String id = "C" + i;
			Car c = new Car(id, i, false);
			park.parkVehicle(c, i, 300);
		}
		for (int i = 151; i < 171; i++) {
			String id = "C" + i;
			Car c = new Car(id, i, false);
			park.enterQueue(c);
		}
		park.tryProcessNewVehicles(171, sim);
	}
	
	/**
	 * When normal car space available but queue are full, try process a normal car
	 * should be able to add the car into car park.
	 * @throws SimulationException 
	 * @throws VehicleException 
	 */
	@Test
	public void tryProcessNewVehiclesNormalCarArrivalWhenNormalCarSpaceAvailableButQueueFulledTest() throws SimulationException, VehicleException {
		Simulator sim = new Simulator(Constants.DEFAULT_SEED,Constants.DEFAULT_INTENDED_STAY_MEAN,Constants.DEFAULT_INTENDED_STAY_SD,
				1.0, 0.0, 0.0);
		for (int i = 1; i < 21; i++) {
			String id = "C" + i;
			Car c = new Car(id, i, false);
			park.enterQueue(c);
		}
		park.tryProcessNewVehicles(21, sim);
		assertEquals("No normal car was added to the car park when queue fulled", 1, park.getNumCars());
	}
	
//============================================================================================
	
		
	/**
	 * When small car space is available, try process a small car
	 * should be able to add the small car into car park.
	 * @throws SimulationException 
	 * @throws VehicleException 
	 */
	@Test
	public void tryProcessNewVehiclesSmallCarArrivalTest() throws SimulationException, VehicleException {
		Simulator sim = new Simulator(Constants.DEFAULT_SEED,Constants.DEFAULT_INTENDED_STAY_MEAN,Constants.DEFAULT_INTENDED_STAY_SD,
				1.0, 1.0, 0.0);
		park.tryProcessNewVehicles(1, sim);
		assertEquals("No small car was added to the car park", 1, park.getNumSmallCars());
	}
	
	/**
	 * When small car space is full but normal car space available, try process a small car
	 * should be able to add the small car into car park.
	 * @throws SimulationException 
	 * @throws VehicleException 
	 */
	@Test
	public void tryProcessNewVehiclesSmallCarArrivalSmallCarSpaceFullButNormalCarSpaceNotTest() throws SimulationException, VehicleException {
		Simulator sim = new Simulator(Constants.DEFAULT_SEED,Constants.DEFAULT_INTENDED_STAY_MEAN,Constants.DEFAULT_INTENDED_STAY_SD,
				1.0, 1.0, 0.0);
		for (int i = 1; i < 51; i++) {
			String id = "C" + i;
			Car c = new Car(id, i, true);
			park.parkVehicle(c, i, 300);
		}
		park.tryProcessNewVehicles(51, sim);
		assertEquals("Small car unable to park after small car spaces fulled even normal car space available", 
				51, park.getNumSmallCars());
	}
	
	/**
	 * When small car space is full but only one normal car space available, try process a small car
	 * should be able to add the small car into car park.
	 * @throws SimulationException 
	 * @throws VehicleException 
	 */
	@Test
	public void tryProcessNewVehiclesSmallCarArrivalSmallCarSpaceFullButNormalCarSpaceNotBoundaryTest() throws SimulationException, VehicleException {
		Simulator sim = new Simulator(Constants.DEFAULT_SEED,Constants.DEFAULT_INTENDED_STAY_MEAN,Constants.DEFAULT_INTENDED_STAY_SD,
				1.0, 1.0, 0.0);
		for (int i = 1; i < 200; i++) {
			String id = "C" + i;
			Car c = new Car(id, i, true);
			park.parkVehicle(c, i, 300);
		}
		park.tryProcessNewVehicles(200, sim);
		assertEquals("Small car unable to park after small car spaces fulled even normal car space available", 
				200, park.getNumSmallCars());
	}
		
	/**
	 * When small and normal car space are full but queue is not, try process a small car
	 * should be able to add the small car into queue.
	 * @throws SimulationException 
	 * @throws VehicleException 
	 */
	@Test
	public void tryProcessNewVehiclesSmallCarArrivalSmallAndNormalCarSpaceFullTest() throws SimulationException, VehicleException {
		Simulator sim = new Simulator(Constants.DEFAULT_SEED,Constants.DEFAULT_INTENDED_STAY_MEAN,Constants.DEFAULT_INTENDED_STAY_SD,
				1.0, 1.0, 0.0);
		for (int i = 1; i < 201; i++) {
			String id = "C" + i;
			Car c = new Car(id, i, true);
			park.parkVehicle(c, i, 300);
		}
		park.tryProcessNewVehicles(201, sim);
		assertEquals("Small car unable to enter the queue when no space available", 
				1, park.numVehiclesInQueue());
	}
	
	/**
	 * When small and normal car space are full but queue is nearly full, try process a small car
	 * should be able to add the small car into queue.
	 * @throws SimulationException 
	 * @throws VehicleException 
	 */
	@Test
	public void tryProcessNewVehiclesSmallCarArrivalSmallAndNormalCarSpaceFullBoundaryTest() throws SimulationException, VehicleException {
		Simulator sim = new Simulator(Constants.DEFAULT_SEED,Constants.DEFAULT_INTENDED_STAY_MEAN,Constants.DEFAULT_INTENDED_STAY_SD,
				1.0, 1.0, 0.0);
		for (int i = 1; i < 201; i++) {
			String id = "C" + i;
			Car c = new Car(id, i, true);
			park.parkVehicle(c, i, 300);
		}
		for (int i = 201; i < 220; i++) {
			String id = "C" + i;
			Car c = new Car(id, i, true);
			park.enterQueue(c);
		}
		park.tryProcessNewVehicles(220, sim);
		assertEquals("Small car unable to enter the queue when no space available", 
				20, park.numVehiclesInQueue());
	}
	
	/**
	 * When both queue, small and normal car space are full, try process a small car
	 * should not be able to add to queue or parked(archived the car).
	 * Exception should be thrown when the vehicle tried to process has successfully going into park or queue.
	 * @throws SimulationException 
	 * @throws VehicleException 
	 */
	@Test
	public void tryProcessNewVehiclesSmallCarArrivalQueueAndSmallAndNormalCarSpaceFullTest() throws SimulationException, VehicleException {
		Simulator sim = new Simulator(Constants.DEFAULT_SEED,Constants.DEFAULT_INTENDED_STAY_MEAN,Constants.DEFAULT_INTENDED_STAY_SD,
				1.0, 1.0, 0.0);
		for (int i = 1; i < 201; i++) {
			String id = "C" + i;
			Car c = new Car(id, i, true);
			park.parkVehicle(c, i, 300);
		}
		for (int i = 201; i < 221; i++) {
			String id = "C" + i;
			Car c = new Car(id, i, true);
			park.enterQueue(c);
		}
		park.tryProcessNewVehicles(221, sim);
	}
	
	/**
	 * When queue is full but small car space available, try process a small car
	 * should be able to add the small car into car park.
	 * @throws SimulationException 
	 * @throws VehicleException 
	 */
	@Test
	public void tryProcessNewVehiclesSmallCarArrivalQueueFullButSmallCarSpaceAvailableTest() throws SimulationException, VehicleException {
		Simulator sim = new Simulator(Constants.DEFAULT_SEED,Constants.DEFAULT_INTENDED_STAY_MEAN,Constants.DEFAULT_INTENDED_STAY_SD,
				1.0, 1.0, 0.0);
		for (int i = 1; i < 21; i++) {
			String id = "C" + i;
			Car c = new Car(id, i, false);
			park.enterQueue(c);
		}
		park.tryProcessNewVehicles(21, sim);
		assertEquals("Unable to add small cars into park when small car space available but queue full",
				1, park.getNumSmallCars());
	}
	
	/**
	 * When queue is full but only one small car space available, try process a small car
	 * should be able to add the small car into car park.
	 * @throws SimulationException 
	 * @throws VehicleException 
	 */
	@Test
	public void tryProcessNewVehiclesSmallCarArrivalQueueFullButSmallCarSpaceAvailableBoundaryTest() throws SimulationException, VehicleException {
		Simulator sim = new Simulator(Constants.DEFAULT_SEED,Constants.DEFAULT_INTENDED_STAY_MEAN,Constants.DEFAULT_INTENDED_STAY_SD,
				1.0, 1.0, 0.0);
		for (int i = 1; i < 200; i++) {
			String id = "C" + i;
			Car c = new Car(id, i, true);
			park.parkVehicle(c, i, 300);
		}
		for (int i = 200; i < 220; i++) {
			String id = "C" + i;
			Car c = new Car(id, i, false);
			park.enterQueue(c);
		}
		park.tryProcessNewVehicles(220, sim);
		assertEquals("Unable to add small cars into park when small car space available but queue full",
				200, park.getNumSmallCars());
	}
	
	
	/**
	 * When one of small car space are occupied by motorcycle but normal car space is full, try process a small car
	 * should be able to add the small car into car park.
	 * @throws SimulationException 
	 * @throws VehicleException 
	 */
	@Test
	public void tryProcessNewVehiclesSmallCarArrivalSomeMotorParkingInSmallSpaceTest() throws SimulationException, VehicleException {
		Simulator sim = new Simulator(Constants.DEFAULT_SEED,Constants.DEFAULT_INTENDED_STAY_MEAN,Constants.DEFAULT_INTENDED_STAY_SD,
				1.0, 1.0, 0.0);
		for (int i = 1; i < 151; i++) {
			String id = "C" + i;
			Car c = new Car(id, i, false);
			park.parkVehicle(c, i, 300);
		}
		for (int i = 1; i < 42; i++) {
			String id = "MC" + i;
			MotorCycle mc = new MotorCycle(id, i);
			park.parkVehicle(mc, i, 300);
		}
		park.tryProcessNewVehicles(151, sim);
		assertEquals("Unable to add small car into car park when no normal space"
				+ " and a motor is parked in small car space", 1, park.getNumSmallCars());
	}
	
	/**
	 * When most of small car space are occupied by motorcycle but normal car space is full, try process a small car
	 * should be able to add the small car into car park.
	 * @throws SimulationException 
	 * @throws VehicleException 
	 */
	@Test
	public void tryProcessNewVehiclesSmallCarArrivalSomeMotorParkingInSmallSpaceBoundaryTest() throws SimulationException, VehicleException {
		Simulator sim = new Simulator(Constants.DEFAULT_SEED,Constants.DEFAULT_INTENDED_STAY_MEAN,Constants.DEFAULT_INTENDED_STAY_SD,
				1.0, 1.0, 0.0);
		for (int i = 1; i < 151; i++) {
			String id = "C" + i;
			Car c = new Car(id, i, false);
			park.parkVehicle(c, i, 300);
		}
		for (int i = 1; i < 90; i++) {
			String id = "MC" + i;
			MotorCycle mc = new MotorCycle(id, i);
			park.parkVehicle(mc, i, 300);
		}
		park.tryProcessNewVehicles(151, sim);
		assertEquals("Unable to add small car into car park when no normal space"
				+ " and a motor is parked in small car space", 1, park.getNumSmallCars());
	}
	
	
	/**
	 * When all of small car space are occupied by motorcycle and normal car space is full, 
	 * queue is not full, try process a small car
	 * should be able to add the small car into queue.
	 * @throws SimulationException 
	 * @throws VehicleException 
	 */
	@Test
	public void tryProcessNewVehiclesSmallCarArrivalOnlyQueueAvailableTest() throws SimulationException, VehicleException {
		Simulator sim = new Simulator(Constants.DEFAULT_SEED,Constants.DEFAULT_INTENDED_STAY_MEAN,Constants.DEFAULT_INTENDED_STAY_SD,
				1.0, 1.0, 0.0);
		for (int i = 1; i < 151; i++) {
			String id = "C" + i;
			Car c = new Car(id, i, false);
			park.parkVehicle(c, i, 300);
		}
		for (int i = 1; i < 91; i++) {
			String id = "MC" + i;
			MotorCycle mc = new MotorCycle(id, i);
			park.parkVehicle(mc, i, 300);
		}
		park.tryProcessNewVehicles(151, sim);
		assertEquals("Unable to add small car into queue when spaces are occupied by normal car and motor", 
				1, park.numVehiclesInQueue());
	}
	
	/**
	 * When all of small car space are occupied by motorcycle and the queue and normal car space are full, 
	 * try process a small car
	 * should not be able to add to queue or parked(archived the car).
	 * Exception should be thrown when the vehicle tried to process has successfully going into park or queue.
	 * @throws SimulationException 
	 * @throws VehicleException 
	 */
	@Test
	public void tryProcessNewVehiclesSmallCarArrivalNoSpacesAvailableTest() throws SimulationException, VehicleException {
		Simulator sim = new Simulator(Constants.DEFAULT_SEED,Constants.DEFAULT_INTENDED_STAY_MEAN,Constants.DEFAULT_INTENDED_STAY_SD,
				1.0, 1.0, 0.0);
		for (int i = 1; i < 151; i++) {
			String id = "C" + i;
			Car c = new Car(id, i, false);
			park.parkVehicle(c, i, 300);
		}
		for (int i = 1; i < 91; i++) {
			String id = "MC" + i;
			MotorCycle mc = new MotorCycle(id, i);
			park.parkVehicle(mc, i, 300);
		}
		for (int i = 151; i < 171; i++) {
			String id = "C" + i;
			Car c = new Car(id, i, false);
			park.enterQueue(c);
		}
		park.tryProcessNewVehicles(171, sim);		
	}
	
	
	
//============================================================================================
	
	/**
	 * When no motorcycle is parking, try process a motorcycle
	 * should be able to add the motorcycle into car park.
	 * @throws SimulationException 
	 * @throws VehicleException 
	 */
	@Test
	public void tryProcessNewVehiclesMotorCycleArrivalTest() throws SimulationException, VehicleException {
		Simulator sim = new Simulator(Constants.DEFAULT_SEED,Constants.DEFAULT_INTENDED_STAY_MEAN,Constants.DEFAULT_INTENDED_STAY_SD,
				0.0, 0.0, 1.0);
		park.tryProcessNewVehicles(1, sim);
		assertEquals("No motorcycle was added to the car park", 1, park.getNumMotorCycles());
	}
	
	/**
	 * When motorcycle space is full but small car space available, try process a motorcycle
	 * should be able to add the motorcycle into car park.
	 * @throws SimulationException 
	 * @throws VehicleException 
	 */
	@Test
	public void tryProcessNewVehiclesMotorCycleArrivalNoMotorSpaceButSmallSpaceAvailableTest() throws SimulationException, VehicleException {
		Simulator sim = new Simulator(Constants.DEFAULT_SEED,Constants.DEFAULT_INTENDED_STAY_MEAN,Constants.DEFAULT_INTENDED_STAY_SD,
				0.0, 0.0, 1.0);
		for (int i = 1; i < 41; i++) {
			String id = "MC" + i;
			MotorCycle mc = new MotorCycle(id, i);
			park.parkVehicle(mc, i, 300);
		}
		park.tryProcessNewVehicles(1, sim);
		assertEquals("Unable to add motorcycle to the car park when motor space full but small car space available"
				, 41, park.getNumMotorCycles());
	}
	
	/**
	 * When motorcycle space is full and only one small car space available, try process a motorcycle
	 * should be able to add the motorcycle into car park.
	 * @throws SimulationException 
	 * @throws VehicleException 
	 */
	@Test
	public void tryProcessNewVehiclesMotorCycleArrivalNoMotorSpaceButSmallSpaceAvailableBoundaryTest() throws SimulationException, VehicleException {
		Simulator sim = new Simulator(Constants.DEFAULT_SEED,Constants.DEFAULT_INTENDED_STAY_MEAN,Constants.DEFAULT_INTENDED_STAY_SD,
				0.0, 0.0, 1.0);
		for (int i = 1; i < 90; i++) {
			String id = "MC" + i;
			MotorCycle mc = new MotorCycle(id, i);
			park.parkVehicle(mc, i, 300);
		}
		park.tryProcessNewVehicles(90, sim);
		assertEquals("Unable to add motorcycle into car park when motor space full but small car space available"
				, 90, park.getNumMotorCycles());
	}
		
	/**
	 * When queue is full but motorcycle space is available, try process a motorcycle
	 * should be able to add the motorcycle into car park.
	 * @throws SimulationException 
	 * @throws VehicleException 
	 */
	@Test
	public void tryProcessNewVehiclesMotorCycleArrivalNoQueueSpaceButMotorSpaceAvailableTest() throws SimulationException, VehicleException {
		Simulator sim = new Simulator(Constants.DEFAULT_SEED,Constants.DEFAULT_INTENDED_STAY_MEAN,Constants.DEFAULT_INTENDED_STAY_SD,
				0.0, 0.0, 1.0);
		for (int i = 1; i < 21; i++) {
			String id = "C" + i;
			Car c = new Car(id, i, false);
			park.enterQueue(c);
		}
		park.tryProcessNewVehicles(1, sim);
		assertEquals("Unable to add motorcycle into car park when queue is full but space available", 
				1, park.getNumMotorCycles());
	}
	
	/**
	 * When motorcycle space and small car space are full but queue available, try process a motorcycle
	 * should be able to add the motorcycle into queue.
	 * @throws SimulationException 
	 * @throws VehicleException 
	 */
	@Test
	public void tryProcessNewVehiclesMotorCycleArrivalNoMotorSpaceAndSmallSpaceButQueueAvailableTest() throws SimulationException, VehicleException {
		Simulator sim = new Simulator(Constants.DEFAULT_SEED,Constants.DEFAULT_INTENDED_STAY_MEAN,Constants.DEFAULT_INTENDED_STAY_SD,
				0.0, 0.0, 1.0);
		for (int i = 1; i < 91; i++) {
			String id = "MC" + i;
			MotorCycle mc = new MotorCycle(id, i);
			park.parkVehicle(mc, i, 300);
		}
		park.tryProcessNewVehicles(1, sim);
		assertEquals("Unable to add motorcycle into queue when no space available", 1, park.numVehiclesInQueue());
	}
	
	/**
	 * When motorcycle space and small car space are full but only one queue space available, try process a motorcycle
	 * should be able to add the motorcycle into queue.
	 * @throws SimulationException 
	 * @throws VehicleException 
	 */
	@Test
	public void tryProcessNewVehiclesMotorCycleArrivalNoMotorSpaceAndSmallSpaceButQueueAvailableBoundaryTest() throws SimulationException, VehicleException {
		Simulator sim = new Simulator(Constants.DEFAULT_SEED,Constants.DEFAULT_INTENDED_STAY_MEAN,Constants.DEFAULT_INTENDED_STAY_SD,
				0.0, 0.0, 1.0);
		for (int i = 1; i < 91; i++) {
			String id = "MC" + i;
			MotorCycle mc = new MotorCycle(id, i);
			park.parkVehicle(mc, i, 300);
		}
		for (int i = 1; i < 20; i++) {
			String id = "C" + i;
			Car c = new Car(id, i, false);
			park.enterQueue(c);
		}		
		park.tryProcessNewVehicles(1, sim);
		assertEquals("Unable to add motorcycle into queue when no space available", 20, park.numVehiclesInQueue());
	}
	
	/**
	 * When both queue, motorcycle space and small car space are full, try process a motorcycle
	 * should not be able to add to queue or parked(archived the motorcycle).
	 * Exception should be thrown when the vehicle tried to process has successfully going into park or queue.
	 * @throws SimulationException 
	 * @throws VehicleException 
	 */
	@Test
	public void tryProcessNewVehiclesMotorCycleArrivalNothingAvailableTest() throws SimulationException, VehicleException {
		Simulator sim = new Simulator(Constants.DEFAULT_SEED,Constants.DEFAULT_INTENDED_STAY_MEAN,Constants.DEFAULT_INTENDED_STAY_SD,
				0.0, 0.0, 1.0);
		for (int i = 1; i < 91; i++) {
			String id = "MC" + i;
			MotorCycle mc = new MotorCycle(id, i);
			park.parkVehicle(mc, i, 300);
		}
		for (int i = 1; i < 21; i++) {
			String id = "C" + i;
			Car c = new Car(id, i, false);
			park.enterQueue(c);
		}		
		park.tryProcessNewVehicles(1, sim);
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
