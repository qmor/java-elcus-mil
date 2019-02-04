package ru.elcus.mil.guitest;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;

import net.miginfocom.swing.MigLayout;
import ru.elcus.mil.Eclus1553Exception;
import ru.elcus.mil.Elcus1553Device;
import ru.elcus.mil.Mil1553Packet;
import ru.elcus.mil.MilWorkMode;

import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;

/**
 * @author Guest
 *
 */
public class ChannelControllerTest {
	
	private JFrame frmChannelcontrollettest;
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChannelControllerTest window = new ChannelControllerTest();
					window.frmChannelcontrollettest.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/*
	 * Create the application.
	 */
	private Elcus1553Device device;
	TableModel model = new TableModel();
	MTListViewModel modelPacket = new MTListViewModel();
	
	public ChannelControllerTest() {
		initialize();
	}

	/*
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmChannelcontrollettest = new JFrame();
		frmChannelcontrollettest.setTitle("ChannelControllerTest");
		frmChannelcontrollettest.setBounds(100, 100, 450, 750);
		frmChannelcontrollettest.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frmChannelcontrollettest.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new MigLayout("", "[grow][grow]", "[][grow][][grow]"));
					
		JLabel lblCardNumber = new JLabel("Card number:");
		lblCardNumber.setFont(new Font("Dialog", Font.BOLD, 14));
		panel.add(lblCardNumber, "cell 0 0,growx");
		
		JSpinner spinner = new JSpinner();
		panel.add(spinner, "cell 1 0");
		spinner.setFont(new Font("Dialog", Font.BOLD, 14));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		panel.add(scrollPane_1, "flowy,cell 0 1,grow");
		
		JTable table = new JTable(model);
		scrollPane_1.setViewportView(table);
		table.setFont(new Font("Dialog", Font.BOLD, 14));
		
		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane, "cell 0 3,grow");
		
		JList<Mil1553Packet> list = new JList<Mil1553Packet>(modelPacket);
		scrollPane.setViewportView(list);
		list.setFont(new Font("Dialog", Font.BOLD, 14));
		
		JButton btnSend = new JButton("Send");
		panel.add(btnSend, "cell 0 2,growx");
		btnSend.setFont(new Font("Dialog", Font.BOLD, 14));
		
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (device==null)
					device = new Elcus1553Device(Integer.parseInt(spinner.getValue().toString()));
				try {
					device.initAs(MilWorkMode.eMilWorkModeMT);
				} catch (Eclus1553Exception e1) {
					e1.printStackTrace();
				}
				
				device.addMsgReceivedListener((msg)->{
					modelPacket.addElement(msg);
				});
				
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
			return 1;
		}		
		
		@Override
		public Object getValueAt(int row, int column) {			
			if (row == 0)
				return Integer.toHexString(packet.commandWord).toUpperCase();
			return Integer.toHexString(packet.dataWords[row-1]).toUpperCase();
		}
		
		@Override
		public void setValueAt(Object aValue, int row, int column) {	
			if (row == 0) {
				packet.commandWord=Short.parseShort(aValue.toString(),16);
				return;
			}
			packet.dataWords[row-1]=Short.parseShort(aValue.toString(),16);
		}
	}	
}
