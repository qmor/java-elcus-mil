package ru.elcus.mil.guitest;

import java.awt.Adjustable;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
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
import javax.swing.JScrollBar;
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
import ru.elcus.mil.TimeManipulation;
import ru.elcus.mildecoders.ClassByNameHelper;
import ru.elcus.mildecoders.IMil1553Decoder;


public class MTTest {

	private JFrame frame;
	private JButton btnStart, btnStop, btnSql;
	private JSpinner spinner;
	private JList<Mil1553Packet> list;
	private JTextArea textArea;
	private JScrollBar verticalBar;
	private AdjustmentListener adjlistener;
	
	private static final String dbFolder = "databases/";
	
	private Elcus1553Device device;
	MTListViewModel model = new MTListViewModel(dbFolder);
	
	private JButton chooseDB;
	private JLabel statusDB;
	private File currDBfile;
	private JLabel label_1;
	private JButton htmlbtn;
	private JButton binbutton;
	
	enum btnStatus{
		mtStart,
		mtStop,
		SqlQuery,
		getPacket,
		changeDB,
		gethtmlfile,
		getbinfile
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
		frame.setBounds(100, 100, 694, 695);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new MigLayout("", "[][grow][grow]", "[][][][][22.00][][grow][415.00,grow][36.00][35.00]"));
		
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
		
		htmlbtn = new JButton("Выгрузить в html");
		htmlbtn.addActionListener(new ActionListenerController(btnStatus.gethtmlfile));
		htmlbtn.setEnabled(false);
		
		verticalBar = scrollPane.getVerticalScrollBar();
		
		verticalBar.addMouseListener(new MouseAdapter(){
			
			@Override
			public void mousePressed(MouseEvent e) {
				verticalBar.setValue(verticalBar.getValue());
				verticalBar.removeAdjustmentListener(adjlistener);
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if(btnStop.isEnabled())
					verticalBar.addAdjustmentListener(adjlistener);
			}
			
		});
		
		panel.add(htmlbtn, "cell 0 8 3 1,grow");
		
		binbutton = new JButton("Создать bin файл");
		panel.add(binbutton, "cell 0 9 3 1,grow");
		binbutton.setEnabled(false);
		binbutton.addActionListener(new ActionListenerController(btnStatus.getbinfile));
		
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

class ActionListenerController extends MouseAdapter implements ActionListener {
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
		
		private String getHTMLpacket(Mil1553Packet packet, int num) {
			StringBuilder sb = new StringBuilder();
			
			sb.append("<div class='wrap'><h3>Пакет #" + num + "</h3>");
			sb.append("<div class='metainfo'><ul>");
			sb.append(String.format("<li><b>Командное слово:</b> %04Xh</li>", packet.commandWord));
			sb.append(String.format("<li><b>Ответное слово:</b> %04Xh</li>", packet.answerWord));
			sb.append("<li><b>Формат:</b> " + packet.format + "</li>");
			sb.append("<li><b>Шина:</b> " + packet.bus + "</li>");
			sb.append("<li><b>Дата:</b> " + TimeManipulation.ToLongTimeStringMillis(packet.date) + "</li>");
			sb.append("<li><b>Описание:</b> " + packet.shortDescr + "</li>");
			sb.append("<li><b>Статус:</b> " + packet.status + "</li></ul></div>");
			
			sb.append("<div class='datawords'><ul><li><b><i>Массив слов данных</i></b></li>");
			for(int i = 0; i < packet.dataWords.length; i++)
				sb.append(String.format("<li><b>#%d:</b> %04Xh</li>", i, packet.dataWords[i]));
			
			sb.append("</ul></div></div>");
			
			return sb.toString();
		}
		
		private void getHTMLfile()
		{
			int listSize = list.getModel().getSize();
			
			if(listSize > 0)
			{
				
				String FILENAME = "./htmldump/" + String.valueOf(System.currentTimeMillis()) + ".html";
				File fl = new File(FILENAME);
				
				try {
					fl.createNewFile();
					
					try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(FILENAME), "UTF8"))) 
					{
						bw.write("<!DOCTYPE html><head><meta charset='UTF-8' /><title>" + statusDB.getText() + "</title><style>ul{list-style:none;} .wrap{ max-width:300px;margin:0 auto;border:1px solid black;} .wrap h3{text-align:center;border-bottom:1px solid black;padding-bottom:15px;}</style></head><body><h1 align='center'>" + statusDB.getText() + "</h1>");
						
							for(int i = 0; i < listSize; i++)
								bw.write(getHTMLpacket(list.getModel().getElementAt(i), i));
							
						bw.write("</body></html>");
							
						bw.flush();
						bw.close();
					}
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			}
		}
		
		
		
		private void getBinFile()
		{
			int listSize = list.getModel().getSize();
			
			if(listSize > 0)
			{
				
				String FILENAME = "./bindumps/" + String.valueOf(System.currentTimeMillis()) + ".bin";
				File fl = new File(FILENAME);
				
				try {
					fl.createNewFile();
					
					// String text = "Hello world!";
			        try(FileOutputStream fos=new FileOutputStream(fl))
			        {
			            // byte[] buffer = text.getBytes();
			            
			        	// list.getModel().getElementAt(i);
			        	
			        	for(int i = 0; i < listSize; i++)
			        	{
			        		Mil1553Packet packet = list.getModel().getElementAt(i);
			        		int dwsize = Mil1553Packet.getWordsCount(packet.commandWord);
			        		
			        		for(int j = 0; j < dwsize; j++)
			        		{
			        			fos.write((byte) ((packet.dataWords[j] >> 8) & 0xff));
			        			fos.write((byte) (packet.dataWords[j] & 0xff));
			        		}
			        	}
			        
			        }
			        
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
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
						htmlbtn.setEnabled(false);
						binbutton.setEnabled(false);
						
						String filename = model.createNewDBConn();
						if(filename != "")
						{
							currDBfile = new File(filename);
							statusDB.setText("Создана БД: " + filename);
						}
						
						mtStart();
						list.setModel(model);
					}
					
					adjlistener = new AdjustmentListener() {
						@Override
			            public void adjustmentValueChanged(AdjustmentEvent e) {
			                verticalBar.setValue(verticalBar.getMaximum());
			            }
					};
					
					verticalBar.addAdjustmentListener(adjlistener);
					
					break;
				}				
				case mtStop: {
					btnStart.setEnabled(true);
					btnStop.setEnabled(false);
					btnSql.setEnabled(true);
					chooseDB.setEnabled(true);
					htmlbtn.setEnabled(true);
					binbutton.setEnabled(true);
					
					setDevicePause(true);
					
					statusDB.setText("Остановка. БД для просмотра: " + currDBfile.getName());
					
					verticalBar.removeAdjustmentListener(adjlistener);
					
					break;
				}					
				case SqlQuery: {
					setListByQuery(textArea.getText());
					break;
				}
				case gethtmlfile: {
					getHTMLfile();
					break;
				}
				case getbinfile: {
					getBinFile();
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
					
					if(list.getModel().getSize() == 0)
					{
						htmlbtn.setEnabled(false);
						binbutton.setEnabled(false);
					}
					else
					{
						htmlbtn.setEnabled(true);
						binbutton.setEnabled(true);
					}
						
					break;
				}				
				default:
					break;					
			}			
		}				
	}		
}
