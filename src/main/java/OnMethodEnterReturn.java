import java.lang.StackWalker.StackFrame;
import java.util.List;

public class OnMethodEnterReturn {
	private long startTime;
	private String packageName;
	private String commitHash;
	private int idRun;
	private int idMethod;
	
	public int getIdMethod() {
		return idMethod;
	}
	public void setIdMethod(int idMethod) {
		this.idMethod = idMethod;
	}
	public int getIdRun() {
		return idRun;
	}
	public void setIdRun(int idTestCase) {
		this.idRun = idTestCase;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getCommitHash() {
		return commitHash;
	}
	public void setCommitHash(String commitHash) {
		this.commitHash = commitHash;
	}
}
