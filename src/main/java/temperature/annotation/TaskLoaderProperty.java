package temperature.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.constraints.NotBlank;

@Retention(RUNTIME)
@Target(TYPE)
public @interface TaskLoaderProperty {
	@NotBlank(message="TaskLoaderProperty must have name")
	String name();
}
