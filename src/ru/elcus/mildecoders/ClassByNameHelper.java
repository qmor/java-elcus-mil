package ru.elcus.mildecoders;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.logging.log4j.Level;

public class ClassByNameHelper {
	public Object getInstanceByName(String classname)
	{
		Object result = null;
		Class<?> a = null;
		try {
			a = Class.forName(classname);
			Constructor<?> ctor = null;
			ctor = a.getConstructor();
			result = ctor.newInstance();
		} catch (NoSuchMethodException e) 
		{
			Log.write(Level.ERROR, String.format("Ошибка инстанцирования класса %s - не найдён конструктор по умолчанию",classname));
		} catch (SecurityException e) 
		{
			Log.write(Level.ERROR, String.format("Ошибка инстанцирования класса %s - SecurityException",classname));
		}
		catch (ClassNotFoundException e) 
		{
			Log.write(Level.ERROR, String.format("Ошибка инстанцирования класса %s - класс не найден",classname));
		}
		catch (InstantiationException e) 
		{
			Log.write(Level.ERROR, String.format("Ошибка инстанцирования класса %s - %s",classname,e.getMessage()));
		} catch (IllegalAccessException e) 
		{
			Log.write(Level.ERROR, String.format("Ошибка инстанцирования класса %s - %s",classname,e.getMessage()));
		} catch (IllegalArgumentException e) 
		{
			Log.write(Level.ERROR, String.format("Ошибка инстанцирования класса %s - %s",classname,e.getMessage()));
		} catch (InvocationTargetException e) 
		{
			Log.write(Level.ERROR, String.format("Ошибка инстанцирования класса %s - %s",classname,e.getMessage()));
		}

		return result;
	}
}
