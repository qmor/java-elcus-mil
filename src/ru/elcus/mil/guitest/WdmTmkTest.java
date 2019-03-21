package ru.elcus.mil.guitest;

import ru.elcus.mil.Eclus1553Exception;
import ru.elcus.mil.Elcus1553Device;
import ru.elcus.mil.MilWorkMode;

public class WdmTmkTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Elcus1553Device device = new Elcus1553Device(0);
		try {
			device.initAs(MilWorkMode.eMilWorkModeMT);
		} catch (Eclus1553Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
