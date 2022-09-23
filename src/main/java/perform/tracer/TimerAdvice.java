package perform.tracer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.bytebuddy.asm.Advice;

public class TimerAdvice {

	@Advice.OnMethodEnter
	public static OnMethodEnterReturn enter(@Advice.Origin String method, @AgentArguments String agentArguments) {
//		String agentArguments = "org.apache.bcel.,29c17da7c24168113063cffae1a7f974225f2d0f,59";
		OnMethodEnterReturn onEnterValues = null;
		String[] agentArgs = agentArguments.trim().split("\\s*,\\s*");
		if (method.contains(agentArgs[0])) {
			onEnterValues = new OnMethodEnterReturn();
			onEnterValues.setPackageName(agentArgs[0]);
			onEnterValues.setCommitHash(agentArgs[1]);
			onEnterValues.setRunId(Integer.parseInt(agentArgs[2]));
//			onEnterValues.setStartTime(System.currentTimeMillis());
			String className = parseClassName(method);
			System.out.println("[ENTER] -\t[CLASS] : " + className + "\t[METHOD] : " + method);
			MethodEnter m = new MethodEnter(onEnterValues.getCommitHash(), className, method, onEnterValues.getRunId());
			int methodId = m.getId();
			onEnterValues.setMethodId(methodId);
			onEnterValues.setStartTime(m.getStartTime());
	
	//		read stack
	//		var walker =  StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
	//		walker.forEach(SF_CONSUMER);
			
		}
		return onEnterValues;
	}

	@Advice.OnMethodExit(onThrowable = Throwable.class)
	public static void exit(@Advice.Origin String method, @Advice.Enter OnMethodEnterReturn onEnterValues) {
		if(onEnterValues!= null) { 
			long end = System.currentTimeMillis();
			String packName = onEnterValues.getPackageName();
			String className = parseClassName(method).replace(packName, "") + ".java";
			long start = onEnterValues.getStartTime();
			long duration = end - start;
			String returnedType = "";
			MethodExit m = new MethodExit(onEnterValues.getCommitHash(), className, method, end, duration, onEnterValues.getMethodId(), returnedType);
			System.out.println("[EXIT] -\t[CLASS] : " + className + "\t[METHOD] : " + method + "\t[START] : " + start + "\t[END] : " + end
					+ " [DURATION] : " + duration);
		}
	}

	public static String parseClassName(String method) {
		String className = null;
		Pattern p = Pattern.compile("[^\\s]*\\(");
		Matcher m = p.matcher(method);
		if (m.find()) {
			String methodName = (String) m.group(0).toString();

			String[] methodAux = methodName.split("\\.");
			if (methodAux.length > 0) {
				className = methodAux[0];
				for (int i = 1; i < methodAux.length - 1; i++) {
					className += "." + methodAux[i];
				}
			} else {
				className = methodName;
			}
		}
		String last = className.substring(className.lastIndexOf('.') + 1);
		className = last + ".java";
		return className;
	}
}
