package ru.elcus.mil;

import com.sun.jna.Structure;

public class TTmkEventData extends Structure
{
	int nInt;
	  unsigned short wMode;
	  Union
	  {
	    struct
	    {
	      unsigned short wResult;
	      unsigned short wAW1;
	      unsigned short wAW2;
	    } bc;
	    struct
	    {
	      unsigned short wBase;
	      unsigned short wResultX;
	    } bcx;
	    struct
	    {
	      unsigned short wStatus;
	      unsigned short wCmd;
	    } rt;
	    struct
	    {
	      unsigned short wBase;
	      unsigned short wResultX;
	    } mt;
	    struct
	    {
	      unsigned short wStatus;
	    } mrt;
	    struct
	    {
	      unsigned short wRequest;
	    } tmk;
}