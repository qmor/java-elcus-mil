package ru.elcus.mil.structs;

import com.sun.jna.Union;

public class TTmkEventDataUnion extends Union{
	public BC bc = new BC();
	public BCX bcx = new BCX();
	public RT rt = new RT();
	public MT mt = new MT();
	public MRT mrt = new MRT();
	public TMK tmk = new TMK();
}
