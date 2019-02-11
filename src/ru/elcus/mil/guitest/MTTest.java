package ru.elcus.mil.guitest;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import net.miginfocom.swing.MigLayout;
import ru.elcus.mil.Eclus1553Exception;
import ru.elcus.mil.Elcus1553Device;
import ru.elcus.mil.Mil1553Packet;
import ru.elcus.mil.MilWorkMode;

import javax.swing.JSpinner;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.Font;

import javax.swing.SpinnerNumberModel;
import javax.swing.JTextArea;

public class MTTest {

	private JFrame frame;
	private JButton btnStart, btnStop, btnSql;
	
	enum btnStatus
	{
		mtStart,
		mtStop,
		SqlQuery,
		disabled
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MTTest window = new MTTest();
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
	MTListViewModel model = new MTListViewModel();
	
	public MTTest() {
		initialize();
		btnController(btnStatus.disabled);
	}
	
	private void btnController(btnStatus st)
	{
		switch(st)
		{
			case mtStart: {
				btnStart.setEnabled(false);
				btnSql.setEnabled(false);
				btnStop.setEnabled(true);
				
				if(device != null)
					try {
						device.setPause(false);
					} catch (Eclus1553Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				break;
			}
			
			case mtStop: {
				btnStart.setEnabled(true);
				btnStop.setEnabled(false);
				btnSql.setEnabled(true);
				
				try {
					device.setPause(true);
				} catch (Eclus1553Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				break;
			}
			
			case disabled:
				btnStop.setEnabled(false);
				break;
				
			case SqlQuery: {
				if(device != null)
					try {
						device.setPause(true);
					} catch (Eclus1553Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				break;
			}
				
		}
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 549);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new MigLayout("", "[grow][grow]", "[][][][grow][][grow][grow]"));
		
		JLabel label = new JLabel("Выбор шины");
		label.setFont(new Font("Dialog", Font.BOLD, 14));
		panel.add(label, "cell 0 0");
		
		SpinnerNumberModel model1 = new SpinnerNumberModel(0, 0, 3, 1);  
		JSpinner spinner = new JSpinner(model1);
		spinner.setFont(new Font("Dialog", Font.BOLD, 14));
		panel.add(spinner, "cell 1 0");
		
		JList<Mil1553Packet> list= new JList();
		
		btnStart = new JButton("Запустить монитор");
		btnStart.setFont(new Font("Dialog", Font.BOLD, 14));
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnController(btnStatus.mtStart);
				
				if (device==null)
				{
					device = new Elcus1553Device(Integer.parseInt(spinner.getValue().toString()));
				
					try {
						device.initAs(MilWorkMode.eMilWorkModeMT);
					} catch (Eclus1553Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}  
					
					device.addMsgReceivedListener((msg)->{
						model.insertElementAndAddToList(msg);
					});
				}
				
				list.setModel(model);
			}
		});
		panel.add(btnStart, "cell 0 1");
		
		btnStop = new JButton("Остановить монитор");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				btnController(btnStatus.mtStop);
			}
		});
		
		btnStop.setFont(new Font("Dialog", Font.BOLD, 14));
		panel.add(btnStop, "cell 1 1");
		
		btnSql = new JButton("SQL запрос");
		panel.add(btnSql, "cell 0 2 2 1");
		
		JTextArea textArea = new JTextArea();
		panel.add(textArea, "cell 0 3 2 1,grow");
		
		
		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane, "cell 0 4 2 3,grow");
		
		list.setModel(model);
		scrollPane.setViewportView(list);
		
		btnSql.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				model.removeAllElements();
				try
				{
					list.setModel(model.getListByQuery(textArea.getText()));
				}
				catch(java.lang.IllegalArgumentException e1)
				{
					System.out.println("java.lang.IllegalArgumentException: model must be non null");
				}
			}
		});
		
		list.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e)
			{
				if(!list.isSelectionEmpty())
				{
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							try {
								MTPacketGUI window = new MTPacketGUI();
								window.frame.setVisible(true);
								
								window.frame.setTitle("CW " + String.format("%04x", list.getSelectedValue().commandWord));
								
								window.l_AW.setText(String.format("%04x", list.getSelectedValue().answerWord));;
								window.l_CW.setText(String.format("%04x ", list.getSelectedValue().commandWord));
								window.l_bus.setText(String.valueOf(list.getSelectedValue().bus));
								window.l_date.setText(String.valueOf(list.getSelectedValue().date));
								window.l_format.setText(String.valueOf(list.getSelectedValue().format));
								window.l_status.setText(String.valueOf(list.getSelectedValue().status));
								
								int numofrws = list.getSelectedValue().dataWords.length;
								for(int i = 0; i < numofrws; i++)
									window.model.addRow(new Object[] {String.format("%04x", list.getSelectedValue().dataWords[i])});
								
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
				}
			}
		});
		
	}

}
