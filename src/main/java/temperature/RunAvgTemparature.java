package temperature;

import java.io.File;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import temperature.fileloader.loaderinterface.IFileLoader;
import temperature.service.TemperatureService;
import temperature.util.AppLoaderUtil;

import static temperature.util.AppProperties.*;

public class RunAvgTemparature {

	private static final Logger LOGGER = LogManager.getLogger(RunAvgTemparature.class.getName());

	public static void main(String[] args) {
		LOGGER.info("Start calculate avarange tempareture for countries.");

		setLocale(LOCALE_EN);
		LOGGER.info("System locale is " + getLocale());

		TemperatureService service = new TemperatureService();

		LOGGER.info("Init countries...");

		countriesList.stream().forEach(l -> {
			if (service.validate(l.toString(), getLocale())) {
				service.postCountry(l.toString());
				LOGGER.info(l.toString() + " - OK");
			} else {
				LOGGER.info(l.toString() + " - FAIL");
			}
		});

		IFileLoader fileLoader = AppLoaderUtil.getFileLoader(LOADER_PKG_NAME, FILE_LOADER_NAME);
		File inputFile = fileLoader != null ? fileLoader.getFile() : null;

		LOGGER.info(inputFile != null ? "Selected file is: " + inputFile : "No file selected");
		service.postTemperatureFile(inputFile);

		LOGGER.info("Init task '" + TASK_NAME + "' with delay " + taskDelay + " sec and period " + taskPeriod + "sec");
		service.startCalculate(TASK_NAME, taskDelay, taskPeriod);
		Scanner scanner = new Scanner(System.in);
		boolean isRunning = true;
		
		try {
			do {
				String line = scanner.nextLine();
				if (!line.equals("quit")) {
					service.postTemperatureString(line);
				} else {
					isRunning = false;
				}
			} while (isRunning);
			
		} catch (NoSuchElementException | IllegalStateException e) {
			System.out.println("Fatal error: program will terminate");
		}finally {
			if (scanner != null) {
				scanner.close();
			}
		}

		LOGGER.info("Stop task: '" + TASK_NAME + "'");
		service.stopCalculate();
		LOGGER.info("Finish calculate avarange tempareture for countries.");
		System.exit(0);

	}

}
