import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// New annotation to pass the variable
// https://stackoverflow.com/questions/66951575/how-to-pass-variables-to-advice
@Retention(RetentionPolicy.RUNTIME)
public @interface AgentArguments {
}