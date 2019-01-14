package ru.elcus.mil;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Elcus1553Device device = new Elcus1553Device();
		int result =device.TmkOpen();
		System.out.println(result);
	}

}
