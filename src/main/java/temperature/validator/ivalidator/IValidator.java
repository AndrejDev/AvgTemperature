package temperature.validator.ivalidator;

public interface IValidator<T> {

	public boolean isValid(T t);
	
	public String getErrorMessage(String language);
	
}
