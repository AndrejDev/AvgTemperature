package temperature.manage;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import temperature.manage.calculate.ICalculate;
import temperature.util.AppLoaderUtil;
import temperature.util.AppProperties;

public enum TaskManager {
	INSTANCE;

	private static final Logger LOGGER = LogManager.getLogger(TaskManager.class.getName());

	private ScheduledExecutorService scheduler;
	private ReentrantLock lock = new ReentrantLock();

	public boolean startExecutor(String taskName, int initialDelay, int period) {
		lock.lock();
		ICalculate task = null;
		try {
			stopExecutor();
			scheduler = Executors.newSingleThreadScheduledExecutor();
			task = AppLoaderUtil.getTask(AppProperties.TASK_PKG_NAME, taskName);
			if (task != null) {
				scheduler.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.SECONDS);
				LOGGER.info("Schedule task " + taskName);
			}
		} finally {
			lock.unlock();
		}
		return task != null;

	}

	public boolean stopExecutor() {
		lock.lock();
		try {
			if (scheduler != null) {
				scheduler.shutdownNow();				
				LOGGER.info("Shutdown task: " + scheduler.isShutdown());
				return scheduler.isShutdown();
			}
		} finally {
			lock.unlock();
		}
		return true;
	}

}
