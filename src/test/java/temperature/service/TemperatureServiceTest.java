package temperature.service;

import static org.junit.jupiter.api.Assertions.*;
import static temperature.util.AppProperties.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(OrderAnnotation.class)
class TemperatureServiceTest {

	static TemperatureService testService, concurrentService;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		setLocale(LOCALE_EN);
		testService = new TemperatureService();
		concurrentService = new TemperatureService();
	}

	@Test
	@Order(1)
	public void validateTest() {
		assertEquals(getLocale(), "EN");
		assertNull(testService.validate(null, getLocale()));
		assertNull(testService.validate(null, null));
		assertTrue(testService.validate("CZ", null));
		assertTrue(testService.validate("CZ", ""));
		assertFalse(testService.validate("", getLocale()));
		assertFalse(testService.validate("cz", getLocale()));
		assertFalse(testService.validate("Cz", getLocale()));
		assertFalse(testService.validate("cZ", getLocale()));
		assertFalse(testService.validate("CZE", getLocale()));
		assertFalse(testService.validate("C", getLocale()));
		assertTrue(testService.validate("CZ", getLocale()));
	}

	@Test
	@Order(2)
	public void getCountriesTestBeforeInit() {
		assertNotNull(testService.getCountries());
		assertTrue(testService.getCountries().isEmpty());
	}

	@Test
	@Order(3)
	public void postCountryTest() {
		assertFalse(testService.postCountry(""));
		assertFalse(testService.postCountry(null));

		assertTrue(testService.postCountry("CZ"));
		assertFalse(testService.postCountry("CZ"));
		assertTrue(testService.postCountry("DE"));

		assertFalse(concurrentService.postCountry("DE"));
	}

	@Test
	@Order(4)
	public void getCountriesTestAfterInit() {
		assertNotNull(testService.getCountries());
		assertFalse(testService.getCountries().isEmpty());
		assertEquals(2, testService.getCountries().size());

		assertEquals(2, concurrentService.getCountries().size());
	}

	@Test
	@Order(6)
	public void postTemperatureStringTest() {
		assertFalse(testService.postTemperatureString(null));
		assertFalse(testService.postTemperatureString(""));

		assertTrue(testService.postTemperatureString("DE -8.99"));
		assertTrue(testService.postTemperatureString("DE +8.99"));
		assertTrue(testService.postTemperatureString("DE 0"));
		assertTrue(testService.postTemperatureString("DE 0.0"));

		assertFalse(testService.postTemperatureString("IT -8.99"));
		assertFalse(testService.postTemperatureString("IT"));
		assertFalse(testService.postTemperatureString("12"));

		assertFalse(testService.postTemperatureString("  DE  10 "));
		assertFalse(testService.postTemperatureString("DE  10"));
		assertFalse(testService.postTemperatureString("De 36.0"));
		assertFalse(testService.postTemperatureString("de 36.0"));
		assertFalse(testService.postTemperatureString("dE 36.6"));
		assertFalse(testService.postTemperatureString("DE"));
		assertFalse(testService.postTemperatureString("11"));
		assertFalse(testService.postTemperatureString("DE null"));
		assertFalse(testService.postTemperatureString("DE NULL"));
		assertFalse(testService.postTemperatureString("DE  \"\""));
		assertFalse(testService.postTemperatureString("DE  \" \""));
		assertFalse(testService.postTemperatureString("DE DE 5"));
		assertTrue(testService.postTemperatureString("DE 5.555 1.0"));

		assertTrue(testService.postTemperatureString("CZ 10.1"));
		assertTrue(concurrentService.postTemperatureString("CZ -20.2"));
		assertTrue(testService.postTemperatureString("CZ 10.1"));
	}

	@Test
	@Order(7)
	public void getTemperatureTest() {
		assertNull(testService.getTemperature("IT"));
		assertNotNull(testService.getTemperature("DE"));
		assertEquals(5, testService.getTemperature("DE").size());
		assertEquals(3, testService.getTemperature("CZ").size());

		assertEquals(5, concurrentService.getTemperature("DE").size());
		assertEquals(3, concurrentService.getTemperature("CZ").size());
	}

	@Test
	@Order(8)
	public void startCalculateTest() {
		assertTrue(concurrentService.startCalculate(TASK_NAME, 1, 1));
		assertTrue(testService.startCalculate(TASK_NAME, 0, 1));
		try {
			Thread.sleep(999);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	@Test
	@Order(9)
	public void stopCalculateTest() {
		assertTrue(testService.stopCalculate());
		assertTrue(concurrentService.stopCalculate());
	}
}
