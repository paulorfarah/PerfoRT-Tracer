

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
		PrintStream fileOut = new PrintStream("../../perfrt/logs/" + agentArgs[0] + "-" + agentArgs[1] + "-" + agentArgs[2]+ "-perfrt-profiler.log");
		System.setOut(fileOut);
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	}
    System.out.println("Premain" );
    final String packName = agentArgs[0];
//    final String packName = "";
    
    new AgentBuilder.Default()
	    .with(new AgentBuilder.InitializationStrategy.SelfInjection.Eager())
	    .type((ElementMatchers.nameStartsWith(packName)))
	    .transform((builder, typeDescription, classLoader, module) -> builder
	            .method(ElementMatchers.any())
	            .intercept(Advice.withCustomMapping().bind(AgentArguments.class, arguments).to(TimerAdvice.class))
	    ).installOn(instrumentation);
}}

//exemplo dando erro:
//java -javaagent:/home/usuario/go-work/src/github.com/paulorfarah/perfrt/perfrt-profiler-0.0.1-SNAPSHOT.jar=org.apache.bcel.,29c17da7c24168113063cffae1a7f974225f2d0f,59  -jar /home/usuario/go-work/src/github.com/paulorfarah/perfrt/junit-platform-console-standalone-1.8.2.jar -cp .:target/test-classes/:target/classes:/home/usuario/.m2/repository/org/junit/jupiter/junit-jupiter/5.8.2/junit-jupiter-5.8.2.jar:/home/usuario/.m2/repository/org/junit/jupiter/junit-jupiter-api/5.8.2/junit-jupiter-api-5.8.2.jar:/home/usuario/.m2/repository/org/opentest4j/opentest4j/1.2.0/opentest4j-1.2.0.jar:/home/usuario/.m2/repository/org/junit/platform/junit-platform-commons/1.8.2/junit-platform-commons-1.8.2.jar:/home/usuario/.m2/repository/org/apiguardian/apiguardian-api/1.1.2/apiguardian-api-1.1.2.jar:/home/usuario/.m2/repository/org/junit/jupiter/junit-jupiter-params/5.8.2/junit-jupiter-params-5.8.2.jar:/home/usuario/.m2/repository/org/junit/jupiter/junit-jupiter-engine/5.8.2/junit-jupiter-engine-5.8.2.jar:/home/usuario/.m2/repository/org/junit/platform/junit-platform-engine/1.8.2/junit-platform-engine-1.8.2.jar:/home/usuario/.m2/repository/net/java/dev/jna/jna/5.11.0/jna-5.11.0.jar:/home/usuario/.m2/repository/net/java/dev/jna/jna-platform/5.11.0/jna-platform-5.11.0.jar:/home/usuario/.m2/repository/org/apache/commons/commons-lang3/3.12.0/commons-lang3-3.12.0.jar:/home/usuario/.m2/repository/javax/javaee-api/6.0/javaee-api-6.0.jar:/home/usuario/.m2/repository/org/apache/commons/commons-exec/1.3/commons-exec-1.3.jar -m org.apache.bcel.HandleTestCase#testBranchHandle
//java -javaagent:/mnt/sda4/go-work/src/github.com/paulorfarah/perfrt/perfrt-profiler-0.0.1-SNAPSHOT.jar=org.apache.bcel.,29c17da7c24168113063cffae1a7f974225f2d0f,59  -jar /mnt/sda4/go-work/src/github.com/paulorfarah/perfrt/junit-platform-console-standalone-1.8.2.jar -cp .:target/test-classes/:target/classes:/users/farah/.m2/repository/org/junit/jupiter/junit-jupiter/5.8.2/junit-jupiter-5.8.2.jar:/users/farah/.m2/repository/org/junit/jupiter/junit-jupiter-api/5.8.2/junit-jupiter-api-5.8.2.jar:/users/farah/.m2/repository/org/opentest4j/opentest4j/1.2.0/opentest4j-1.2.0.jar:/users/farah/.m2/repository/org/junit/platform/junit-platform-commons/1.8.2/junit-platform-commons-1.8.2.jar:/users/farah/.m2/repository/org/apiguardian/apiguardian-api/1.1.2/apiguardian-api-1.1.2.jar:/users/farah/.m2/repository/org/junit/jupiter/junit-jupiter-params/5.8.2/junit-jupiter-params-5.8.2.jar:/users/farah/.m2/repository/org/junit/jupiter/junit-jupiter-engine/5.8.2/junit-jupiter-engine-5.8.2.jar:/users/farah/.m2/repository/org/junit/platform/junit-platform-engine/1.8.2/junit-platform-engine-1.8.2.jar:/users/farah/.m2/repository/net/java/dev/jna/jna/5.11.0/jna-5.11.0.jar:/users/farah/.m2/repository/net/java/dev/jna/jna-platform/5.11.0/jna-platform-5.11.0.jar:/users/farah/.m2/repository/org/apache/commons/commons-lang3/3.12.0/commons-lang3-3.12.0.jar:/users/farah/.m2/repository/javax/javaee-api/6.0/javaee-api-6.0.jar:/users/farah/.m2/repository/org/apache/commons/commons-exec/1.3/commons-exec-1.3.jar -m org.apache.bcel.HandleTestCase#testBranchHandle