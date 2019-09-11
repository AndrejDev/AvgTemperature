package temperature.manage.calculate;

import java.util.List;
import java.util.Map.Entry;
import temperature.annotation.TaskLoaderProperty;
import temperature.manage.TemperatureManager;
import static temperature.util.AppProperties.PRECISION;


@TaskLoaderProperty(name = "avg")
public class AvgCalculate extends Thread implements ICalculate {

	public void run() {	
		System.out.println("--- Output ---");
		for (Entry<String, List<Double>> data : TemperatureManager.INSTANCE.getCurrentData().entrySet()) {
			Double avgT = calculate(data.getValue());
			if (Double.compare(avgT, Double.valueOf(0.0)) > 0) {
				System.out.println(data.getKey() + " " + avgT);
			}
		}
		System.out.println("--------------");
	}

	private Double calculate(List<Double> values) {
		if (!values.isEmpty()) {
			return (double) Math.round(values.stream().mapToDouble(Number::doubleValue).average().getAsDouble() * PRECISION) / PRECISION;
		}
		return new Double(0.0);
	}

}
