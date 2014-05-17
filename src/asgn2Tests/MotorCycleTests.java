/**
 * 
 * This file is part of the CarParkSimulator Project, written as 
 * part of the assessment for INB370, semester 1, 2014. 
 *
 * CarParkSimulator
 * asgn2Tests 
 * 15/05/2014
 * 
 * 
 * @author Chun Pui Chan
 */
package asgn2Tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import asgn2Exceptions.*;
import asgn2Simulators.Constants;
import asgn2Vehicles.*;

public class MotorCycleTests {
	
	MotorCycle motorCycle;
	
	
	/**
	 * @throws java.lang.Exception
	 * initial a motorCycle object
	 */
	@Before
	public void setUp() throws Exception {
		//MotorCycle(String vehID, int arrivalTime)
		motorCycle = new MotorCycle("MC1",10);
	}
	
	
	
	/**
	 * Test method for {@link asgn2Vehicles.MotorCycle#MotorCycle(java.lang.String, int)}.
	 * 
	 * ArrivalTimeZeroTest: set the arrival time of a vehicle to zero,
	 * expected to throw a VehicleException
	 * 
	 * ArrivalTimeNegativeTest: set arrival time of a vehicle to a negative value,
	 * expected to throw a VehicleException
	 * 
	 * @throws VehicleException
	 */	
	@Test(expected = VehicleException.class)
	public void MotorCycleConstructArrivalTimeZeroTest() throws VehicleException {
		motorCycle = new MotorCycle("MC1",0);
	}
	
	@Test(expected = VehicleException.class)
	public void MotorCycleConstructArrivalTimeNegativeTest() throws VehicleException {
		motorCycle = new MotorCycle("MC1",-1);
	}
		

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#getVehID()}.
	 * 
	 * getVehIDTest: check if the vehID is set according to the constructor
	 */
	@Test
	public void getVehIDTest() {
		assertEquals("Fail to get correct vehID","MC1",motorCycle.getVehID());		
	}

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#getArrivalTime()}.
	 * 
	 * getArrivalTimeTest: check if the arrival time is set as constructor
	 */
	@Test
	public void getArrivalTimeTest() {
		assertEquals("Fail to get correct arrival time",10,motorCycle.getArrivalTime());
	}

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#enterQueuedState()}.
	 * 
	 * enterQueuedStateTest: send the motorCycle to queue state
	 * 						 and check if the vehicle is actually in queue state
	 * 
	 * enterQueuedStateVehicleAlreadyInQueuedTest: send the vehicle to queue state twice
	 * @throws VehicleExceptions 
	 * 
	 * enterQueuedStateVehicleAlreadyInParkedTest: send the vehicle to park state and try to send it to queue state
	 */
	@Test
	public void enterQueuedStateTest() throws VehicleException {
		motorCycle.enterQueuedState();
		assertTrue("queue state is not set to queued", motorCycle.isQueued());
	}
	
	
	@Test(expected = VehicleException.class)
	public void enterQueuedStateVehicleAlreadyInQueuedTest() throws VehicleException {
		try {motorCycle.enterQueuedState();} 
		catch (VehicleException e){ fail("MotorCycle cannot enter first queue state.");} //use try-catch to ensure the vehicleException expected is not throw by the 1st enterQueuedState
		motorCycle.enterQueuedState();
	}
	
