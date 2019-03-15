package ru.elcus.mil.guitest;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import ru.elcus.mil.Mil1553Packet;
import ru.elcus.mil.Mil1553RawPacketMT;
import ru.elcus.mil.TimeManipulation;
import ru.elcus.mil.structs.EMilFormat;
import ru.elcus.mildecoders.IMil1553Decoder;

public class MTListViewModel extends DefaultTableModel{

	
	final String metasql = "INSERT INTO metadata "
			+ "(`CommandWord`, `AnswerWord`, `Date`, `DataWords`, `SW`) "
			+ "VALUES "
			+ "(?, ?, ?, ?, ?)";
	
	private static final long serialVersionUID = 4887147732114211340L;	
	private String dbFolder;
	private static final String tablename = "metadata";	
	private Connection conn;
	Timer flushTimer = new Timer(true);
	
	Map<Integer,IMil1553Decoder> decoders = new HashMap<>();
	private String column_names[]= {"Packets"};
	
	MTListViewModel(String dbFolder){
		this.dbFolder = "jdbc:sqlite:" + dbFolder;
		
		
		flushTimer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				try
				{
					if (insertPrepareStatement!=null)
					{
						insertPrepareStatement.executeBatch();
					}
				}
				catch (Exception ex)
				{
					System.out.println(ex.getLocalizedMessage());
				}
				
			}
		}, 200, 100);		
	}
	
	@Override
	public int getColumnCount() {
		return 1;
	}	
	
    @Override 
    public String getColumnName(int index) { 
        return column_names[index]; 
    } 
    
	public boolean checkConn(){
		return (conn != null) ? true : false;
	}
	
	PreparedStatement insertPrepareStatement = null;
	
	public MTListViewModel(){}
	
	public void setConnection(String dbname){
        try {
        	
            String url = dbFolder + dbname;
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            insertPrepareStatement = conn.prepareStatement(metasql);
            System.out.println("Connection to SQLite has been established.");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 
	}
	
	public String createNewDBConn(){
		String name = String.valueOf(System.currentTimeMillis()) + ".db";
		String url = dbFolder + name;
		
        try {
        	conn = DriverManager.getConnection(url);
        	Statement stmt = conn.createStatement();
        	
        	if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
              
                String sql = "CREATE TABLE IF NOT EXISTS metadata (\n"
                        + "CommandWord,\n"
                        + "AnswerWord,\n"
                        + "Date,\n"
                        + "DataWords,\n"
                        + "SW"
                        + ");";
                
                stmt.execute(sql);
                insertPrepareStatement = conn.prepareStatement(metasql);
        	}
 
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return name;
	}
	
	public void closeConn(){
		try {
            if (conn != null)
                conn.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
	}
	
	public void addDecoder(IMil1553Decoder decoder){
		decoders.put(decoder.getDeviceRT(), decoder);
	}
	
	void insertElementAndAddToList(Mil1553Packet packet){
		if (decoders.containsKey(Mil1553Packet.getRtAddress(packet.commandWord)))
			decoders.get(Mil1553Packet.getRtAddress(packet.commandWord)).processPacket(packet);
		synchronized (this) {
			SwingUtilities.invokeLater(new Runnable() {
					
				@Override
				public void run() {
					addRow(new Object[]{packet});
				}
			});	
		}			

		try {	            
			insertPrepareStatement.setString(1, String.format("%04x", packet.commandWord));
			insertPrepareStatement.setString(2, String.format("%04x",packet.answerWord));
			insertPrepareStatement.setDouble(3, TimeManipulation.getUnixTimeUTC(packet.date));;
			insertPrepareStatement.setString(5, String.valueOf(packet.sw));
			insertPrepareStatement.setBytes(4, packet.dataWordsAsByteArray());
			insertPrepareStatement.addBatch();
	            
	    } catch (SQLException e) {
	        System.out.println(e.getMessage());
	    }		
	}
	
	DefaultTableModel getListByQuery(String where){
		String sql = "SELECT * FROM " + tablename;
		if(!where.isEmpty())
			sql += " WHERE " + where;
		
		try (Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql)) {			
        	
			Mil1553Packet packet;

			
        	// loop through the result set
            while (rs.next()) {            	
            	short[] basedata = new short[64];            	
            	short cw = (short) Integer.parseInt(rs.getString("CommandWord"), 16);
            	short aw = Short.parseShort(rs.getString("AnswerWord"), 16);
            	EMilFormat format = Mil1553Packet.calcFormat(cw);                 	
            	short[] dw = new short[32];
            	
            	basedata[0] = cw;
            	
            	ByteBuffer byteBuf = ByteBuffer.wrap(rs.getBytes("DataWords"));
            	byteBuf.order(ByteOrder.LITTLE_ENDIAN);
            	for (int i = 0; i < dw.length; i++) {										
            			dw[i] = byteBuf.getShort();
				}
            	
            	int i = Mil1553Packet.getWordsCount(cw);
    			if (i == 0) 
    				i = 32;
    			
            	switch (format) {
	        		case CC_FMT_1:
	        			System.arraycopy(dw, 0, basedata, 1, i);      			
	        			basedata[i+1] = aw;
	        			break;
	        		case CC_FMT_2:
	        			basedata[1] = aw;
	        			System.arraycopy(dw, 0, basedata, 2, i);
	        			break;
	        		case CC_FMT_4:
	        			basedata[1] = aw;
	        			break;
	        		case CC_FMT_5:
	        			basedata[1] = aw;
	        			basedata[2] = dw[0];
	        			break;
	        		case CC_FMT_6:
	        			basedata[2] = aw;
	        			basedata[1] = dw[0];
	        			break;
	        		default:
	        			break;
        		}
            	
            	packet = new Mil1553Packet(new Mil1553RawPacketMT(basedata,rs.getInt("SW"),0));
            	
        		if (decoders.containsKey(Mil1553Packet.getRtAddress(packet.commandWord)))
        			decoders.get(Mil1553Packet.getRtAddress(packet.commandWord)).processPacket(packet);
        		
        		packet.date = TimeManipulation.getDateTimeFromUnixUTC(rs.getDouble("Date"));
        		
        		this.addRow(new Object[]{packet});
            }           
            return this;  
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }	
		return null;
	}
	
	@Override
	protected void finalize(){
		closeConn();
	}
	
}
