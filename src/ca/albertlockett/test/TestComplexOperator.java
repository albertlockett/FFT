package ca.albertlockett.test;

import static org.junit.Assert.*;

import org.junit.Test;

import ca.albertlockett.Complex;
import ca.albertlockett.ComplexOperator;

public class TestComplexOperator {

	private final ComplexOperator cop = new ComplexOperator();
	
	
	@Test
	public void testTimes(){
		
		// 1+0j * 1+0j
		assertEquals(
			cop.times(new Complex(1,0), new Complex(1,0)),
			new Complex(1,0)
		);
		
		// 0+1j * 0+1j
		assertEquals(
			cop.times(new Complex(0,1), new Complex(0,1)),	
			new Complex(-1,0)
		);
		
		// 2+1j * 2+1j
		assertEquals(
			cop.times(new Complex(2,1), new Complex(2,1)),	
			new Complex(3,4)
		);
		
	}
	
	@Test
	public void testDivide(){
		// TODO
	}
	
	
	@Test
	public void testAdd(){
		// TODO
	}
	
	@Test
	public void testSubtract(){
		// TODO
	}
	
	
	
}
