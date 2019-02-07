package ru.elcus.mil.guitest;


import java.io.BufferedReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.swing.DefaultListModel;
import javax.swing.ListModel;

import ru.elcus.mil.EMilPacketStatus;
import ru.elcus.mil.Mil1553Packet;
import ru.elcus.mil.structs.EBus;
import ru.elcus.mil.structs.EMilFormat;

public class MTListViewModel extends DefaultListModel<Mil1553Packet> {

	private static final long serialVersionUID = 4887147732114211340L;
	
	private static final String urlDB = "jdbc:sqlite:/home/bvv/java-elcus-mil/databases/monitor.db";
	private Connection conn;

	MTListViewModel()
	{
		conn = null;
		try {
            // create a connection to the database
            conn = DriverManager.getConnection(urlDB);
            
            System.out.println("Connection to SQLite has been established.");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
	}
	
	/*
	 * 
	 *  {[
	 *  	
	 *  ]}
	 * 
	 * */
	
	void insertElementAndAddToList(Mil1553Packet packet)
	{
		addElement(packet);
		
		String metasql = "INSERT INTO metadata "
				+ "(`CommandWord`, `AnswerWord`, `Date`, `Bus`, `Format`, `Status`, `DataWords`) "
				+ "VALUES "
				+ "(?, ?, ?, ?, ?, ?, ?)";
		
		try (PreparedStatement pstmt = conn.prepareStatement(metasql, Statement.RETURN_GENERATED_KEYS)) {
            
			pstmt.setShort(1, packet.commandWord);
			pstmt.setShort(2, packet.answerWord);
			pstmt.setString(3,  String.valueOf(packet.date));
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
	
	DefaultListModel<Mil1553Packet> getListByQuery(String sql)
	{
		try (Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql)){
            
        	DefaultListModel<Mil1553Packet> list = new DefaultListModel<Mil1553Packet>();
        	
        	
        	SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz YYYY", Locale.ENGLISH);
        	
        	// loop through the result set
            while (rs.next()) {
            	Mil1553Packet packet = new Mil1553Packet();
            	
            	packet.commandWord = rs.getShort("CommandWord");
            	packet.answerWord = rs.getShort("AnswerWord");
            	
            	try {
					packet.date = format.parse(rs.getString("Date"));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
            	System.out.println(packet.date);
            	
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
		try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
	}
	
}
