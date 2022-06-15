import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MethodExit{ // implements Runnable {

	private String hashCommit, className, methodName, retValue;
	private long duration;
	private int idRun, idMethod;
	
//	public static void saveOnExit(String hashCommit, String className, String methodName, long duration, int idRun, int idMethod, String retValue) {
//	@Override
//    public void run() {
	public MethodExit(String hashCommit, String className, String methodName, long duration, int idRun, int idMethod, String retValue) {
//		System.out.println("saveOnExit");
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		long ownDuration = duration;
		
		try{
			Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/perfrt","root","password");
			st = con.createStatement();	
			
			// calculate own_duration
			String ownDurationSql = "SELECT SUM(cumulative_duration) AS sum FROM perfrt.methods WHERE caller_id="+ idMethod +";";
			ResultSet rsOwnDuration = st.executeQuery(ownDurationSql);
			if(rsOwnDuration.next()) {
				long sumCumulativeDuration = rsOwnDuration.getLong("sum");
				ownDuration -= sumCumulativeDuration;				
			}
			rsOwnDuration.close();
			
//			System.out.println("select methods");
			String queryCallerId = "SELECT * from perfrt.methods AS m INNER JOIN perfrt.files AS f ON m.file_id=f.id INNER JOIN perfrt.commits AS c ON f.commit_id=c.id WHERE m.id < " + idMethod + " AND m.caller_id IS NULL AND c.commit_hash='" + hashCommit + "' AND m.finished=false ORDER BY m.id DESC LIMIT 1;";
//			System.out.println(queryCallerId);
			rs = st.executeQuery(queryCallerId);
			  
			if( rs.next()) {
				int idCaller = rs.getInt("id");
				String queryUpdate = "UPDATE perfrt.methods SET caller_id=?, own_duration=?, cumulative_duration=?, finished=?, return_value=? WHERE id=?";
//				System.out.println("if: " + queryUpdate);
				PreparedStatement preparedStmt = con.prepareStatement(queryUpdate);
						      preparedStmt.setInt(1, idCaller);
						      preparedStmt.setLong(2, ownDuration);
						      preparedStmt.setLong(3, duration);
						      preparedStmt.setBoolean(4, true);
						      preparedStmt.setString(5,  retValue);
						      preparedStmt.setInt(6, idMethod);
	
				preparedStmt.executeUpdate();
				preparedStmt.close();
			}else {		
				String queryUpdate = "UPDATE perfrt.methods SET own_duration=?, cumulative_duration=?, finished=?, return_value=? WHERE id=?";
//				System.out.println("else: " + queryUpdate);
				PreparedStatement preparedStmt = con.prepareStatement(queryUpdate);
						      preparedStmt.setLong(1, ownDuration);
						      preparedStmt.setLong(2, duration);
						      preparedStmt.setBoolean(3, true);
						      preparedStmt.setString(4,  retValue);
						      preparedStmt.setInt(5, idMethod);
	
				preparedStmt.executeUpdate();
				preparedStmt.close();
			}
			rs.close();
			st.close();
			con.close();
		}
		catch (Exception e){
			System.err.println("saveOnExit: Got an exception!");
			System.err.println(e.getMessage());		
			e.printStackTrace();
			if(con != null) {
				try {
					con.close();
				} catch (SQLException e1) {
					System.err.println("cannot close database connection!");
					System.err.println(e1.getMessage());
				}
			}
			if( con != null) {
				if(st != null) {
					if(rs != null) {
						try {
							rs.close();
						} catch (SQLException e1) {
							System.out.println("Error closing resultset: " + e1.getMessage());
							e1.printStackTrace();
						}
					}
					try {
						st.close();
					} catch (SQLException e1) {
						System.out.println("Error closing statement: " + e1.getMessage());
						e1.printStackTrace();
					}
				}
				try {
					con.close();
				} catch (SQLException e1) {
					System.out.println("Error closing mysql connection: " + e1.getMessage());
					e1.printStackTrace();
				}
			}
		}	
	}

	public String getHashCommit() {
		return hashCommit;
	}

	public void setHashCommit(String hashCommit) {
		this.hashCommit = hashCommit;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getRetValue() {
		return retValue;
	}

	public void setRetValue(String retValue) {
		this.retValue = retValue;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public int getIdRun() {
		return idRun;
	}

	public void setIdRun(int idRun) {
		this.idRun = idRun;
	}
}
