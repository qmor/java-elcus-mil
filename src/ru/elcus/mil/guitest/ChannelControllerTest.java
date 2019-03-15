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
import ru.elcus.mil.structs.EBus;

import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JRadioButton;
import javax.swing.LayoutStyle.ComponentPlacement;

/**
 * @author Guest
 *
 */
public class ChannelControllerTest {
	
	private JFrame frmChannelcontrollertest;
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChannelControllerTest window = new ChannelControllerTest();
					window.frmChannelcontrollertest.setVisible(true);
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
	DefaultListModel<String> modelPacket = new DefaultListModel<String>();
	
	public ChannelControllerTest() {
		initialize();
	}

	/*
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmChannelcontrollertest = new JFrame();
		frmChannelcontrollertest.setTitle("ChannelControllerTest");
		frmChannelcontrollertest.setBounds(100, 100, 450, 750);
		frmChannelcontrollertest.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frmChannelcontrollertest.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new MigLayout("", "[grow][grow]", "[][grow][][grow]"));
					
		JLabel lblCardNumber = new JLabel("Card number:");
		lblCardNumber.setFont(new Font("Dialog", Font.BOLD, 14));
		panel.add(lblCardNumber, "cell 0 0,growx");
		
		JSpinner spinner = new JSpinner(new SpinnerNumberModel(0,0,100,1));
		panel.add(spinner, "cell 1 0,alignx center");
		spinner.setFont(new Font("Dialog", Font.BOLD, 14));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		panel.add(scrollPane_1, "flowy,cell 0 1,grow");
		
		JTable table = new JTable(model);
		scrollPane_1.setViewportView(table);
		table.setFont(new Font("Dialog", Font.BOLD, 14));
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1, "cell 1 1,grow");
		
		JRadioButton line1 = new JRadioButton("Line 1",true);
		
		JRadioButton line2 = new JRadioButton("Line 2");
		ButtonGroup group = new ButtonGroup();
		group.add(line1);
		group.add(line2);
		
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_panel_1.createSequentialGroup()
					.addGap(23)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
						.addComponent(line2)
						.addComponent(line1))
					.addContainerGap(27, Short.MAX_VALUE))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(226)
					.addComponent(line1)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(line2)
					.addContainerGap(232, Short.MAX_VALUE))
		);
		panel_1.setLayout(gl_panel_1);
		
		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane, "cell 0 3,grow");
		
		JList<String> list = new JList<String>(modelPacket);
		scrollPane.setViewportView(list);
		list.setFont(new Font("Dialog", Font.BOLD, 14));
		
		JButton btnSend = new JButton("Send");
		panel.add(btnSend, "cell 0 2,growx");
		btnSend.setFont(new Font("Dialog", Font.BOLD, 14));
		
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				modelPacket.clear();
				if (device==null)
				{
					device = new Elcus1553Device(Integer.parseInt(spinner.getValue().toString()));	
					try {
						device.initAs(MilWorkMode.eMilWorkModeBC);
					} catch (Eclus1553Exception e1) {
						e1.printStackTrace();
					}										
					device.addMsgReceivedListener((msg)->{
						modelPacket.addElement(msg.toString());
					});	 
					device.addDebugReceivedListener((msg)->{
						modelPacket.addElement(msg);
					});
					spinner.setEnabled(false);
				}
				Mil1553Packet packet = model.getPacket();
				packet.bus=line1.isSelected()?EBus.eBusA:EBus.eBusB;
				device.sendPacket(packet);
			}			
		});
	}
	
	class TableModel extends DefaultTableModel{		
		private static final long serialVersionUID = 1205259912765952013L;
		private String column_names[]= {"Title","Code"};
		
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
			if(column != 1){
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
