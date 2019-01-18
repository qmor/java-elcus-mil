package ru.elcus.mil.structs;

import com.sun.jna.Structure;

public class TTmkConfigData extends Structure{
	
	public  short nType;
	public  byte[] szName = new byte[10];
	public  short wPorts1;
	public  short wPorts2;
	public  short wIrq1;
	public  short wIrq2;
	public  short wIODelay;
	
}
