package ru.elcus.mil.guitest;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import net.miginfocom.swing.MigLayout;
import ru.elcus.mil.Eclus1553Exception;
import ru.elcus.mil.Elcus1553Device;
import ru.elcus.mil.MilWorkMode;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
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
			/* (non-Javadoc)
			 * @see java.lang.Runnable#run()
			 */
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
	DefaultListModel<String> model = new DefaultListModel<String>();
	public RTTest() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new MigLayout("", "[grow][]", "[][][grow]"));
		
		JLabel label = new JLabel("Номер платы");
		panel.add(label, "cell 0 0");
		label.setFont(new Font("Dialog", Font.BOLD, 14));
		
		JSpinner spinner = new JSpinner(new SpinnerNumberModel(0,0,100,1));
		panel.add(spinner, "cell 1 0");
		spinner.setFont(new Font("Dialog", Font.BOLD, 14));
		
		JButton button = new JButton("Запуск монитора");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (device==null)
				{
					device = new Elcus1553Device(Integer.parseInt(spinner.getValue().toString()));	
					try {
						device.initAs(MilWorkMode.eMilWorkModeRT);
					} catch (Eclus1553Exception e1) {
						e1.printStackTrace();
					}										
					device.addMsgReceivedListener((msg)->{
						model.addElement(msg.toString());
					});
				}
			}
		});
		panel.add(button, "cell 0 1");
		button.setFont(new Font("Dialog", Font.BOLD, 14));
		
		JButton button_1 = new JButton("Остановка монитора");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(device != null)
					try {
						device.setPause(true);
					} catch (Eclus1553Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			}
		});
		panel.add(button_1, "cell 1 1");
		button_1.setFont(new Font("Dialog", Font.BOLD, 14));
		
		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane, "cell 0 2 2 1,grow");
		
		JList<String> list = new JList<String>(model);
		scrollPane.setViewportView(list);
		list.setFont(new Font("Dialog", Font.BOLD, 14));
	}

}
