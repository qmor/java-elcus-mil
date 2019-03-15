package ru.elcus.mildecoders;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public  class Log {
	private Log() {}
	static Logger logger = null;
	public static final Level OPERATOR_ACTIONS = Level.forName("OPERATOR_ACTIONS", 450);
	static
	{
		logger = LogManager.getLogger("MainLog");
	}
	public static Logger getLogger()
	{
		return logger;
	}
	public static void write(Level level, String msg)
	{
		if (logger!=null)
		{
			logger.log(level,msg);
		}
	}

}
