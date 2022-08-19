package perfrt.profiler;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.instrument.Instrumentation;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;

class AgentTest {

	public static void premain(String arguments, Instrumentation instrumentation) {
		System.out.println(arguments.trim());
		String[] agentArgs = arguments.trim().split("\\s*,\\s*");

		try {
			PrintStream fileOut = new PrintStream("../../perfrt/logs/" + agentArgs[0] + "-" + agentArgs[1] + "-"
					+ agentArgs[2] + "-perfrt-profiler.log");
			System.setOut(fileOut);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("Premain");
		final String packName = agentArgs[0];
//    final String packName = "";

		new AgentBuilder.Default().with(new AgentBuilder.InitializationStrategy.SelfInjection.Eager())
				.type((ElementMatchers.nameStartsWith(packName)))
				.transform(
						(builder, typeDescription, classLoader,
								module) -> builder.method(ElementMatchers.any()).intercept(Advice.withCustomMapping()
										.bind(AgentArguments.class, arguments).to(TimerAdvice.class)))
				.installOn(instrumentation);
	}
}
