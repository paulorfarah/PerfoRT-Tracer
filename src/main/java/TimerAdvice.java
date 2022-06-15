

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.bytebuddy.asm.Advice;

	
public class TimerAdvice {   
	    
	@Advice.OnMethodEnter
	public static OnMethodEnterReturn enter(@Advice.Origin String method, @AgentArguments String agentArguments) {
//		String agentArguments = "org.apache.bcel.,29c17da7c24168113063cffae1a7f974225f2d0f,59";
		String[] agentArgs = agentArguments.trim().split("\\s*,\\s*");
		
		OnMethodEnterReturn onEnterValues = new OnMethodEnterReturn();
		onEnterValues.setPackageName(agentArgs[0]);
		onEnterValues.setCommitHash(agentArgs[1]);
		onEnterValues.setIdRun(Integer.parseInt(agentArgs[2]));
		onEnterValues.setStartTime(System.currentTimeMillis());
		String className = parseClassName(method);
		System.out.println("onEnter - Class: " + className + " method: " + method);
//		System.out.println("onEnter: " + " method: " + method);
//		int idMethod = saveOnEnter(onEnterValues.getCommitHash(), className, method, onEnterValues.getIdRun());
		
		MethodEnter m = new MethodEnter(onEnterValues.getCommitHash(), className, method, onEnterValues.getIdRun());

//		MethodEnter m = new MethodEnter();
//		m.setClassName(className);
//		m.setHashCommit(onEnterValues.getCommitHash());
//		m.setIdRun(onEnterValues.getIdRun());
//		m.setMethodName(method);
//		Thread thread = new Thread(m);
//		thread.start();
//		try {
//			thread.join();
//		} catch (InterruptedException e) {
//			System.out.println("Error saving method: " + e.getMessage());
//			e.printStackTrace();
//		}
		int idMethod = m.getId();
		onEnterValues.setIdMethod(idMethod);
		
//		read stack
//		var walker =  StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
//		walker.forEach(SF_CONSUMER);
		return onEnterValues;
	}
	
	@Advice.OnMethodExit(onThrowable = Throwable.class)
	public static void exit(@Advice.Origin String method
	        , @Advice.Enter OnMethodEnterReturn onEnterValues 	
	){
		String packName = onEnterValues.getPackageName(); // "com.github.paulorfarah.mavenproject.";
		String className = parseClassName(method).replace(packName, "") + ".java";
		long start = onEnterValues.getStartTime();
		long end =  System.currentTimeMillis();
		long duration = end - start; 
//		System.out.println(returned);
//		String returnedType = returned.getClass().toString();
		String returnedType = "teste";
		System.out.println("onExit - Class: " + className + " method: " + method + " start: " + start + " end: " + end + " duration: " + duration + "retValue:" );
//		System.out.println("onExit - method: " + method + " end: " + end);
//		System.out.println(thrown);
				
		MethodExit m = new MethodExit(onEnterValues.getCommitHash(), className, method, duration, onEnterValues.getIdRun(), onEnterValues.getIdMethod(), returnedType);

//		MethodExit m = new MethodExit();
//		m.setClassName(className);
//		m.setDuration(duration);
//		m.setHashCommit(onEnterValues.getCommitHash());
//		m.setIdRun(onEnterValues.getIdRun());
//		m.setMethodName(method);
//		m.setRetValue("teste");
//		
//		Thread thread = new Thread(m);
//		thread.start();
//		try {
//			thread.join();
//		} catch (InterruptedException e) {
//			System.out.println("Error ending method: " + e.getMessage());
//			e.printStackTrace();
//		}

		//		saveOnExit(onEnterValues.getCommitHash(), className, method, duration, onEnterValues.getIdRun(), onEnterValues.getIdMethod(), "teste");
		//		String hash = onEnterValues.getCommitHash(); // "cc2ed3975de05d3a6f9616807b44f974425e0e74";
//		int idRun = onEnterValues.getIdRun();
//		List<StackFrame> stack = onEnterValues.getStack();
//		for (StackFrame stackFrame : stack) {
//			System.out.println(stackFrame.getClassName() + " " + stackFrame.getMethodName() + " " + stackFrame.getFileName());
//		}
//			save(hash, className, origin, duration, idRun, stack);
//		    try {
//		    	FileWriter myWriter = new FileWriter("/home/usuario/perfrt/" + hash+"-"+className+".txt");
//				myWriter.write("Class: " + className + " method: " + origin + " start: " + start + " end: " + end + " duration: " + (end - start));
//				for (StackFrame stackFrame : stack) {
//					stackFrame.
//					myWriter.write(stackFrame.getClassName() + " " + stackFrame.getMethodName() + " " + stackFrame.getFileName());
//				}
//				myWriter.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
	}
	
