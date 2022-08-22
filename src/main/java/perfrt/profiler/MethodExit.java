package perfrt.profiler;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.zaxxer.hikari.HikariDataSource;

import perfrt.db.ConnectionPools;

public class MethodExit {

	public MethodExit(String hashCommit, String className, String methodName, long endedAt, long duration, int methodId, String retValue) {
		long ownDuration = duration;

			HikariDataSource ds = ConnectionPools.getProcessing();

			ownDuration = calcOwnDuration(ds, methodId, ownDuration);

			int callerId = getCallerId(ds, hashCommit, methodId);
			updateMethod(ds, methodId, callerId, endedAt, duration, retValue, ownDuration);
	}

	private void updateMethod(HikariDataSource ds, int methodId, int callerId, long endedAt, long duration, String retValue, long ownDuration) {
		if(callerId != -1) {
			//first method
			String SQL_UPDATE_METHOD = "UPDATE perfrt.methods SET caller_id=?, ended_at=?, own_duration=?, cumulative_duration=?, finished=?, return_value=? WHERE id=?";
			try (Connection connection = ds.getConnection();
			         PreparedStatement ps = connection.prepareStatement(SQL_UPDATE_METHOD)) {				 
						ps.setInt(1, callerId);
						ps.setTimestamp(2, new java.sql.Timestamp(endedAt));
						ps.setLong(3, ownDuration);
						ps.setLong(4, duration);
						ps.setBoolean(5, true);
						ps.setString(6, retValue);
						ps.setInt(7, methodId);
						
						ps.executeUpdate();
						
		        } catch (SQLException e) {
		        	System.out.println("SQL EXCEPTION - [UPDATE METHOD] [METHOD_ID] : "+ methodId);
		            e.printStackTrace();;
		        }
			
		} else {
			//not first method
			
			String SQL_UPDATE_METHOD = "UPDATE perfrt.methods SET ended_at=?, own_duration=?, cumulative_duration=?, finished=?, return_value=? WHERE id=?";
			try (Connection connection = ds.getConnection();
			         PreparedStatement ps = connection.prepareStatement(SQL_UPDATE_METHOD)) {				 
						ps.setTimestamp(1, new java.sql.Timestamp(endedAt));
						ps.setLong(2, ownDuration);
						ps.setLong(3, duration);
						ps.setBoolean(4, true);
						ps.setString(5, retValue);
						ps.setInt(6, methodId);
						
						ps.executeUpdate();
						
		        } catch (SQLException e) {
		        	System.out.println("SQL EXCEPTION - [UPDATE METHOD] [METHOD_ID] : "+ methodId);
		            e.printStackTrace();;
		        }
			
		}
	}

	private int getCallerId(HikariDataSource ds, String hashCommit, int methodId) {
		int callerId = -1;
		String SQL_CALLER_ID = "SELECT * from perfrt.methods AS m INNER JOIN perfrt.files AS f ON m.file_id=f.id INNER JOIN perfrt.commits AS c ON f.commit_id=c.id WHERE m.id < "
				+ methodId + " AND m.caller_id IS NULL AND c.commit_hash='" + hashCommit
				+ "' AND m.finished=false ORDER BY m.id DESC LIMIT 1;";
		
		try (Connection connection = ds.getConnection();
	            PreparedStatement ps = connection.prepareStatement(SQL_CALLER_ID);
				ResultSet rs = ps.executeQuery();) {
				 
					if (rs.next()) {
						callerId = rs.getInt("id");
					}
						
        } catch (SQLException e) {
        	System.out.println("SQL EXCEPTION - [calcOwnDuration] [METHOD_ID] : " + methodId);
            e.printStackTrace();
        }
		return callerId;
	}

	private long calcOwnDuration(HikariDataSource ds, int methodId, long ownDuration) {		
//		long cumulativeDuration;
		String SQL_DURATION = "SELECT SUM(cumulative_duration) AS sum FROM perfrt.methods WHERE caller_id=" + methodId + ";";
		try (Connection connection = ds.getConnection();
	            PreparedStatement ps = connection.prepareStatement(SQL_DURATION);
				ResultSet rs = ps.executeQuery();) {
				 
					if (rs.next()) {
						long sumCumulativeDuration = rs.getLong("sum");
						ownDuration -= sumCumulativeDuration;
					}
						
        } catch (SQLException e) {
        	System.out.println("SQL EXCEPTION - [calcOwnDuration] [METHOD_ID] : " + methodId);
            e.printStackTrace();
        }
		return ownDuration;
		
//		// calculate own_duration
//		String ownDurationSql = "SELECT SUM(cumulative_duration) AS sum FROM perfrt.methods WHERE caller_id="
//				+ methodId + ";";
//		ResultSet rsOwnDuration = st.executeQuery(ownDurationSql);
//		if (rsOwnDuration.next()) {
//			long sumCumulativeDuration = rsOwnDuration.getLong("sum");
//			ownDuration -= sumCumulativeDuration;
//		}
//		rsOwnDuration.close();
//		return ownDuration;
	}
}
