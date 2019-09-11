package temperature.util;

public enum ErrorMsg {
	LOCALE_NOT_FOUND("Národní prostøedí #param1# nebylo nalezeno", "Locale #param1# not found"),
	LOCALE_INVALID("Národní prostøedí je NULL nebo EMPTY", "Lolale is NULL or EMPTY");

	public static final String CZ = "CZ";
	public static final String EN = "EN";

	private String messageCz;
	private String messageEn;

	private ErrorMsg(String messageCz, String messageEn) {
		this.messageCz = messageCz;
		this.messageEn = messageEn;
	}

	public String message(String locale) {
		return message(null, locale);
	}

	public String message(String[] param, String locale) {
		switch (locale) {
		case CZ:
			return processParam(this.messageCz, param);
		case EN:
			return processParam(this.messageEn, param);
		default:
			return processParam(this.messageEn, param);
		}
	}

	private String processParam(String message, String[] param) {
		if (param != null) {
			for (int i = 0; i < param.length; i++) {
				message = message.replace("#param" + (i + 1) + "#", param[i]);
			}
		}
		return message;
	}
}
