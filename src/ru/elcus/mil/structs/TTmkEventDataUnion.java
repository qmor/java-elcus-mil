package ru.elcus.mil.structs;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Union;

public class TTmkEventDataUnion extends Union{
	public BC bc = new BC();
	public BCX bcx = new BCX();
	public RT rt = new RT();
	public MT mt = new MT();
	public MRT mrt = new MRT();
	public TMK tmk = new TMK();
	public TTmkEventDataUnion()
	{
		
	}
	@Override
	protected List<String> getFieldOrder() {
		return Arrays.asList(new String[] { "bc","bcx","rt","mt","mrt","tmk" });
	}
}
