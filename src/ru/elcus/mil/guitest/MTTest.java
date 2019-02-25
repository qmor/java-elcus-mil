package ru.elcus.mil.guitest;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;

import org.json.JSONArray;
import org.json.JSONObject;

import net.miginfocom.swing.MigLayout;
import ru.elcus.mil.EMilPacketStatus;
import ru.elcus.mil.Eclus1553Exception;
import ru.elcus.mil.Elcus1553Device;
import ru.elcus.mil.Mil1553Packet;
import ru.elcus.mil.MilWorkMode;
import ru.elcus.mildecoders.ClassByNameHelper;
import ru.elcus.mildecoders.IMil1553Decoder;


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
	private File currDBfile;
	private JLabel label_1;
	
	enum btnStatus{
		mtStart,
		mtStop,
		SqlQuery,
		getPacket,
		changeDB
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
		if (args.length>0)
			loadJSON(args[0]);
	}
	
	
	private void loadJSON(String jsonFileName){
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
	
	private void initialize(){
		frame = new JFrame("MTTest");
		frame.setBounds(100, 100, 694, 549);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new MigLayout("", "[][grow][grow]", "[][][][][22.00][][grow][grow]"));
		
		JLabel label = new JLabel("Выбор платы");
		label.setFont(new Font("Dialog", Font.BOLD, 14));
		panel.add(label, "cell 0 0 2 1");
		
		SpinnerNumberModel model1 = new SpinnerNumberModel(0, 0, 3, 1);  
		spinner = new JSpinner(model1);
		spinner.setFont(new Font("Dialog", Font.BOLD, 14));
		panel.add(spinner, "cell 2 0,growx");
		
		list = new JList<Mil1553Packet>();
		list.setCellRenderer(new FaildItemsOfListRenderer());
		
		btnStart = new JButton("Запуск монитора");
		btnStart.setFont(new Font("Dialog", Font.BOLD, 14));
		btnStart.addActionListener(new ActionListenerController(btnStatus.mtStart));
		panel.add(btnStart, "cell 0 1 2 1,growx");
		
		btnStop = new JButton("Остановка монитора");
		btnStop.setEnabled(false);
		btnStop.addActionListener(new ActionListenerController(btnStatus.mtStop));
		
		
		btnStop.setFont(new Font("Dialog", Font.BOLD, 14));
		panel.add(btnStop, "cell 2 1,growx");
		
		chooseDB = new JButton("Выбор БД");
		panel.add(chooseDB, "cell 0 2 2 1,growx");
		chooseDB.addActionListener(new ActionListenerController(btnStatus.changeDB));
		
		btnSql = new JButton("SQL запрос");
		btnSql.setEnabled(false);
		panel.add(btnSql, "cell 2 2,growx");
		
		btnSql.addActionListener(new ActionListenerController(btnStatus.SqlQuery));
		
		statusDB = new JLabel("База не выбрана. При запуске будет создана новая БД");
		panel.add(statusDB, "cell 0 3 3 1");
		
		label_1 = new JLabel("Фильтр:");
		panel.add(label_1, "cell 0 4,growx");
		
		textArea = new JTextArea();
		panel.add(textArea, "cell 1 4 2 1,grow");
		
		
		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane, "cell 0 5 3 3,grow");
		
		list.setModel(model);
		scrollPane.setViewportView(list);
		
		list.addMouseListener(new ActionListenerController(btnStatus.getPacket));
		
	}

	
	private class FaildItemsOfListRenderer extends DefaultListCellRenderer {
		
	private static final long serialVersionUID = -8889521324387989571L;
	
	@Override
	   public Component getListCellRendererComponent(JList<?> list,
	            Object value, int index, boolean isSelected, boolean cellHasFocus) {
	         Component superRenderer = super.getListCellRendererComponent(list, value, index, isSelected,
	               cellHasFocus);
	         
	         setBackground(null);
	         setForeground(null);
	         if (value != null && ((Mil1553Packet) value).status == EMilPacketStatus.eFAILED)
	            setForeground(Color.RED);

	         return superRenderer;
	      }
	}

	class ActionListenerController extends MouseAdapter implements ActionListener{
		private btnStatus st;
		
		public ActionListenerController(btnStatus st)
		{
			this.st = st;
		}
		
		private void getPacket(){
			if(!list.isSelectionEmpty())
			{
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							MTPacketGUI window = new MTPacketGUI(list.getSelectedValue());
							window.frame.setVisible(true);
							
							
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		}
		
		@Override
		public void mouseClicked(MouseEvent e){
			switch(st)
			{
				case getPacket:
					this.getPacket();
					break;
				default:
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
			else
			{
				model.removeAllElements();
				setDevicePause(false);
			}
		}
		
		private void setDevicePause(boolean mode){
			try {
				device.setPause(mode);
			} catch (Eclus1553Exception ex) {
				ex.printStackTrace();
			}
		}
		
		private String changeDB(){ 	
			JFileChooser fch = new JFileChooser();
			fch.setCurrentDirectory(new File(dbFolder));
			fch.showOpenDialog(frame);
			currDBfile = fch.getSelectedFile();
			String filename = null;
			if(currDBfile != null)
			{
				filename = currDBfile.getName();
				filename = filename.substring(0, filename.length() - 3);
			}
			
			return filename;
		}
		
		private void setListByQuery(String query){
			model.removeAllElements();
			try
			{
				list.setModel(model.getListByQuery(query));
			}
			catch(java.lang.IllegalArgumentException e1)
			{
				System.out.println("java.lang.IllegalArgumentException: model must be non null");
			}
		}
		
		@Override
		public void actionPerformed(ActionEvent ev) {
			
			switch(st)
			{
				case mtStart: {
					int input = 0;
					
					if(currDBfile != null)
						input = JOptionPane.showConfirmDialog(frame, "Есть открытая база. Закрыть и продолжить?", null, JOptionPane.YES_NO_OPTION);
					
					if(currDBfile == null || input == 0)
					{
						btnStart.setEnabled(false);
						btnSql.setEnabled(false);
						btnStop.setEnabled(true);
						chooseDB.setEnabled(false);
						
						
						String filename = model.createNewDBConn();
						if(filename != "")
						{
							currDBfile = new File(filename);
							statusDB.setText("Создана БД: " + filename);
						}
						
						mtStart();
						list.setModel(model);
					}					
					break;
				}				
				case mtStop: {
					btnStart.setEnabled(true);
					btnStop.setEnabled(false);
					btnSql.setEnabled(true);
					chooseDB.setEnabled(true);
					
					setDevicePause(true);
					
					statusDB.setText("Остановка. БД для просмотра: " + currDBfile.getName());
					
					break;
				}					
				case SqlQuery: {
					setListByQuery(textArea.getText());
					break;
				}				
				case changeDB: {					
					String filename = changeDB();
					if(filename != null)
					{
						btnSql.setEnabled(true);
						statusDB.setText(filename + ".db (дата: " + (new Date(Long.valueOf(filename))) + ")");
					
						if(model.checkConn())
							model.closeConn();
						
						model.setConnection(filename + ".db");
						
						setListByQuery("");
					}
						
					break;
				}				
				default:
					break;					
			}			
		}				
	}		
}
