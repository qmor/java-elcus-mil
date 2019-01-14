package ru.elcus.mil;

import com.sun.jna.Structure;

public class TTmkConfigData extends Structure{
	 short nType;
	 byte[] szName = new byte[10];
	 short wPorts1;
	 short wPorts2;
	 short wIrq1;
	 short wIrq2;
	 short wIODelay;
}
