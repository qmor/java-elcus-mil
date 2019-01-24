package ru.elcus.mil.structs;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;

public class RT extends Structure{
	public  short wStatus;
	public  short wCmd;
  	@Override
  	protected List<String> getFieldOrder() {
  		return Arrays.asList(new String[] { "wStatus","wCmd"});
  	}
}
