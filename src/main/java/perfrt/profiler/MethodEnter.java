package perfrt.profiler;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.zaxxer.hikari.HikariDataSource;

import perfrt.db.ConnectionPools;

public class MethodEnter {
	private int id;

	public MethodEnter(String hashCommit, String className, String methodName, int runId) {
		id = -1;
		HikariDataSource ds = ConnectionPools.getProcessing();
		
		int fileId = getFileID(ds, hashCommit, className);
		if(fileId != -1)
				id = addMethod(ds, fileId, runId, methodName);
		
//		con = ds.getConnection();
//		
//		st = con.createStatement();
//		String query = "SELECT f.id FROM perfrt.files AS f INNER JOIN perfrt.commits AS c ON f.commit_id = c.id WHERE c.commit_hash='"
//				+ hashCommit + "' AND f.name LIKE '%" + className + "';";
//		rs = st.executeQuery(query);

//		if (rs.next()) {
//			int fileId = rs.getInt("id");
//			java.sql.Timestamp startedAt = new java.sql.Timestamp(new java.util.Date().getTime());
//			String query1 = "insert into perfrt.methods (created_at, " + "updated_at, file_id, run_id, name)"
//					+ " values (?, ?, ?, ?, ?)";

//			PreparedStatement preparedStmt = con.prepareStatement(query1, Statement.RETURN_GENERATED_KEYS);
//			preparedStmt.setTimestamp(1, startedAt);
//			preparedStmt.setTimestamp(2, startedAt);
//			preparedStmt.setInt(3, fileId);
//			preparedStmt.setInt(4, runId);
//			preparedStmt.setString(5, methodName);
//
//			preparedStmt.executeUpdate();
//			rs2 = preparedStmt.getGeneratedKeys();
//			if (rs2.next()) {
//				id = rs2.getInt(1);
//			}
//			rs.close();
//			rs2.close();
//			st.close();
//				con.close();
		}

	private int addMethod(HikariDataSource ds, int fileId, int runId, String methodName) {
		int methodId = -1;
		String SQL_INSERT_METHOD = "insert into perfrt.methods (created_at, " + "updated_at, file_id, run_id, name)"
					+ " values (?, ?, ?, ?, ?)";
	
		java.sql.Timestamp startedAt = new java.sql.Timestamp(new java.util.Date().getTime());
		
		
		try (Connection connection = ds.getConnection();
	         PreparedStatement ps = connection.prepareStatement(SQL_INSERT_METHOD, Statement.RETURN_GENERATED_KEYS)) {				 
				ps.setTimestamp(1, startedAt);
				ps.setTimestamp(2, startedAt);
				ps.setInt(3, fileId);
				ps.setInt(4, runId);
				ps.setString(5, methodName);	
				
				ps.executeUpdate();
				
				ResultSet rs = ps.getGeneratedKeys();
				if(rs.next())
					methodId = rs.getInt(1);
        } catch (SQLException e) {
        	System.out.println("SQL EXCEPTION - [INSERT METHOD] [FILE_ID] : "+ fileId + ", [RUN_ID] : " + runId + ", [METHOD] : " + methodName);
            e.printStackTrace();;
        }
		return methodId;
	}

	private int getFileID(HikariDataSource ds, String hashCommit, String className) {
		int fileId = -1;
		String SQL_FILE_ID = "SELECT f.id FROM perfrt.files AS f INNER JOIN perfrt.commits AS c ON f.commit_id = c.id WHERE c.commit_hash='"
				+ hashCommit + "' AND f.name LIKE '%" + className + "';";
		try (Connection connection = ds.getConnection();
	            PreparedStatement ps = connection.prepareStatement(SQL_FILE_ID);
				ResultSet rs = ps.executeQuery();) {
				 
					if (rs.next()) 
						fileId = rs.getInt("id");
						
        } catch (SQLException e) {
        	System.out.println("SQL EXCEPTION - [SELECT] [COMMIT] : " + hashCommit + ", [CLASSNAME] : " + className);
            e.printStackTrace();;
        }
		return fileId;
	}
	

	public int getId() {
		return id;
	}
}