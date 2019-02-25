package ru.elcus.mil.guitest;


import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListModel;

import ru.elcus.mil.Mil1553Packet;
import ru.elcus.mil.Mil1553RawPacketMT;
import ru.elcus.mil.TimeManipulation;
import ru.elcus.mil.structs.EMilFormat;
import ru.elcus.mildecoders.IMil1553Decoder;

public class MTListViewModel extends DefaultListModel<Mil1553Packet> {

	private static final long serialVersionUID = 4887147732114211340L;	
	private String dbFolder;
	private static final String tablename = "metadata";	
	private Connection conn;
	Map<Integer,IMil1553Decoder> decoders = new HashMap<>();
	
	MTListViewModel(String dbFolder){
		this.dbFolder = "jdbc:sqlite:" + dbFolder;
	}
	
	public boolean checkConn(){
		return (conn != null) ? true : false;
	}
	
	public void setConnection(String dbname){
        try {
        	
            String url = dbFolder + dbname;
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            
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
		
		addElement(packet);
		
		String metasql = "INSERT INTO metadata "
				+ "(`CommandWord`, `AnswerWord`, `Date`, `DataWords`, `SW`) "
				+ "VALUES "
				+ "(?, ?, ?, ?, ?)";
		
		
		try (PreparedStatement pstmt = conn.prepareStatement(metasql)) {
            
			pstmt.setString(1, String.format("%04x", packet.commandWord));
			pstmt.setString(2, String.format("%04x",packet.answerWord));
			pstmt.setDouble(3, TimeManipulation.getUnixTimeUTC(packet.date));;
			pstmt.setString(5, String.valueOf(packet.sw));
		
			String DataWords = "";
			int len = packet.dataWords.length;
			for(int i = 0; i < len; i++)
			{
				DataWords += String.valueOf(packet.dataWords[i]);
				if(i < len - 1)
					DataWords += ",";
			}
			pstmt.setString(4, DataWords);
				
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
	}
	
	DefaultListModel<Mil1553Packet> getListByQuery(String where){
		String sql = "SELECT * FROM " + tablename;
		if(!where.isEmpty())
			sql += " WHERE " + where;
		
		try (Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql)) {
			
			DefaultListModel<Mil1553Packet> list = new DefaultListModel<Mil1553Packet>();
        	
			Mil1553Packet packet;

			
        	// loop through the result set
            while (rs.next()) {            	
            	short[] basedata = new short[64];            	
            	short cw = (short) Integer.parseInt(rs.getString("CommandWord"), 16);
            	short aw = Short.parseShort(rs.getString("AnswerWord"), 16);
            	EMilFormat format = Mil1553Packet.calcFormat(cw);                 	
            	short[] dw = new short[32];
            	
            	basedata[0] = cw;
            	
            	int i = 0;
            	for(String el : rs.getString("DataWords").split(","))
            		dw[i++] = Short.parseShort(el);
            	
            	i = Mil1553Packet.getWordsCount(cw);
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
        		 
            	list.addElement(packet);
            }
            
            return list;
            
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