	@Test(expected = VehicleException.class)
	public void enterQueuedStateVehicleAlreadyInParkedTest() throws VehicleException {
		try {motorCycle.enterParkedState(40, Constants.MINIMUM_STAY +1);} 
		catch (VehicleException e){ fail("MotorCycle cannot enter park state.");} //use try-catch to ensure the vehicleException expected is not throw by enterParkedState
		motorCycle.enterQueuedState();
	}
	
	
	

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#exitQueuedState(int)}.
	 * 
	 * exitQueuedStateToArchiveExitTimeCorrectTest: send the vehicle to queue and push it out the queue,
	 * 												then check if the vehicle's departure time is correctly set
	 * 
	 * exitQueuedStateVehicleInParkedStateTest: push the vehicle out the queue while the vehicle is in park state
	 * @throws VehicleException
	 * 
	 * exitQueuedStateVehicleInNewStateTest: push the vehicle out the queue while the vehicle is in new state
	 * @throws VehicleException
	 * 
	 * exitQueuedStateVehicleInArchivedStateTest:push the vehicle out the queue while the vehicle is in archived state
	 * @throws VehicleException
	 * 
	 * exitQueuedStateExitTimeEarlierThanArrivalTimeTest: push the vehicle out the queue with time earlier than entered time
	 * @throws VehicleException
	 * 
	 * exitQueuedStateExitTimeEqualToArrivalTimeTest: push the vehicle out the queue with time equals entered time
	 * @throws VehicleException
	 */
	@Test 
	public void exitQueuedStateToArchiveExitTimeCorrectTest() throws VehicleException {		
		//arrivalTime = 10
		motorCycle.enterQueuedState();
		motorCycle.exitQueuedState(60);
		assertEquals("Fail to set correct archive departure time",60,motorCycle.getDepartureTime());
	}
	
	@Test(expected = VehicleException.class)
	public void exitQueuedStateVehicleInParkedStateTest() throws VehicleException {
		try {motorCycle.enterParkedState(40, Constants.MINIMUM_STAY +1);} 
		catch (VehicleException e){ fail("MotorCycle cannot enter park state.");} //use try-catch to ensure the vehicleException expected is not throw by enterParkedState
		motorCycle.exitQueuedState(80);
	}
	
	@Test(expected = VehicleException.class)
	public void exitQueuedStateVehicleInNewStateTest() throws VehicleException {
		motorCycle.exitQueuedState(80);
	}
	
	@Test(expected = VehicleException.class)
	public void exitQueuedStateVehicleInArchivedStateTest() throws VehicleException {
		try {
			motorCycle.enterQueuedState();
			motorCycle.exitQueuedState(79);
			} 
		catch (VehicleException e){ fail("Park state cannot change to archived.");} //use try-catch to ensure the vehicleException expected is not throw by other methods
		motorCycle.exitQueuedState(80);
	}
	
	@Test(expected = VehicleException.class)
	public void exitQueuedStateExitTimeEarlierThanArrivalTimeTest() throws VehicleException {
		try {motorCycle.enterQueuedState();} 
		catch (VehicleException e){ fail("MotorCycle cannot enter queue state.");} //use try-catch to ensure the vehicleException expected is not throw by enterQueuedState
		motorCycle.exitQueuedState(9); //(9 = arrivalTime - 1)
	}
	
	@Test(expected = VehicleException.class)
	public void exitQueuedStateExitTimeEqualToArrivalTimeTest() throws VehicleException {
		try {motorCycle.enterQueuedState();} 
		catch (VehicleException e){ fail("MotorCycle cannot enter queue state.");} //use try-catch to ensure the vehicleException expected is not throw by enterQueuedState
		motorCycle.exitQueuedState(10);
	}
	

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#enterParkedState(int, int)}.
	 *
	 * enterParkedStateTest: send the vehicle to park, then check if it is actually parked
	 * 
	 * enterParkedStateVehicleAlreadyInParkedStateTest: send the vehicle to park, twice
	 * @throws VehicleException
	 * 
	 * enterParkedStateVehicleAlreadyInQueuedStateTest: send the vehicle to queue, then send it to park
	 * @throws VehicleException
	 * 
	 * enterParkedStateParkingTimeZeroTest: set the parking time to be zero and check if it pass
	 * 
	 * enterParkedStateNegativeParkingTimeTest: set parking time to a negative value
	 * @throws VehicleException
	 * 
	 * enterParkedStateIntendedDurationTooShortTest: set intendedDuration as the minimum duration allowed minus one
	 * @throws VehicleException
	 * 
	 */
	@Test
	public void enterParkedStateTest() throws VehicleException {
		motorCycle.enterParkedState(40, Constants.MINIMUM_STAY +1);
		assertTrue("park state is not set to parked", motorCycle.isParked());
	}	
	
