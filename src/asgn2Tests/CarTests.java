/**
 * 
 * This file is part of the CarParkSimulator Project, written as 
 * part of the assessment for INB370, semester 1, 2014.
 * This test will only test the constructor of 
 * the class Car, since the other methods are
 * inherited form abstract class vehicle directly,
 * so they will be tested in MotorCycleTests.java
 *
 * CarParkSimulator
 * asgn2Tests 
 * 15/05/2014
 * 
 * @author Chun Pui Chan
 */
package asgn2Tests;

import static org.junit.Assert.*;

import org.junit.Test;

import asgn2Exceptions.*;
import asgn2Vehicles.*;

public class CarTests {

	Car car;
	

	/**
	 * Test method for {@link asgn2Vehicles.Car#Car(java.lang.String, int, boolean)}.
	 * @throws VehicleException 
	 * 
	 * ArrivalTimeZeroTest: set the arrival time of a car to zero,
	 * expected to throw a VehicleException
	 * 
	 * ArrivalTimeNegativeTest: set arrival time of a car to a negative value,
	 * expected to throw a VehicleException
	 */
	@Test (expected = VehicleException.class)
	public void CarConstructArrivalTimeZeroTest() throws VehicleException {
		car = new Car("C1",0,false);
	}
	@Test (expected = VehicleException.class)
	public void CarConstructArrivalTimeNegativeTest() throws VehicleException {
		car = new Car("C1",-1,false);
	}

	/**
	 * Test method for {@link asgn2Vehicles.Car#isSmall()}.
	 * 
	 * isSmallTest: set a car to not to be small and check,
	 * then set a car to be small and check.
	 */
	@Test
	public void isSmallTest() throws VehicleException {
		car = new Car("C1",10,false);
		assertFalse("Fail to indicate a car is not small",car.isSmall());
		car = new Car("C1", 10, true);
		assertTrue("Fail to indicate a car is small",car.isSmall());
		
		
	}

}
