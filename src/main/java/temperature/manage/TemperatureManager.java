package temperature.manage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static temperature.util.AppProperties.PRECISION;


public enum TemperatureManager {
	INSTANCE;

	private static final Logger LOGGER = LogManager.getLogger(TemperatureManager.class.getName());

	private volatile Map<String, List<Double>> cachedData;
	private ReadWriteLock rwlock;
	
	
	private TemperatureManager() {
		rwlock = new ReentrantReadWriteLock();
		cachedData = new TreeMap<>();
	}

	public boolean addCountry(String country) {
		rwlock.writeLock().lock();
		try {
			if (country != null && !country.isEmpty() && !cachedData.containsKey(country)) {
				cachedData.put(country, new ArrayList<Double>());
				return true;
			}
		} finally {
			rwlock.writeLock().unlock();
		}
		return false;
	}

	public Set<String> getCountries() {
		rwlock.readLock().lock();
		try {
			return new HashSet<>(cachedData.keySet());
		} finally {
			rwlock.readLock().unlock();
		}
	}

	public List<Double> getTemperature(String country) {
		rwlock.readLock().lock();
		try {
			return cachedData.get(country) != null ? new ArrayList<>(cachedData.get(country)) : null;
		} finally {
			rwlock.readLock().unlock();
		}
	}

	public Map<String, List<Double>> getCurrentData() {
		rwlock.readLock().lock();
		try {
			return new TreeMap<String, List<Double>>(cachedData);
		} finally {
			rwlock.readLock().unlock();
		}
	}

	public boolean addTemperature(String country, Double temperature) {
		rwlock.writeLock().lock();
		try {
			if (country != null && !country.isEmpty() && temperature != null) {
				if (getCountries().contains(country)) {
					temperature = (double) Math.round(temperature * PRECISION) / PRECISION;
					System.out.println("Add '" + temperature + "' to '" + country + "'");
					return cachedData.get(country).add(temperature);
				} else {
					LOGGER.warn("Country not exist in list - '" + country + "'");
					System.out.println("Invalid input '" + country + temperature + "'");
				}
			} else {
				LOGGER.warn("Invalid input - '" + country + "'" + " '" + temperature);
				System.out.println("Invalid input '" + country + temperature + "'");
			}
		} finally {
			rwlock.writeLock().unlock();
		}
		return false;
	}

}
