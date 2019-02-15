package ru.elcus.mil.guitest;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;

import org.json.JSONArray;
import org.json.JSONObject;

import net.miginfocom.swing.MigLayout;
import ru.elcus.mil.Eclus1553Exception;
import ru.elcus.mil.Elcus1553Device;
import ru.elcus.mil.Mil1553Packet;
import ru.elcus.mil.MilWorkMode;
import ru.elcus.mildecoders.ClassByNameHelper;
import ru.elcus.mildecoders.IMil1553Decoder;
import javax.swing.JComboBox;


public class MTTest {

	private JFrame frame;
	private JButton btnStart, btnStop, btnSql;
	private JSpinner spinner;
	private JList<Mil1553Packet> list;
	private JTextArea textArea;
	
	private static final String dbFolder = "databases/";
	
	private Elcus1553Device device;
	MTListViewModel model = new MTListViewModel(dbFolder);
	
	private JButton chooseDB;
	private JLabel statusDB;
	
	enum btnStatus
	{
		mtStart,
		mtStop,
		SqlQuery,
		getPacket,
		changeDB,
		disabled
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MTTest window = new MTTest(args);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public MTTest(String[] args) {
		initialize();
		btnController(btnStatus.disabled);
		if (args.length>0)
			loadJSON(args[0]);
	}
	
	
	private void loadJSON(String jsonFileName)
	{
		File jsonFile = new File(jsonFileName);
		if (!jsonFile.exists())
			return;
		String content="";
		try {
			content = new String(Files.readAllBytes(Paths.get(jsonFile.toString())), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		JSONObject json = new JSONObject(content);

		
		JSONArray array= json.getJSONArray("decoders");
		ClassByNameHelper cbn = new ClassByNameHelper();
		for (Object o : array)
		{
			if (o instanceof JSONObject)
			{
				JSONObject jso = (JSONObject)o;
				String classname = jso.getString("class");
				Object inst = cbn.getInstanceByName(classname);
				if (inst!=null)
				{
					if (inst instanceof IMil1553Decoder)
					{
						IMil1553Decoder rt = (IMil1553Decoder)inst;
						rt.initDecoder(jso.getString("name"), jso.getInt("rt"));
						model.addDecoder(rt);
					}
				}
			}
		}
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
				
				model.closeConn();
				
				break;
			}
			
			case disabled:
				btnStop.setEnabled(false);
				break;
				
			case SqlQuery: {
				
				break;
			}
				
		}
	}
	
	private void initialize()
	{
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 549);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new MigLayout("", "[grow][grow]", "[][][][][grow][][grow][grow]"));
		
		JLabel label = new JLabel("Выбор шины");
		label.setFont(new Font("Dialog", Font.BOLD, 14));
		panel.add(label, "cell 0 0");
		
		SpinnerNumberModel model1 = new SpinnerNumberModel(0, 0, 3, 1);  
		spinner = new JSpinner(model1);
		spinner.setFont(new Font("Dialog", Font.BOLD, 14));
		panel.add(spinner, "cell 1 0");
		
		list= new JList<Mil1553Packet>();
		
		
		btnStart = new JButton("Запуск монитора");
		btnStart.setFont(new Font("Dialog", Font.BOLD, 14));
		btnStart.addActionListener(new ActionListenerController(btnStatus.mtStart));
		panel.add(btnStart, "cell 0 1");
		
		btnStop = new JButton("Остановка монитора");
		btnStop.addActionListener(new ActionListenerController(btnStatus.mtStop));
		
		btnStop.setFont(new Font("Dialog", Font.BOLD, 14));
		panel.add(btnStop, "cell 1 1");
		
		btnSql = new JButton("SQL запрос");
		panel.add(btnSql, "cell 0 2");
		
		chooseDB = new JButton("Выбор БД");
		panel.add(chooseDB, "cell 1 2");
		chooseDB.addActionListener(new ActionListenerController(btnStatus.changeDB));
		
		statusDB = new JLabel("База не выбрана. При запуске будет создана новая БД");
		panel.add(statusDB, "cell 0 3 2 1");
		
		textArea = new JTextArea();
		panel.add(textArea, "cell 0 4 2 1,grow");
		
		
		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane, "cell 0 5 2 3,grow");
		
		list.setModel(model);
		scrollPane.setViewportView(list);
		
		btnSql.addActionListener(new ActionListenerController(btnStatus.SqlQuery));
		
		list.addMouseListener(new ActionListenerController(btnStatus.getPacket));
		
	}


	class ActionListenerController extends MouseAdapter implements ActionListener
	{
		private btnStatus st;
		
		public ActionListenerController(btnStatus st)
		{
			this.st = st;
		}
		
		private void getPacket()
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
							window.editorPane.setText(list.getSelectedValue().decodeHTMLString);
							
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
		
		@Override
		public void mouseClicked(MouseEvent e)
		{
			switch(st)
			{
				case getPacket:
					this.getPacket();
					break;
			}
		}
		
		private void mtStart() {
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
		
		private void setDevicePause(boolean mode)
		{
			try {
				device.setPause(mode);
			} catch (Eclus1553Exception ex) {
				ex.printStackTrace();
			}
		}
		
		private void sqlQuery()
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
		
		private void changeDB()
		{
			JFileChooser fch = new JFileChooser();
			fch.setCurrentDirectory(new File(dbFolder));
			fch.showOpenDialog(frame);
		}
		
		@Override
		public void actionPerformed(ActionEvent ev) {
			switch(st)
			{
				case mtStart: {
					btnStart.setEnabled(false);
					btnSql.setEnabled(false);
					btnStop.setEnabled(true);
					
					if(device != null)
						setDevicePause(false);
					
					// comboBox.addItem(model.createNewDBConn());
					
					mtStart();
					
					break;
				}
				
				case mtStop: {
					btnStart.setEnabled(true);
					btnStop.setEnabled(false);
					btnSql.setEnabled(true);
					
					setDevicePause(true);
					
					model.closeConn();
					
					break;
				}
				
				case disabled:
					btnStop.setEnabled(false);
					break;
					
				case SqlQuery:
					sqlQuery();
					break;
				
				case changeDB: 
					changeDB();
					break;
					
			}
			
		}
		
		
	}
	
	
}
