package temperature.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;

import temperature.RunAvgTemparature;
import temperature.annotation.FileLoaderProperty;
import temperature.annotation.TaskLoaderProperty;
import temperature.fileloader.loaderinterface.IFileLoader;
import temperature.manage.calculate.ICalculate;

public class AppLoaderUtil {

	private static final Logger LOGGER = LogManager.getLogger(AppLoaderUtil.class.getName());
	
	public static IFileLoader getFileLoader(String pkg, String name) {
		try {
			Reflections reflections = new Reflections(pkg);
			Class<? extends IFileLoader> clazz = reflections.getSubTypesOf(IFileLoader.class).stream()
					.filter(clz -> (clz.getAnnotation(FileLoaderProperty.class).name()).equals(name)).findAny()
					.orElse(null);
			LOGGER.info(clazz != null ? "Loader class: " + clazz.getName() : "No class selected");
			if (clazz != null) {
				ClassLoader classLoader = RunAvgTemparature.class.getClassLoader();
				Class<?> myObjectClass = classLoader.loadClass(clazz.getName());
				return (IFileLoader) myObjectClass.newInstance();
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			LOGGER.warn("Problem get file loader", e);
		}

		return null;
	}
	
	public static ICalculate getTask(String pkg, String name) {
		try {
			Reflections reflections = new Reflections(pkg);
			Class<? extends ICalculate> clazz = reflections.getSubTypesOf(ICalculate.class).stream()
					.filter(clz -> (clz.getAnnotation(TaskLoaderProperty.class).name()).equals(name)).findAny()
					.orElse(null);
			LOGGER.info(clazz != null ? "Loaded task is : " + clazz.getName() : "No task found");
			if (clazz != null) {
				ClassLoader classLoader = RunAvgTemparature.class.getClassLoader();
				Class<?> myObjectClass = classLoader.loadClass(clazz.getName());
				return (ICalculate) myObjectClass.newInstance();
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			LOGGER.warn("Problem get task " + name, e);
		}

		return null;
	}
	
}
