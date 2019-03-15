package ru.elcus.mildecoders;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.swing.JComponent;
import javax.swing.JPanel;

import ru.elcus.mil.Mil1553Packet;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;

public class MKABrkBck implements IMil1553Decoder {
	Integer rt;
	String name;
	JPanel panel;
	private JLabel lblTime;
	private JLabel lblFrameType;
	private JLabel lblReplayMode;
	private JLabel lblWritePointer;
	private JLabel lblReadPointer;
	private JLabel lblWorkMode;
	private JLabel lblNewLabel_5;
	private JLabel labelBRKtmi;
	
	public MKABrkBck() {
	panel = new JPanel();
	panel.setLayout(new MigLayout("", "[][19.00][][][][]", "[][][][][][][]"));
	
	JLabel lblNewLabel = new JLabel("Время");
	panel.add(lblNewLabel, "cell 0 0");
	
	lblTime = new JLabel("0");
	panel.add(lblTime, "cell 1 0");
	
	JLabel lblFrameTypeа = new JLabel("Тип кадра");
	panel.add(lblFrameTypeа, "cell 0 1");
	
	lblFrameType = new JLabel("0");
	panel.add(lblFrameType, "cell 1 1");
	
	JLabel lblNewLabel_1 = new JLabel("Режим воспр");
	panel.add(lblNewLabel_1, "cell 0 2");
	
	lblReplayMode = new JLabel("0");
	panel.add(lblReplayMode, "cell 1 2");
	
	JLabel lblNewLabel_2 = new JLabel("Указатель записи");
	panel.add(lblNewLabel_2, "cell 0 3");
	
	lblWritePointer = new JLabel("0");
	panel.add(lblWritePointer, "cell 1 3");
	
	JLabel lblNewLabel_3 = new JLabel("Указатель воспр");
	panel.add(lblNewLabel_3, "cell 0 4");
	
	lblReadPointer = new JLabel("0");
	panel.add(lblReadPointer, "cell 1 4");
	
	JLabel lblNewLabel_4 = new JLabel("Режим работы");
	panel.add(lblNewLabel_4, "cell 0 5");
	
	lblWorkMode = new JLabel("0");
	panel.add(lblWorkMode, "cell 1 5");
	
	lblNewLabel_5 = new JLabel("ТМИ БРК");
	panel.add(lblNewLabel_5, "cell 0 6");
	
	labelBRKtmi = new JLabel("0");
	panel.add(labelBRKtmi, "cell 1 6");
	}
	
	@Override
	public int getDeviceRT() {
		return rt;
	}

	@Override
	public void initDecoder(String name, Integer rt) {
		this.name = name;
		this.rt = rt;
		
	}
	private static final char[] hexArray = "0123456789ABCDEF".toCharArray();
	public static String bytesToHex(byte[] bytes,int offset,int len) {
		if (bytes==null)
			return null;
		char[] hexChars = new char[len * 2];
		for ( int j = offset; j < offset+len; j++ ) {
			int v = bytes[j] & 0xFF;
			hexChars[(j-offset) * 2] = hexArray[v >>> 4];
			hexChars[(j-offset) * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	@Override
	public void processPacket(Mil1553Packet packet) {
		int sa = Mil1553Packet.getSubAddress(packet.commandWord);
		int rtr =Mil1553Packet.getRTRBit(packet.commandWord); 
		if (rtr==0 && sa>=1&& sa<=8)
		{
			packet.decodeHTMLString = String.format("КПИ %d",sa );
			packet.shortDescr = String.format("КПИ %d",sa);
		}
		else if (rtr == 1 && sa == 10)
		{
			packet.decodeHTMLString = String.format("ПК");
			packet.shortDescr =  String.format("ПК");
		}
		else if (rtr == 0 && sa == 11)
		{
			packet.decodeHTMLString = String.format("КвтПК");
			packet.shortDescr =  String.format("КвтПК");
		}		
		
		else if (rtr==0 && sa>=29&& sa<=30)
		{
			packet.decodeHTMLString = String.format("ТМИ КИС %d",sa-28 );
			packet.shortDescr =  String.format("ТМИ КИС %d",sa-28 );
			labelBRKtmi.setText(bytesToHex(packet.dataWordsAsByteArray(),0,64));
		}
		else if (rtr==1 && sa>12&& sa<=28)
		{
			packet.decodeHTMLString = String.format("ТМИ БЦК %d",sa-11 );
			packet.shortDescr =  String.format("ТМИ БЦК %d",sa-11 );
		}
		else if (rtr==1 && sa==12)
		{
			
			ByteBuffer bb = ByteBuffer.wrap(packet.dataWordsAsByteArray());
			bb.order(ByteOrder.LITTLE_ENDIAN);
			
			StringBuilder sb = new StringBuilder();
			Double d = bb.getDouble();
			lblTime.setText(d.toString());
			
			sb.append(String.format("Время %f <br>", d));
			
			Short s = bb.getShort();
			lblFrameType.setText(s.toString());
			
			sb.append(String.format("Тип кадра %d <br>", s));
			
			s=bb.getShort();
			lblReplayMode.setText(s.toString());
			
			sb.append(String.format("Режим воспроизведения %d <br>", s));
			
			
			Integer i = bb.getInt();
			lblWritePointer.setText(i.toString());
			sb.append(String.format("Указатель записи %d <br>", i));
			
			i = bb.getInt();
			lblReadPointer.setText(i.toString());
			sb.append(String.format("Указатель воспроизведения %d <br>", i));
			
			i=bb.getInt();
			lblWorkMode.setText(i.toString());
			sb.append(String.format("Режим работы %d <br>", i));
			packet.decodeHTMLString = sb.toString();
			packet.shortDescr =  String.format("ТМИ БЦК осн");
		}

		
		
	}

	@Override
	public JComponent getGui() {
		return panel;
	}

}
