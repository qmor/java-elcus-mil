package ru.elcus.mil.guitest;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import net.miginfocom.swing.MigLayout;
import ru.elcus.mil.Elcus1553Device;

import javax.swing.JSpinner;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MTTest {

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
		
		JSpinner spinner = new JSpinner();
		panel.add(spinner, "cell 1 0");
		
		JButton button = new JButton("Запуск монитора");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (device==null)
					device = new Elcus1553Device(Integer.parseInt(spinner.getValue().toString()));
				device.addMsgReceivedListener((msg)->{
					model.addElement(msg);
				});
			}
		});
		panel.add(button, "cell 0 1");
		
		JButton button_1 = new JButton("Остановка монитора");
		panel.add(button_1, "cell 1 1");
		
		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane, "cell 0 2 2 1,grow");
		
		JList list = new JList();
		list.setModel(model);
		scrollPane.setViewportView(list);
	}

}
