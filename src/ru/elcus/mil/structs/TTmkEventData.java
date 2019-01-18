package ru.elcus.mil.structs;

import com.sun.jna.Structure;

public class TTmkEventData extends Structure{
	  public int nInt;
	  public short wMode;
	  public TTmkEventDataUnion union = new TTmkEventDataUnion();
}
