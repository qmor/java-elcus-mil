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
	
	private static final String dbFolder = "databases/";
	private String[] dbFiles;
	
	JComboBox<String> comboBox;
	
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
					MTTest window = new MTTest(args);
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
	MTListViewModel model = new MTListViewModel(dbFolder);
	
	public MTTest(String[] args) {
		getAllDataBases();
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
				
				comboBox.addItem(model.createNewDBConn());
				
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
	
	private void getAllDataBases()
	{
		File folder = new File(dbFolder);
		
        dbFiles = folder.list(new FilenameFilter() {
 
             @Override public boolean accept(File folder, String name) {
                 return name.endsWith(".db");
             }
            
         });
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
		comboBox = new JComboBox<String>();
		
		
		btnStart = new JButton("Запуск монитора");
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
		
		btnStop = new JButton("Остановка монитора");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				btnController(btnStatus.mtStop);
			}
		});
		
		btnStop.setFont(new Font("Dialog", Font.BOLD, 14));
		panel.add(btnStop, "cell 1 1");
		
		btnSql = new JButton("SQL запрос");
		panel.add(btnSql, "cell 0 2");
		
		
		comboBox.setToolTipText("");
		panel.add(comboBox, "cell 1 2,growx");
		
		final String not_selectable_option = "Выбрать БД";
		
		comboBox.setModel(new DefaultComboBoxModel<String>() {
		      /**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			boolean selectionAllowed = true;

		      @Override
		      public void setSelectedItem(Object anObject) {
		        if (!not_selectable_option.equals(anObject)) {
		          super.setSelectedItem(anObject);
		        } else if (selectionAllowed) {
		          // Allow this just once
		          selectionAllowed = false;
		          super.setSelectedItem(anObject);
		        }
		      }
	    });
		
		comboBox.addItem(not_selectable_option);
	    for(String f : dbFiles)
	    	comboBox.addItem(f);
		
	    comboBox.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e)
			{
				if(!comboBox.getSelectedItem().equals(not_selectable_option))
				{
					model.removeAllElements();
					list.setModel(model);
					model.closeConn();
					model.setConnection(comboBox.getSelectedItem().toString());
					list.setModel(model.getListByQuery(""));
				}
			}
	    });
	    
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
		});
		
	}

}