	public static String parseClassName(String method) {
//		System.out.println("parseClassName");
		String className = null;
		Pattern p = Pattern.compile("[^\\s]*\\(");
		Matcher m = p.matcher(method);
		if (m.find()) {
		    String methodName = (String)m.group(0).toString();
//		    System.out.println(methodName); //com.github.shehanperera.example.Method.method4(
		    
		    String[] methodAux = methodName.split("\\.");
		    if(methodAux.length > 0) {
				className = methodAux[0];
				for(int i = 1; i < methodAux.length-1; i++) {
					className += "." + methodAux[i];
				}
//				return className;
		    }else {
		    	className = methodName;
		    }
		}
		String last = className.substring(className.lastIndexOf('.') + 1);
		className = last + ".java";
		return className;
	}



//	public static int saveOnEnter(String hashCommit, String className, String methodName, int idRun) {
//		System.out.println("saveOnEnter");
//		int id = -1;
//		try{
//			System.out.println("conn");
//			Connection conn = DBCPDataSource.getConnection();
//			System.out.println("stmt");
//			Statement st = conn.createStatement();			  
//			String query = "SELECT f.* FROM perfrt.files AS f INNER JOIN perfrt.commits AS c ON f.commit_id = c.id WHERE c.commit_hash='"+hashCommit + "' AND f.name LIKE '%"+className+"';";
//			System.out.println("query");
//			ResultSet rs = st.executeQuery(query);
//			System.out.println("executed query");
//			  
//			if( rs.next()) {
//				int idFile = rs.getInt("id");	
//				java.sql.Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
//				String query1 = "insert into perfrt.methods (created_at, "
//						+ "updated_at, file_id, run_id, name)"
//						+ " values (?, ?, ?, ?, ?)";
//	
//				PreparedStatement preparedStmt = conn.prepareStatement(query1, Statement.RETURN_GENERATED_KEYS);
//						      preparedStmt.setTimestamp(1, date);
//						      preparedStmt.setTimestamp(2, date);
//						      preparedStmt.setInt(3, idFile);
//						      preparedStmt.setInt(4, idRun);
//						      preparedStmt.setString(5, methodName);
//	
//				preparedStmt.executeUpdate();
//				ResultSet rs2 = preparedStmt.getGeneratedKeys();
//				
//                if(rs2.next())
//                {
//                    id = rs2.getInt(1);
//                    System.out.println("New idMethod: "+ id);
//                }
//				conn.close();
//			}
//			st.close();
//		}
//		catch (Exception e)
//		{
//			System.err.println("saveOnEnter: Got an exception!");
//			System.err.println(e.getMessage());
//		}
//		return id;
//	}
	
//	public static void saveOnExit(String hashCommit, String className, String methodName, long duration, int idRun, int idMethod, String retValue) {
//		System.out.println("saveOnExit");
//		Connection conn = null;
//		long ownDuration = duration;
//		try{
//			System.out.println("getConn");
//			conn = DBCPDataSource.getConnection();
//			System.out.println("createStatement");
//			Statement st = conn.createStatement();	
//			
//			// calculate own_duration
//			System.out.println("calculate own_duration");
//			String ownDurationSql = "SELECT SUM(cumulative_duration) AS sum FROM perfrt.methods WHERE caller_id="+ idMethod +";";
//			ResultSet rsOwnDuration = st.executeQuery(ownDurationSql);
//			if(rsOwnDuration.next()) {
//				long sumCumulativeDuration = rsOwnDuration.getLong("sum");
//				ownDuration -= sumCumulativeDuration;				
//			}
//			
//			System.out.println("select methods");
//			String queryCallerId = "SELECT * from perfrt.methods AS m INNER JOIN perfrt.files AS f ON m.file_id=f.id INNER JOIN perfrt.commits AS c ON f.commit_id=c.id WHERE m.id < " + idMethod + " AND m.caller_id IS NULL AND c.commit_hash='" + hashCommit + "' AND m.finished=false ORDER BY m.id DESC LIMIT 1;";
//			ResultSet rs = st.executeQuery(queryCallerId);
//			  
//			if( rs.next()) {
//				int idCaller = rs.getInt("id");
//				String queryUpdate = "UPDATE perfrt.methods SET caller_id=?, own_duration=?, cumulative_duration=?, finished=?, return_value=? WHERE id=?";
//				PreparedStatement preparedStmt = conn.prepareStatement(queryUpdate);
//						      preparedStmt.setInt(1, idCaller);
//						      preparedStmt.setLong(2, ownDuration);
//						      preparedStmt.setLong(3, duration);
//						      preparedStmt.setBoolean(4, true);
//						      preparedStmt.setString(5,  retValue);
//						      preparedStmt.setInt(6, idMethod);
//	
//				preparedStmt.executeUpdate();
//				System.out.println("update 1");
//			}else {
//				String queryUpdate = "UPDATE perfrt.methods SET own_duration=?, cumulative_duration=?, finished=?, return_value=? WHERE id=?";
//				PreparedStatement preparedStmt = conn.prepareStatement(queryUpdate);
//						      preparedStmt.setLong(1, ownDuration);
//						      preparedStmt.setLong(2, duration);
//						      preparedStmt.setBoolean(3, true);
//						      preparedStmt.setString(4,  retValue);
//						      preparedStmt.setInt(5, idMethod);
//	
//				preparedStmt.executeUpdate();
//				System.out.println("update2");
//				
//			}
//			st.close();
//			conn.close();
//			System.out.println("close");
//		}
//		catch (Exception e){
////			System.exit(0);
//			System.err.println("saveOnExit: Got an exception!");
//			System.err.println(e.getMessage());			
//			if(conn != null) {
//				try {
//					conn.close();
//				} catch (SQLException e1) {
////					e1.printStackTrace();
//					System.err.println("cannot close database connection!");
//					System.err.println(e1.getMessage());
//				}
//			}
//			System.exit(0);
//		}	
//	}
	
	
}



