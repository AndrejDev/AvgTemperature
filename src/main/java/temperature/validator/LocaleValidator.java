package temperature.validator;

import java.util.Arrays;
import java.util.Locale;

import temperature.util.ErrorMsg;
import temperature.validator.ivalidator.IValidator;

public class LocaleValidator implements IValidator<String> {

	private ErrorMsg msg;
	private String[] msgParams;

	@Override
	public boolean isValid(String locale) {
		if (locale == null || locale.isEmpty()) {
			setErrorMessage(ErrorMsg.LOCALE_INVALID, null);
			return false;
		}
		boolean isPresent = Arrays.stream(Locale.getISOCountries()).filter(x -> locale.equals(x)).findFirst()
				.isPresent();
		if (!isPresent) {
			setErrorMessage(ErrorMsg.LOCALE_NOT_FOUND, new String[] { locale });
		}

		return isPresent;
	}

	@Override
	public String getErrorMessage(String language) {
		return msg != null ? msg.message(msgParams, language) : null;
	}

	public void setErrorMessage(ErrorMsg msg, String[] msgParams) {
		this.msg = msg;
		this.msgParams = msgParams;
	}

}
