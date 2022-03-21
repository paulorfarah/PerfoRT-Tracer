

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.instrument.Instrumentation;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;

class AgentTest {

public static void premain(String arguments, Instrumentation instrumentation) {
	try {
		PrintStream fileOut = new PrintStream("./perfrt-profiler-out.txt");
		System.setOut(fileOut);
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	}
    System.out.println("Premain" );
    String[] agentArgs = arguments.trim().split("\\s*,\\s*");
    new AgentBuilder.Default()
	    .with(new AgentBuilder.InitializationStrategy.SelfInjection.Eager())
	    .type((ElementMatchers.nameStartsWith(agentArgs[0])))
	    .transform((builder, typeDescription, classLoader, module) -> builder
	            .method(ElementMatchers.any())
	            .intercept(Advice.withCustomMapping().bind(AgentArguments.class, arguments).to(TimerAdvice.class))
	    ).installOn(instrumentation);
}}
