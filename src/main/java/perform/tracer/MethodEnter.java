package perform.tracer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.zaxxer.hikari.HikariDataSource;

import perform.db.ConnectionPools;

public class MethodEnter {
	private int id;
	private long startTime;

	public MethodEnter(String hashCommit, String className, String methodName, int runId) {
		id = -1;
		HikariDataSource ds = ConnectionPools.getProcessing();
		
		int fileId = getFileID(ds, hashCommit, className);
		if(fileId != -1)
				id = addMethod(ds, fileId, runId, methodName);
		}

	private int addMethod(HikariDataSource ds, int fileId, int runId, String methodName) {
		int methodId = -1;
		String SQL_INSERT_METHOD = "insert into perform.methods (created_at, " + "updated_at, file_id, run_id, name)"
					+ " values (?, ?, ?, ?, ?)";
	
		java.sql.Timestamp startedAt = new java.sql.Timestamp(new java.util.Date().getTime());
		this.startTime = startedAt.getTime();
		
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
//		String SQL_FILE_ID = "SELECT f.id FROM perfrt.files AS f INNER JOIN perfrt.commits AS c ON f.commit_id = c.id WHERE c.commit_hash='"
		String SQL_FILE_ID = "SELECT f.id FROM files AS f INNER JOIN commits AS c ON f.commit_id = c.id WHERE c.commit_hash='"
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
	
	public long getStartTime() {
		return startTime;
	}
}