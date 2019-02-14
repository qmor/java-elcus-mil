package ru.elcus.mildecoders;

import java.nio.ByteBuffer;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import ru.elcus.mil.Mil1553Packet;

public class MBOKZ2V implements IMil1553Decoder {
Integer rt;
String name;
	@Override
	public int getDeviceRT() {
		return rt;
	}

	@Override
	public void initDecoder(String name, Integer rt) {
		this.name = name;
		this.rt = rt;
	}

	@Override
	public void processPacket(Mil1553Packet packet) {
		StringBuilder sb = new StringBuilder();
		int cmdword = packet.commandWord&2047;
		if (cmdword==0x440)
		{
			sb.append("<ul><li>Пакет штатной ТМИ</li>");
			sb.append(String.format("<li>Номер массива %d</li>", packet.dataWords[0]));
			sb.append(String.format("<li>Время привязки ТМИ %d</li>", packet.dataWords[1]<<16 | packet.dataWords[2]));
			sb.append(String.format("<li>Время привязки информации%d</li>", packet.dataWords[3]));
			sb.append(String.format("<li>Код состояния 1 %04Xh</li>", packet.dataWords[4]));
			sb.append(String.format("<li>Код состояния 2 %04Xh</li>", packet.dataWords[5]));
			sb.append(String.format("<li>Заводской номер мБОКЗ-2В, включая номер измерительного полукомплекта  %04Xh</li>", packet.dataWords[6]));
			sb.append(String.format("<li>Слово внутреннего теста %04Xh</li>", packet.dataWords[7]));
			
			
			byte[] bytes = new byte[] {(byte) (packet.dataWords[8]&0xff), (byte) (packet.dataWords[8] >> 8), (byte) (packet.dataWords[9]&0xff), (byte) (packet.dataWords[9] >> 8)};
			ByteBuffer bb = ByteBuffer.wrap(bytes);
			sb.append(String.format("<li>Фокусное расстояние %f</li>", bb.getFloat()));
			
			bytes[0] = (byte) (packet.dataWords[10]&0xff); bytes[1] = (byte) (packet.dataWords[10] >> 8);
			bytes[2] = (byte) (packet.dataWords[11]&0xff); bytes[3] = (byte) (packet.dataWords[11] >> 8);
			bb = ByteBuffer.wrap(bytes);
			sb.append(String.format("<li>Координата Хо гл. точки ВСК %f</li>", bb.getFloat()));
			
			bytes[0] = (byte) (packet.dataWords[12]&0xff); bytes[1] = (byte) (packet.dataWords[12] >> 8);
			bytes[2] = (byte) (packet.dataWords[13]&0xff); bytes[3] = (byte) (packet.dataWords[13] >> 8);
			bb = ByteBuffer.wrap(bytes);
			sb.append(String.format("<li>Координата Уо гл. точки ВСК %f</li>", bb.getFloat()));
			
			
			sb.append(String.format("<li>Время экспонирования %d</li>", packet.dataWords[14]));
			sb.append(String.format("<li>Среднее значение сигнала %d</li>", packet.dataWords[15]));
			sb.append(String.format("<li>СКО сигнала %d</li>", packet.dataWords[16]));
			sb.append(String.format("<li>Статус КП %d</li>", packet.dataWords[17]));
			sb.append(String.format("<li>Счетчик КС КП %d</li>", packet.dataWords[18]));
			sb.append(String.format("<li>Температура КМОП-матрицы, •10 %d</li>", packet.dataWords[19]));
			sb.append(String.format("<li>Резерв %d</li>", packet.dataWords[20]));
			sb.append(String.format("<li>Резерв %d</li>", packet.dataWords[21]));
			sb.append(String.format("<li>Резерв %d</li>", packet.dataWords[22]));
			sb.append(String.format("<li>Резерв %d</li>", packet.dataWords[23]));
			sb.append(String.format("<li>Резерв %d</li>", packet.dataWords[24]));
			sb.append(String.format("<li>Контрольная сумма каталога 0 %04Xh</li>", packet.dataWords[25]));
			sb.append(String.format("<li>Контрольная сумма каталога 1 %04Xh</li>", packet.dataWords[26]));
			sb.append(String.format("<li>Контрольная сумма констант 0 %04Xh</li>", packet.dataWords[27]));
			sb.append(String.format("<li>Контрольная сумма констант 1 %04Xh</li>", packet.dataWords[28]));
			sb.append(String.format("<li>Контрольная сумма программы 0 %04Xh</li>", packet.dataWords[29]));
			sb.append(String.format("<li>Контрольная сумма программы 1 %04Xh</li>", packet.dataWords[30]));
			sb.append(String.format("<li>Номер версии программы %X.%X.%X</li></ul>", packet.dataWords[31] >> 12, packet.dataWords[31] >> 8 & 0x0f, packet.dataWords[31] & 0xff));


		}
		packet.decodeHTMLString = sb.toString();
	}

}
