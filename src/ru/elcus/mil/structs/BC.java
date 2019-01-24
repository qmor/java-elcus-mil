package ru.elcus.mil.structs;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;

public class BC extends Structure{
     public short wResult;
     public short wAW1;
     public short wAW2;
 	@Override
 	protected List<String> getFieldOrder() {
 		return Arrays.asList(new String[] { "wResult","wAW1","wAW2" });
 	}
}
