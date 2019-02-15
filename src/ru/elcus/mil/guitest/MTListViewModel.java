package ru.elcus.mil.guitest;


import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListModel;

import ru.elcus.mil.EMilPacketStatus;
import ru.elcus.mil.Mil1553Packet;
import ru.elcus.mil.structs.EBus;
import ru.elcus.mil.structs.EMilFormat;
import ru.elcus.mildecoders.IMil1553Decoder;

public class MTListViewModel extends DefaultListModel<Mil1553Packet> {

	private static final long serialVersionUID = 4887147732114211340L;
	
	private String dbFolder;
	private static final String tablename = "metadata";
	
	private Connection conn;
	Map<Integer,IMil1553Decoder> decoders = new HashMap<>();
	
	MTListViewModel(String dbFolder)
	{
		this.dbFolder = "jdbc:sqlite:" + dbFolder;
	}
	
	public boolean checkConn()
	{
		return (conn != null) ? true : false;
	}
	
	public void setConnection(String dbname)
	{
        try {
            // db parameters
            String url = dbFolder + dbname;
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            
            System.out.println("Connection to SQLite has been established.");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 
	}
	
	public String createNewDBConn()
	{
		String name = String.valueOf(System.currentTimeMillis()) + ".db";
		String url = dbFolder + name;
		
        try (Connection cn = DriverManager.getConnection(url);
        		Statement stmt = cn.createStatement()) {
        	conn = cn;
        	if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
              
                String sql = "CREATE TABLE IF NOT EXISTS metadata (\n"
                        + "CommandWord,\n"
                        + "AnswerWord,\n"
                        + "Date,\n"
                        + "Bus,\n"
                        + "Format,\n"
                        + "Status,\n"
                        + "DataWords\n"
                        + ");";
                
                stmt.execute(sql);
        	}
 
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return name;
	}
	
	public void closeConn()
	{
		try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
	}
	
	public void addDecoder(IMil1553Decoder decoder)
	{
		decoders.put(decoder.getDeviceRT(), decoder);
	}
	
	void insertElementAndAddToList(Mil1553Packet packet)
	{
		if (decoders.containsKey(Mil1553Packet.getRtAddress(packet.commandWord)))
		{
			decoders.get(Mil1553Packet.getRtAddress(packet.commandWord)).processPacket(packet);
		}
		addElement(packet);
		
		String metasql = "INSERT INTO metadata "
				+ "(`CommandWord`, `AnswerWord`, `Date`, `Bus`, `Format`, `Status`, `DataWords`) "
				+ "VALUES "
				+ "(?, ?, ?, ?, ?, ?, ?)";
		
		try (PreparedStatement pstmt = conn.prepareStatement(metasql, Statement.RETURN_GENERATED_KEYS)) {
            
			pstmt.setString(1, String.format("%04x", packet.commandWord));
			pstmt.setString(2, String.format("%04x",packet.answerWord));
			pstmt.setLong(3, packet.date.getTime());;
			pstmt.setString(4, String.valueOf(packet.bus));
			pstmt.setString(5, String.valueOf(packet.format));
			pstmt.setString(6, String.valueOf(packet.status));
		
			String DataWords = "";
			int len = packet.dataWords.length;
			for(int i = 0; i < len; i++)
			{
				DataWords += String.valueOf(packet.dataWords[i]);
				if(i < len - 1)
					DataWords += ",";
			}
			pstmt.setString(7, DataWords);
				
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
	}
	
	DefaultListModel<Mil1553Packet> getListByQuery(String where)
	{
		String sql = "SELECT * FROM " + tablename;
		if(!where.isEmpty())
			sql += " WHERE " + where;
		try (Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql)){
            
			DefaultListModel<Mil1553Packet> list = new DefaultListModel<Mil1553Packet>();
        	
			
			
			Mil1553Packet packet;
			Date d;
			
        	// loop through the result set
            while (rs.next()) {
            	packet = new Mil1553Packet();
            	packet.commandWord = (short) Integer.parseInt(rs.getString("CommandWord"), 16);
            	packet.answerWord = Short.parseShort(rs.getString("AnswerWord"), 16);
            	
            	d = new Date(rs.getLong("Date"));
            	packet.date = d;
            	
            	packet.bus = EBus.valueOf(rs.getString("Bus"));
            	packet.format = EMilFormat.valueOf(rs.getString("Format"));
            	packet.status = EMilPacketStatus.valueOf(rs.getString("Status"));
            	
            	int i = 0;
            	for(String el : rs.getString("DataWords").split(","))
            		packet.dataWords[i++] = Short.parseShort(el);
            	
            	list.addElement(packet);
            }
            
            return list;
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
		
		return null;
	}
	
	
	@Override
	protected void finalize()
	{
		closeConn();
	}
	
}
