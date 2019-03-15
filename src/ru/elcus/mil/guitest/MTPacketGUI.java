package ru.elcus.mil.guitest;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;
import ru.elcus.mil.Mil1553Packet;
import ru.elcus.mil.TimeManipulation;

class TableModel extends DefaultTableModel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1210374564996406178L;
	Mil1553Packet packet;
	public TableModel(Mil1553Packet packet)
	{
		this.packet = packet;
	}
	@Override
	public boolean isCellEditable(int row, int column) {
	return false;
	}
	@Override
	public int getColumnCount() {
	return 2;
	}
	@Override
	public String getColumnName(int column) {
		if (column == 0)
			return "#";
		return "DataWord";
	}
	@Override
	public int getRowCount() {
		if (packet!=null)
		return 32;
		return 0;
	}
	@Override
	public Object getValueAt(int row, int column) {
		if (column == 0)
			return row;
		return  String.format("%04X", packet.dataWords[row]);
	}
	
}
public class MTPacketGUI {

	JFrame frame;
	JLabel l_CW;
	JLabel l_AW;
	JLabel l_format;
	JLabel l_status;
	JLabel l_date;
	JLabel l_bus;
	DefaultTableModel model;
	private JTable table;
	TableModel tm;
	JEditorPane editorPane;
	
	/**
	 * Create the application.
	 */
	
	public MTPacketGUI(Mil1553Packet packet) {
		initialize();
		frame.setTitle("CW " + String.format("%04x", packet.commandWord));
		l_AW.setText(String.format("%04x", packet.answerWord));
		l_CW.setText(String.format("%04x ", packet.commandWord));
		l_bus.setText(String.valueOf(packet.bus));
		l_date.setText(TimeManipulation.ToLongTimeStringMillis(packet.date));
		l_format.setText(String.valueOf(packet.format));
		l_status.setText(String.valueOf(packet.status));
		editorPane.setText(packet.decodeHTMLString);
		tm = new TableModel(packet);
		table.setModel(tm);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 466, 596);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new MigLayout("", "[127px][25px][250px,grow]", "[15px][15px][15px][15px][15px][15px][268.00px,grow][144.00px,grow]"));
		
		JLabel lblCommandword = new JLabel("CommandWord:");
		frame.getContentPane().add(lblCommandword, "cell 0 0,alignx left,aligny center");
		
		l_CW = new JLabel("Null");
		frame.getContentPane().add(l_CW, "cell 2 0,alignx left,aligny center");
		
		JLabel lblNewLabel = new JLabel("AnswerWord:");
		frame.getContentPane().add(lblNewLabel, "cell 0 1,alignx left,aligny top");
		
		l_AW = new JLabel("Null");
		frame.getContentPane().add(l_AW, "cell 2 1,alignx left,aligny center");
		
		JLabel lblNewLabel_1 = new JLabel("Format:");
		frame.getContentPane().add(lblNewLabel_1, "cell 0 2,alignx left,aligny center");
		
		l_format = new JLabel("Null");
		frame.getContentPane().add(l_format, "cell 2 2,alignx left,aligny center");
		
		JLabel lblNewLabel_2 = new JLabel("Bus:");
		frame.getContentPane().add(lblNewLabel_2, "cell 0 3,alignx left,aligny center");
		
		l_bus = new JLabel("Null");
		frame.getContentPane().add(l_bus, "cell 2 3,alignx left,aligny center");
		
		JLabel lblNewLabel_3 = new JLabel("Date:");
		frame.getContentPane().add(lblNewLabel_3, "cell 0 4,alignx left,aligny center");
		
		l_date = new JLabel("Null");
		frame.getContentPane().add(l_date, "cell 2 4,alignx left,aligny center");
		
		JLabel lblNewLabel_4 = new JLabel("Status: ");
		frame.getContentPane().add(lblNewLabel_4, "cell 0 5,alignx left,aligny center");
		
		l_status = new JLabel("Null");
		frame.getContentPane().add(l_status, "cell 2 5,alignx left,aligny center");
		
		JScrollPane scrollPane = new JScrollPane();
		frame.getContentPane().add(scrollPane, "cell 0 6 3 1,grow");
		
		
		table = new JTable();
		scrollPane.setViewportView(table);
		
		editorPane = new JEditorPane();
		editorPane.setContentType("text/html");
		frame.getContentPane().add(editorPane, "cell 0 7 3 1,grow");
		
	}

}
