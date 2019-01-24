package ru.elcus.mil.structs;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;

public class TTmkEventData extends Structure{
	  public int nInt;
	  public short wMode;
	  public TTmkEventDataUnion union;
	  public TTmkEventData()
	  {
			
			union = new TTmkEventDataUnion();
	  }
		@Override
		protected List<String> getFieldOrder() {
			return Arrays.asList(new String[] { "nInt","wMode","union" });
		}
}
