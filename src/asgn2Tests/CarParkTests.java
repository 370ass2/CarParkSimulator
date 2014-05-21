/**
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

/**
 * This class includes unit testing for class CarPark.
 */
public class CarParkTests {

	CarPark park;
	
	
	/**
	 * Construct car park with values before each test.
	 * @throws java.lang.Exception
	 * @author Chun Hung Chung
	 */
	@Before	
	public void setUp() throws Exception {
		//maxCarSpaces,maxSmallCarSpaces, maxMotorCycleSpaces, maxQueueSize
		park = new CarPark(200,50, 40, 20);
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
	
	@Test(expected = VehicleException.class)
	public void archiveDepartingVehiclesArchivedStateTest() {
		
	}	
	@Test(expected = VehicleException.class)
	public void archiveDepartingVehiclesQueuedStateTest() {
		
	}	
	@Test(expected = VehicleException.class)
	public void archiveDepartingVehiclesNewStateTest() {
		
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

	
	@Test(expected = VehicleException.class)
	public void archiveQueueFailuresInNewTest() {
		
	}
	@Test(expected = VehicleException.class)
	public void archiveQueueFailuresInParkedTest() {
		
	}
	@Test(expected = VehicleException.class)
	public void archiveQueueFailuresInArchivedTest() {
		
	}

	//?timing constaints are violated?
	@Test(expected = VehicleException.class)
	public void archiveQueueFailures  Test() {
		
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
	public void enterQueueZeroTest() {
		assertEquals("Fail to return zero when no vehicle in queue",0,park.numVehiclesInQueue());
	}
	
	@Test
	public void enterQueueOneVehicleTest() {		
		park.enterQueue(v);
		assertEquals("Fail to return zero when no vehicle in queue",1,park.numVehiclesInQueue());
	}	
	
	@Test
	public void enterQueueJustFitMaxQueueSizeTest() {		
		//maxQueueSize = 20
		for (int i=1; i<21; i++) {
			park.enterQueue(v); //do 20 times
		}
		assertEquals("Fail to add correct numbers of vehicles into the queue",20,park.numVehiclesInQueue());
	}	
	
	@Test(expected = SimulationException.class)
	public void enterQueueWhenFulledTest() {
		//max size of queue is 20
		for (int i = 1; i<22; i++) {
			park.enterQueue(v);//do 21 times
		}
	}
	/* Already tested in MotorCycleTests
	@Test(expected = VehicleException.class)
	public void enterQueueAlreadyInQueuedStateTest() {
		
	}
	@Test(expected = VehicleException.class)
	public void enterQueueAlreadyInParkedStateTest() {
		
	}
	@Test(expected = VehicleException.class)
	public void enterQueueAlreadyInArchivedStateTest() {
		
	}*/
		
		
	/**
	 * Test method for {@link asgn2CarParks.CarPark#exitQueue(asgn2Vehicles.Vehicle, int)}.
	 */
	@Test
	public void testExitQueue() {
		fail("Not yet implemented"); // TODO
	}

	@Test(expected = SimulationException.class)
	public void exitQueueInParkedTest() {
		//put vehicle in parking status
		//try to exit the queue
	}
	@Test(expected = SimulationException.class)
	public void exitQueueInArchivedTest() {
		//put vehicle in archived status
		//try to exit the queue
	}
	@Test(expected = SimulationException.class)
	public void exitQueueNewArrivalTest() {
		//put vehicle in new arrival status
		//try to exit the queue
	}
	
	/**
	 * Test method for {@link asgn2CarParks.CarPark#getNumCars()}.
	 */
	@Test
	public void testGetNumCars() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link asgn2CarParks.CarPark#getNumMotorCycles()}.
	 */
	@Test
	public void testGetNumMotorCycles() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link asgn2CarParks.CarPark#getNumSmallCars()}.
	 */
	@Test
	public void testGetNumSmallCars() {
		fail("Not yet implemented"); // TODO
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
	 * Test method for {@link asgn2CarParks.CarPark#parkVehicle(asgn2Vehicles.Vehicle, int, int)}.
	 */
	@Test
	public void testParkVehicle() {
		fail("Not yet implemented"); // TODO
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
	 */
	@Test
	public void testQueueEmpty() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link asgn2CarParks.CarPark#queueFull()}.
	 */
	@Test
	public void testQueueFull() {
		fail("Not yet implemented"); // TODO
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