	@Test(expected = VehicleException.class)
	public void enterParkedStateVehicleAlreadyInParkedStateTest() throws VehicleException {
		try {motorCycle.enterParkedState(40, Constants.MINIMUM_STAY +1);} 
		catch (VehicleException e){ fail("MotorCycle cannot enter first park state.");} //use try-catch to ensure the vehicleException expected is not throw by first enterParkedState
		motorCycle.enterParkedState(40, Constants.MINIMUM_STAY +1);
	}
	
	@Test(expected = VehicleException.class)
	public void enterParkedStateVehicleAlreadyInQueuedStateTest() throws VehicleException {
		try {motorCycle.enterQueuedState();} 
		catch (VehicleException e){ fail("MotorCycle cannot enter queue state.");} //use try-catch to ensure the vehicleException expected is not throw by enterQueuedState
		motorCycle.enterParkedState(40, Constants.MINIMUM_STAY +1);
	}
	
	@Test
	public void enterParkedStateParkingTimeZeroTest() throws VehicleException {
		//enterParkedState(int parkingTime, int intendedDuration)
		motorCycle.enterParkedState(0, Constants.MINIMUM_STAY +1);
		assertEquals("Parking Time should able to be zero.",0,motorCycle.getParkingTime());
	}
	
	@Test(expected = VehicleException.class)
	public void enterParkedStateNegativeParkingTimeTest() throws VehicleException {
		motorCycle.enterParkedState(-1, Constants.MINIMUM_STAY +1);
	}
	
	@Test(expected = VehicleException.class)
	public void enterParkedStateIntendedDurationTooShortTest() throws VehicleException {
		motorCycle.enterParkedState(30,Constants.MINIMUM_STAY -1);
	}
	

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#exitParkedState(int)}.
	 * 
	 * exitParkedStateEarlierThanIntendedTest: send the vehicle to park, then exit the vehicle by a time shorter than intendedDuration
	 * 
	 * exitParkedStateVehicleInNewStateTest: exit the vehicle while it's in 'new' state
	 * @throws VehicleException
	 * 
	 * exitParkedStateVehicleInQueuedStateTest: exit the vehicle while it's in 'queued' state
	 * @throws VehicleException
	 * 
	 * exitParkedStateVehicleInArchivedStateTest: exit the vehicle while it's in 'archived' state
	 * @throws VehicleException
	 * 
	 * exitParkedStateDepartTimeEarlierThanParkingTimeTest: exit the vehicle with departure earlier than parking time assigned
	 * @throws VehicleException
	 */
	@Test
	public void exitParkedStateEarlierThanIntendedTest() throws VehicleException {		
		motorCycle.enterParkedState(40, Constants.MINIMUM_STAY + 1);
		motorCycle.exitParkedState(40 + Constants.MINIMUM_STAY );
		assertEquals("Fail to set departure time that earlier than the intened stay",
						40 + Constants.MINIMUM_STAY ,motorCycle.getDepartureTime());
	}
	
	@Test(expected = VehicleException.class)
	public void exitParkedStateVehicleInNewStateTest() throws VehicleException {
		motorCycle.exitParkedState(80);
	}
	
	@Test(expected = VehicleException.class)
	public void exitParkedStateVehicleInQueuedStateTest() throws VehicleException {
		try {motorCycle.enterQueuedState();}
		catch (VehicleException e) { fail("MotorCycle cannot enter queue state"); }  //use try-catch to ensure the vehicleException expected is not throw by enterQueuedState
		motorCycle.exitParkedState(80);
	}
	
