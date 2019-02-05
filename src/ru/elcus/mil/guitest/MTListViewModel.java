package ru.elcus.mil.guitest;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
	
	void insertElementAndAddToList(Mil1553Packet packet)
	{
		addElement(packet);
		
		String metasql = "INSERT INTO metadata "
				+ "(`CommandWord`, `AnswerWord`, `Date`, `Bus`, `Format`, `Status`) "
				+ "VALUES "
				+ "(?, ?, ?, ?, ?, ?)";
		
		try (PreparedStatement pstmt = conn.prepareStatement(metasql, Statement.RETURN_GENERATED_KEYS)) {
            
			pstmt.setShort(1, packet.commandWord);
			pstmt.setShort(2, packet.answerWord);
			pstmt.setDate(3, (Date) packet.date);
			pstmt.setString(4, String.valueOf(packet.bus));
			pstmt.setString(5, String.valueOf(packet.format));
			pstmt.setString(6, String.valueOf(packet.status));
			
            pstmt.executeUpdate();
            
            String datawordsql = "INSERT INTO datawords (`id_metadata`, `DataWord`) VALUES(?, ?)";
            
            ResultSet rs = pstmt.getGeneratedKeys();
            
            try(PreparedStatement pstmt2 = conn.prepareStatement(datawordsql, Statement.RETURN_GENERATED_KEYS))
    		{
            	for(int i = 0; i < packet.dataWords.length; i++)
            	{
            		pstmt2.setInt(rs.getInt("id_metadata"), packet.dataWords[i]);
            		pstmt2.executeUpdate();
            	}
    		}
            catch(SQLException e)
            {
            	System.out.println(e.getMessage());
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
	}
	
	DefaultListModel<Mil1553Packet> getListByQuery(String sql)
	{
        
        try (Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            
        	DefaultListModel<Mil1553Packet> list = new DefaultListModel<Mil1553Packet>();
        	
        	Mil1553Packet packet = new Mil1553Packet();
        	
            // loop through the result set
            while (rs.next()) {
            	
            	packet.commandWord = rs.getShort("CommandWord");
            	packet.answerWord = rs.getShort("AnswerWord");
            	packet.date = rs.getDate("Date");
            	packet.bus = EBus.valueOf(rs.getString("Bus"));
            	packet.format = EMilFormat.valueOf(rs.getString("Format"));
            	packet.status = EMilPacketStatus.valueOf(rs.getString("Status"));
            	
            	/*System.out.println(rs.getInt("id") +  "\t" + 
                                   rs.getString("name") + "\t" +
                                   rs.getDouble("capacity"));*/
            }
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
