package ru.elcus.mil;
import org.junit.Test;
public class Tests {


	@Test
	public void testMT() {

		Elcus1553Device device = new Elcus1553Device(2);
		try {
			device.initAs(MilWorkMode.eMilWorkModeMT);
		} catch (Eclus1553Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //_device.mtstartx(0, (ushort)(CX_CONT | CX_NOINT | CX_NOSIG));

        
        //_listenerThread.startThreadLoop([this]()
        //{
        //    ListenLoopMT();
        //});
		//Info() << "Card " << _card_number << " Successfully opened in MT mode" << msg_show;
		while(true)
		{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
