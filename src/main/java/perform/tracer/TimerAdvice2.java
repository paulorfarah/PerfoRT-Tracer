package perform.tracer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.bytebuddy.asm.Advice;

public class TimerAdvice2 {
	@Advice.OnMethodEnter
	public static void onEnter(@Advice.Origin String method) {
		System.out.println("OnEnter");
		
		Pattern p = Pattern.compile("[^\\s]*\\(");
		Matcher m = p.matcher(method);
		if (m.find()) {
		    String methodName = (String)m.group(0).toString();
//		    System.out.println(methodName); //com.github.shehanperera.example.Method.method4(
		    
		    String[] methodAux = methodName.split("\\.");
		    if(methodAux.length > 0) {
				String className = methodAux[0];
				for(int i = 1; i < methodAux.length-1; i++) {
					className += "." + methodAux[i];
				}
				System.out.println(className);
		    }else {
		    	System.out.println("ERROR: Class name not found.");
		    }
		}
		
		
//		String className = parseClassName(method).replace(onEnterValues.getPackageName(), "") + ".java";
//		System.out.println("onEnter - Class: " + className + " method: " + method);
	}
	
	@Advice.OnMethodExit
	public static void onExit() {
		System.out.println("OnExit");
	}

}
