package perfrt.profiler;

public class OnMethodEnterReturn {
	private long startTime;
	private String packageName;
	private String commitHash;
	private int runId;
	private int methodId;
	
	public int getMethodId() {
		return methodId;
	}
	public void setMethodId(int methodId) {
		this.methodId = methodId;
	}
	public int getRunId() {
		return runId;
	}
	public void setRunId(int idTestCase) {
		this.runId = idTestCase;
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
