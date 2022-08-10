import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MethodEnter {
	private int id;
	private String hashCommit, className, methodName;
	private int idRun;

	public MethodEnter(String hashCommit, String className, String methodName, int idRun) {
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		ResultSet rs2 = null;

		id = -1;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/perfrt", "root", "password");

			st = con.createStatement();
			String query = "SELECT f.* FROM perfrt.files AS f INNER JOIN perfrt.commits AS c ON f.commit_id = c.id WHERE c.commit_hash='"
					+ hashCommit + "' AND f.name LIKE '%" + className + "';";
			rs = st.executeQuery(query);

			if (rs.next()) {
				int idFile = rs.getInt("id");
				java.sql.Timestamp startedAt = new java.sql.Timestamp(new java.util.Date().getTime());
				String query1 = "insert into perfrt.methods (created_at, " + "updated_at, file_id, run_id, name)"
						+ " values (?, ?, ?, ?, ?)";

				PreparedStatement preparedStmt = con.prepareStatement(query1, Statement.RETURN_GENERATED_KEYS);
				preparedStmt.setTimestamp(1, startedAt);
				preparedStmt.setTimestamp(2, startedAt);
				preparedStmt.setInt(3, idFile);
				preparedStmt.setInt(4, idRun);
				preparedStmt.setString(5, methodName);

				preparedStmt.executeUpdate();
				rs2 = preparedStmt.getGeneratedKeys();
				if (rs2.next()) {
					id = rs2.getInt(1);
				}
				rs.close();
				rs2.close();
				st.close();
				con.close();
			}

		} catch (Exception e) {
			if (con != null) {
				if (st != null) {
					if (rs != null) {
						try {
							rs.close();
						} catch (SQLException e1) {
							System.out.println("Error closing resultset: " + e1.getMessage());
							e1.printStackTrace();
						}
					}
					if (rs2 != null) {
						try {
							rs2.close();
						} catch (SQLException e1) {
							System.out.println("Error closing resultset2: " + e1.getMessage());
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
			System.out.println("saveOnEnter: Got an exception!");
			System.out.println(e.getMessage());
			System.err.println("saveOnEnter: Got an exception!");
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public int getId() {
		return id;
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

	public int getIdRun() {
		return idRun;
	}

	public void setIdRun(int idRun) {
		this.idRun = idRun;
	}
}