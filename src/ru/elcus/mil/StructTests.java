package ru.elcus.mil;
import org.junit.Test;

import ru.elcus.mil.structs.TTmkEventData;
import ru.elcus.mil.structs.TTmkEventDataUnion;
public class StructTests {
	@Test
	public void test1()
	{
		TTmkEventDataUnion union = new TTmkEventDataUnion();
		System.out.println(union);
		TTmkEventData eventdata = new TTmkEventData();
		System.out.println(eventdata);
	}
}