	@Test(expected = VehicleException.class)
	public void exitParkedStateVehicleInArchivedStateTest() throws VehicleException {
		try{
			motorCycle.enterParkedState(40, Constants.MINIMUM_STAY );
			motorCycle.exitParkedState(40 + Constants.MINIMUM_STAY );
			}
		catch (VehicleException e){ fail("MotorCycle cannot enter archive state.");}  //use try-catch to ensure the vehicleException expected is not throw by other methods
		motorCycle.exitParkedState(40 + Constants.MINIMUM_STAY );
	}
	
	@Test(expected = VehicleException.class)
	public void exitParkedStateDepartTimeEarlierThanParkingTimeTest() throws VehicleException {
		//enterParkedState(int parkingTime, int intendedDuration)
		try {motorCycle.enterParkedState(50, Constants.MINIMUM_STAY );}
		catch (VehicleException e){ fail("MotorCycle cannot enter park state");}  //use try-catch to ensure the vehicleException expected is not throw by enterParkedState
		//exitParkedState(int departureTime)
		motorCycle.exitParkedState(49);
	}	
	
	

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#isParked()}.
	 * @throws VehicleException 
	 */
	@Test 
	public void isParkedTest() throws VehicleException {		
		motorCycle.enterParkedState(40, Constants.MINIMUM_STAY +1);
		assertTrue("Fail to show the vehicle is parked or not",motorCycle.isParked());
	}
		

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#isQueued()}.
	 * isQueuedTest: send the vehicle to queued state and access this method
	 */
	@Test 
	public void isQueuedTest() throws VehicleException {
		motorCycle.enterQueuedState();
		assertTrue("Fail to show the vehicle is queued or not", motorCycle.isQueued());
	}

		
	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#getParkingTime()}.
	 * getParkingTimeTest: send the vehicle to park and check the parking time with the assigned one
	 */
	@Test
	public void getParkingTimeTest() throws VehicleException {
		//enterParkedState(int parkingTime, int intendedDuration)
		motorCycle.enterParkedState(30, Constants.MINIMUM_STAY );
		assertEquals("Fail to get correct parking time",30,motorCycle.getParkingTime());	
	}

	
	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#getDepartureTime()}.
	 * getDepartureTimeTest: send the vehicle to park,
	 * 						 check the estimated departure time (parking time + intendedDuration),
	 * 						 exit the vehicle,
	 * 						 check the actual departure time
	 */
	@Test
	public void getDepartureTimeTest() throws VehicleException {
		motorCycle.enterParkedState(30, Constants.MINIMUM_STAY );
		assertEquals("Fail to get correct departure time (estimated)",30 + Constants.MINIMUM_STAY ,motorCycle.getDepartureTime());
		motorCycle.exitParkedState(30 + Constants.MINIMUM_STAY -1);
		assertEquals("Fail to get correct departure time (actual)",30 + Constants.MINIMUM_STAY -1 ,motorCycle.getDepartureTime());
	}

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#wasQueued()}.
	 * wasQueuedTest: send the vehicle to queue, then check if the flag is changed
	 */
	@Test
	public void wasQueuedTest() throws VehicleException {
		assertFalse("Fail to show the vehicle was not queued", motorCycle.wasQueued());
		motorCycle.enterQueuedState();
		assertTrue("Fail to show the vehicle was queued", motorCycle.wasQueued());
	}

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#wasParked()}.
	 * wasParkedTest: send the vehicle to park, then check if the flag is changed
	 */
	@Test
	public void wasParkedTest() throws VehicleException {
		assertFalse("Fail to determine when the vehicle was not parked", motorCycle.wasParked());
		motorCycle.enterParkedState(30, Constants.MINIMUM_STAY ) ;
		assertTrue("Fail to determine when the vehicle was parked", motorCycle.wasParked());
	}

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#isSatisfied()}.
	 */
	@Test
	public void isSatisfiedTest() throws VehicleException {		
		assertFalse("Fail to determine when the customer was dissatisfied", motorCycle.wasParked());
		motorCycle.enterParkedState(30, Constants.MINIMUM_STAY ) ;
		assertTrue("Fail to determine when the customer was satisfied", motorCycle.wasParked());
	}
	
}
