package temperature.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import temperature.manage.TaskManager;
import temperature.manage.TemperatureManager;
import temperature.validator.LocaleValidator;
import temperature.validator.ivalidator.IValidator;

public class TemperatureService {

	private static final Logger LOGGER = LogManager.getLogger(TemperatureService.class.getName());

	public Boolean postCountry(String country) {
		return TemperatureManager.INSTANCE.addCountry(country);
	}

	public Boolean postTemperatureFile(File inputFile) {
		try {
			addTemperatureFromFile(inputFile);
		} catch (FileNotFoundException e) {
			LOGGER.error("Cannot read from input file", e);
			return false;
		}
		return true;
	}

	public Boolean postTemperatureString(String data) {
		return addTemperatureFromString(data);
	}

	public List<Double> getTemperature(String country) {
		return TemperatureManager.INSTANCE.getTemperature(country);
	}

	public Set<String> getCountries() {
		return TemperatureManager.INSTANCE.getCountries();
	}

	public Boolean validate(Object o, String language) {
		Boolean isValid = null;
		IValidator<String> validator = null;
		if (o instanceof String) {
			validator = new LocaleValidator();
			isValid = validator.isValid((String) o);
		}
		if (validator != null && isValid != null && !isValid) {
			LOGGER.error("Validator error: " + validator.getErrorMessage(language));

		}
		return isValid;
	}

	public Boolean startCalculate(String taskName, int initialDelay, int period) {
		return TaskManager.INSTANCE.startExecutor(taskName, initialDelay, period);
	}

	public Boolean stopCalculate() {
		return TaskManager.INSTANCE.stopExecutor();
	}

	private void addTemperatureFromFile(File file) throws FileNotFoundException {
		if (file != null) {
			System.out.println("--- Input ---");
			Scanner fileScanner = new Scanner(file);
			while (fileScanner.hasNextLine()) {
				addTemperatureFromString(fileScanner.nextLine());
			}
			fileScanner.close();
			System.out.println("--------------");
		}
	}

	private boolean addTemperatureFromString(String data) {
		Scanner dataScanner = null;
		try {
			dataScanner = new Scanner(data);
			dataScanner.useDelimiter("\\s");
			String country = dataScanner.hasNext() ? dataScanner.next() : null;
			String temperature = dataScanner.hasNext() ? dataScanner.next() : null;
			Double value = Double.parseDouble(temperature);
			return TemperatureManager.INSTANCE.addTemperature(country, value);

		} catch (NoSuchElementException e) {
			LOGGER.warn("No more tokens are available");
			System.out.println("Invalid input '" + data + "'");
		} catch (NullPointerException | NumberFormatException e) {
			LOGGER.warn("Error during parsing of temperature");
			System.out.println("Invalid input '" + data + "'");
		} finally {
			if (dataScanner != null) {
				dataScanner.close();
			}
		}
		return false;
	}

}
