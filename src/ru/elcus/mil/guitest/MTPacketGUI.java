package ru.elcus.mil.guitest;

import javax.swing.JFrame;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import javax.swing.JScrollPane;

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
	
	/**
	 * Create the application.
	 */
	public MTPacketGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 405, 400);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{160, 258, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 160, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		JLabel lblCommandword = new JLabel("CommandWord:");
		GridBagConstraints gbc_lblCommandword = new GridBagConstraints();
		gbc_lblCommandword.anchor = GridBagConstraints.WEST;
		gbc_lblCommandword.insets = new Insets(0, 15, 5, 5);
		gbc_lblCommandword.gridx = 0;
		gbc_lblCommandword.gridy = 0;
		frame.getContentPane().add(lblCommandword, gbc_lblCommandword);
		
		l_CW = new JLabel("Null");
		GridBagConstraints gbc_lblNull = new GridBagConstraints();
		gbc_lblNull.anchor = GridBagConstraints.WEST;
		gbc_lblNull.insets = new Insets(0, 0, 5, 0);
		gbc_lblNull.gridx = 1;
		gbc_lblNull.gridy = 0;
		frame.getContentPane().add(l_CW, gbc_lblNull);
		
		JLabel lblNewLabel = new JLabel("AnswerWord:");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.ABOVE_BASELINE_LEADING;
		gbc_lblNewLabel.insets = new Insets(0, 15, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 1;
		frame.getContentPane().add(lblNewLabel, gbc_lblNewLabel);
		
		l_AW = new JLabel("Null");
		GridBagConstraints gbc_lblNull_1 = new GridBagConstraints();
		gbc_lblNull_1.anchor = GridBagConstraints.WEST;
		gbc_lblNull_1.insets = new Insets(0, 0, 5, 0);
		gbc_lblNull_1.gridx = 1;
		gbc_lblNull_1.gridy = 1;
		frame.getContentPane().add(l_AW, gbc_lblNull_1);
		
		JLabel lblNewLabel_1 = new JLabel("Format:");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_1.insets = new Insets(0, 15, 5, 5);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 2;
		frame.getContentPane().add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		l_format = new JLabel("Null");
		GridBagConstraints gbc_lblNull_2 = new GridBagConstraints();
		gbc_lblNull_2.anchor = GridBagConstraints.WEST;
		gbc_lblNull_2.insets = new Insets(0, 0, 5, 0);
		gbc_lblNull_2.gridx = 1;
		gbc_lblNull_2.gridy = 2;
		frame.getContentPane().add(l_format, gbc_lblNull_2);
		
		JLabel lblNewLabel_2 = new JLabel("Bus:");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_2.insets = new Insets(0, 15, 5, 5);
		gbc_lblNewLabel_2.gridx = 0;
		gbc_lblNewLabel_2.gridy = 3;
		frame.getContentPane().add(lblNewLabel_2, gbc_lblNewLabel_2);
		
		l_bus = new JLabel("Null");
		GridBagConstraints gbc_lblNull_3 = new GridBagConstraints();
		gbc_lblNull_3.anchor = GridBagConstraints.WEST;
		gbc_lblNull_3.insets = new Insets(0, 0, 5, 0);
		gbc_lblNull_3.gridx = 1;
		gbc_lblNull_3.gridy = 3;
		frame.getContentPane().add(l_bus, gbc_lblNull_3);
		
		JLabel lblNewLabel_3 = new JLabel("Date:");
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_3.insets = new Insets(0, 15, 5, 5);
		gbc_lblNewLabel_3.gridx = 0;
		gbc_lblNewLabel_3.gridy = 4;
		frame.getContentPane().add(lblNewLabel_3, gbc_lblNewLabel_3);
		
		l_date = new JLabel("Null");
		GridBagConstraints gbc_lblNull_4 = new GridBagConstraints();
		gbc_lblNull_4.anchor = GridBagConstraints.WEST;
		gbc_lblNull_4.insets = new Insets(0, 0, 5, 0);
		gbc_lblNull_4.gridx = 1;
		gbc_lblNull_4.gridy = 4;
		frame.getContentPane().add(l_date, gbc_lblNull_4);
		
		JLabel lblNewLabel_4 = new JLabel("Status: ");
		GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
		gbc_lblNewLabel_4.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_4.insets = new Insets(0, 15, 5, 5);
		gbc_lblNewLabel_4.gridx = 0;
		gbc_lblNewLabel_4.gridy = 5;
		frame.getContentPane().add(lblNewLabel_4, gbc_lblNewLabel_4);
		
		l_status = new JLabel("Null");
		GridBagConstraints gbc_lblNull_5 = new GridBagConstraints();
		gbc_lblNull_5.anchor = GridBagConstraints.WEST;
		gbc_lblNull_5.insets = new Insets(0, 0, 5, 0);
		gbc_lblNull_5.gridx = 1;
		gbc_lblNull_5.gridy = 5;
		frame.getContentPane().add(l_status, gbc_lblNull_5);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 6;
		frame.getContentPane().add(scrollPane, gbc_scrollPane);
		
		model = new DefaultTableModel();
		model.addColumn("DataWords");
		
		table = new JTable(model);
		scrollPane.setViewportView(table);
		
	}

}
