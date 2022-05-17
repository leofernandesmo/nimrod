package org.joda.time;

import java.util.Comparator;

import junit.framework.TestCase;

public class SampleTestDateTimeComparator extends TestCase {
	
	public void test1() {
		Comparator cHour = DateTimeComparator.getInstance(DateTimeFieldType.hourOfDay(), DateTimeFieldType.dayOfYear());
		
		DateTime dt1 = new DateTime("2015-02-05T12.00");
		DateTime dt2 = new DateTime("2015-02-06T13.00");
		
		assertTrue(cHour.compare( dt1, dt2 ) == -1);
	}
	
	

}
