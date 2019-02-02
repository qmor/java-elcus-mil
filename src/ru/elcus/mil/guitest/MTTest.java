package ru.elcus.mil.guitest;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import net.miginfocom.swing.MigLayout;
import ru.elcus.mil.Eclus1553Exception;
import ru.elcus.mil.Elcus1553Device;
import ru.elcus.mil.MilWorkMode;

import javax.swing.JSpinner;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class MTTest {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
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
		panel.setLayout(new MigLayout("", "[grow][]", "[][][][][grow][grow]"));
		
		JLabel label = new JLabel("Номер платы");
		label.setFont(new Font("Dialog", Font.BOLD, 14));
		panel.add(label, "cell 0 0");
		
		JSpinner spinner = new JSpinner();
		spinner.setFont(new Font("Dialog", Font.BOLD, 14));
		panel.add(spinner, "cell 1 0");
		
		JButton button = new JButton("Запуск монитора");
		button.setFont(new Font("Dialog", Font.BOLD, 14));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (device==null)
					device = new Elcus1553Device(Integer.parseInt(spinner.getValue().toString()));
				try {
					device.initAs(MilWorkMode.eMilWorkModeMT);
				} catch (Eclus1553Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				device.addMsgReceivedListener((msg)->{
					model.addElement(msg);
				});
			}
		});
		panel.add(button, "cell 0 1");
		
		JButton button_1 = new JButton("Остановка монитора");
		
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				try {
					device.setPause(true);
				} catch (Eclus1553Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		button_1.setFont(new Font("Dialog", Font.BOLD, 14));
		panel.add(button_1, "cell 1 1");
		
		JLabel label_1 = new JLabel("Фильтр пакетов:");
		label_1.setFont(new Font("Dialog", Font.BOLD, 14));
		panel.add(label_1, "cell 0 2");
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Все пакеты", "Часть пакетов", "И так далее"}));
		panel.add(comboBox, "cell 1 2,growx");
		
		
		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane, "cell 0 3 2 3,grow");
		
		JList list = new JList();
		list.setModel(model);
		scrollPane.setViewportView(list);
	}

}
