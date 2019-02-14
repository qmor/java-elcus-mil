package ru.elcus.mil.guitest;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import net.miginfocom.swing.MigLayout;
import ru.elcus.mil.Eclus1553Exception;
import ru.elcus.mil.Elcus1553Device;
import ru.elcus.mil.Mil1553Packet;
import ru.elcus.mil.MilWorkMode;

import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RTTest {

	private JFrame frame;

	/*
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RTTest window = new RTTest();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	private Elcus1553Device device;
	TableModel model = new TableModel();
	String column_names[]= {"Title","Code"};
	DefaultListModel<String> modelPacket = new DefaultListModel<String>();

	public RTTest() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Device");
		frame.setBounds(100, 100, 450, 750);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new MigLayout("", "[grow][grow]", "[][][][grow][][grow]"));
					
		JLabel lblCardNumber = new JLabel("Card number:");
		lblCardNumber.setFont(new Font("Dialog", Font.BOLD, 14));
		panel.add(lblCardNumber, "cell 0 0,growx");
		
		JSpinner card_number = new JSpinner(new SpinnerNumberModel(0,0,100,1));
		panel.add(card_number, "cell 1 0,alignx center");
		card_number.setFont(new Font("Dialog", Font.BOLD, 14));
		
		JLabel lblSubaddress = new JLabel("Subaddress:");
		panel.add(lblSubaddress, "cell 0 1");
		lblSubaddress.setFont(new Font("Dialog", Font.BOLD, 14));
		
		JSpinner subaddress = new JSpinner(new SpinnerNumberModel(0,0,100,1));
		panel.add(subaddress, "cell 1 1,alignx center");
		subaddress.setFont(new Font("Dialog", Font.BOLD, 14));
		
		JButton start = new JButton("Start");
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (device==null)
				{
					device = new Elcus1553Device(Integer.parseInt(card_number.getValue().toString()));	
					try {
						device.initAs(MilWorkMode.eMilWorkModeRT);
					} catch (Eclus1553Exception e1) {
						e1.printStackTrace();
					}										
					device.addMsgReceivedListener((msg)->{
						modelPacket.addElement(msg.toString());
					});
					device.addDebugReceivedListener((msg)->{
						modelPacket.addElement(msg);
					});
				}
			}
		});
		panel.add(start, "cell 0 2,alignx left");
		start.setFont(new Font("Dialog", Font.BOLD, 14));
		
		JButton stop = new JButton("Stop");
		stop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (device!=null)
				{
					try {
						device.setPause(true);
					} catch (Eclus1553Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		panel.add(stop, "cell 1 2");
		stop.setFont(new Font("Dialog", Font.BOLD, 14));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		panel.add(scrollPane_1, "flowy,cell 0 3,grow");
		
		JTable table = new JTable(model);
		scrollPane_1.setViewportView(table);
		table.setFont(new Font("Dialog", Font.BOLD, 14));
		
		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane, "cell 0 5,grow");
		
		JList<String> output = new JList<String>(modelPacket);
		scrollPane.setViewportView(output);
		output.setFont(new Font("Dialog", Font.BOLD, 14));
		
		JButton btnPut = new JButton("Put");
		panel.add(btnPut, "cell 0 4,growx");
		btnPut.setFont(new Font("Dialog", Font.BOLD, 14));
		
		btnPut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				if (device==null)
				{
					device = new Elcus1553Device(Integer.parseInt(card_number.getValue().toString()));	
					try {
						device.initAs(MilWorkMode.eMilWorkModeRT);
					} catch (Eclus1553Exception e1) {
						e1.printStackTrace();
					}										
					device.addMsgReceivedListener((msg)->{
						modelPacket.addElement(msg.toString());
					});	 
					device.addDebugReceivedListener((msg)->{
						modelPacket.addElement(msg);
					});
				}
				Mil1553Packet packet = model.getPacket();
				device.sendPacket(packet);
			}			
		});
	}
	
	class TableModel extends DefaultTableModel{		
		private static final long serialVersionUID = -3894503544563437459L;	
		
		Mil1553Packet packet = new Mil1553Packet();
		
		public Mil1553Packet getPacket() {
			return packet;
		}
		
		@Override
		public int getRowCount() {			
			return 33;
		}
		
		@Override
		public int getColumnCount() {
			return 2;
		}	
		
        @Override 
        public String getColumnName(int index) { 
            return column_names[index]; 
        } 
        
		@Override
		public Object getValueAt(int row, int column) {			
			if(row == 0 && column==0){
				return "Command word";
			}
			if(column!=1){
				return "Data word ["+row+"]";
			}
			if (row == 0)
				return Integer.toHexString(packet.commandWord).toUpperCase();
			return Integer.toHexString(packet.dataWords[row-1]).toUpperCase();
		}
		
		@Override
		public void setValueAt(Object aValue, int row, int column) {	
			if (row == 0) {
				try {
					packet.commandWord=Short.parseShort(aValue.toString(),16);
				} catch(NumberFormatException e) {
					modelPacket.addElement(e.getMessage()+"\n");
				}
				return;
			}
			try {
				packet.dataWords[row-1]=Short.parseShort(aValue.toString(),16);
			} catch(NumberFormatException e) {
				modelPacket.addElement(e.getMessage()+"\n");
			}
			return;
		}
	}	
}
