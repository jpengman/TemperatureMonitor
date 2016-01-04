package se.anviken.temperaturemonitor;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import se.anviken.temperaturemonitor.persistance.Sensor;
import se.anviken.temperaturemonitor.persistance.SensorType;

public class SensorHandler {

	protected List<Sensor> sensorList = null;
	protected List<SensorType> sensorTypeList = null;
	private List<Sensor> sensorsNotInDb = null;

	public static int SENSOR_ID = 1;
	public static int SENSOR_NAME = 2;
	public static int SENSOR_ADRESS = 3;
	public static int SENSOR_DESCRIPTION = 4;
	public static int SENSOR_TYPE = 5;

	
	/**
	 * Sensorer för att mäta utomhustemperaturen 
	 */
	public static int TYPE_OUTSIDE = 1;
	/**
	 * //Sensorer för att mäta rumstemperaturer 
	 */
	public static int TYPE_ROOM = 2; 
	public static int TYPE_HEATING_HP = 3; //Sensorer för att monitorera värmepumpen
	public static int TYPE_HEATING_FP_ACC = 4;//Sensorer för att monitorera värmeöverföring mellan vedspis och ackumulatortank
	public static int TYPE_HEATING_ACC_HP = 5; //Sensorer för att monitorera värmeöverföring mellan ackumulatortank och värmepump
	public static int TYPE_HEATING_ACC = 6; //Sensorer för att monitorera ackumulatortanken
	public static int TYPE_HEATING_RAD = 7; //Sesnosrer för att monitorera elementen
	public static int TYPE_HEATING_FH = 8; //Sesnosrer för att monitorera golvvärmen
	public static int TYPE_WATER = 9; //Sensorer för att mäta vattentemperaturer
	public static int TYPE_BRINE = 10; //Sensorer för att monitorera brinetemperaturer

	
	public List<String> getListOf(int whatToGet, Integer sensorType) {
		List<String> stringList = new ArrayList<String>();
		for (Sensor sensor : getSensorList()) {
			if (sensorType == null
					|| sensorType.intValue() == sensor.getSensorType().getSensorTypeId()) {
				if (whatToGet == SENSOR_NAME) {
					stringList.add(sensor.getName());
				} else if (whatToGet == SENSOR_ADRESS) {
					stringList.add(sensor.getName());
				} else if (whatToGet == SENSOR_DESCRIPTION) {
					stringList.add(sensor.getName());
				}
			}
		}
		return stringList;
	}
	public int getIdFromName(String name){
		for(Sensor sensor:getSensorList()){
			if (name.equals(sensor.getName())){
				return sensor.getSensorId();
			}
		}
		return -1;
	}

	public List<Sensor> getSensorList() {
		return sensorList;
	}


	public void setSensorList(List<Sensor> sensorList) {
		this.sensorList = sensorList;
	}
	public List<SensorType> getSensorTypeList() {
		return sensorTypeList;
	}
	public void setSensorTypeList(List<SensorType> sensorTypeList) {
		this.sensorTypeList = sensorTypeList;
	}
	
	public List<String> getConnectedSensors() {

		File file = new File("/mnt/1wire");
		String[] directories = file.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				if (name.startsWith("28.")) {
					return new File(current, name).isDirectory();
				} else {
					return false;
				}
			}
		});
		List<String> dirList = new ArrayList<String>();
		for (int i = 0; i < directories.length; i++) {
			dirList.add(directories[i]);
		}
		return dirList;
	}

	public List<Sensor> getSensorsNotInDb() {
		if (this.sensorsNotInDb == null) {
			reloadSensorsNotInDb();
		}
		return this.sensorsNotInDb;
	}

	protected void reloadSensorsNotInDb() {
		List<String> connectedSensors = getConnectedSensors();
		List<Sensor> inDb = getSensorList();
		List<Sensor> notInDb = new ArrayList<Sensor>();
		for (String address : connectedSensors) {
			boolean isInDb = false;
			for (Sensor sensor : inDb) {
				if (sensor.getAddress().equals(address)) {
					isInDb = true;
				}
			}
			if (!isInDb) {
				Sensor newSensor = new Sensor();
				newSensor.setAddress(address);
				notInDb.add(newSensor);
			}
		}
		this.sensorsNotInDb = notInDb;
	}

	public void setSensorsNotInDb(List<Sensor> sensorList) {
		this.sensorsNotInDb = sensorList;
	}
	
}
