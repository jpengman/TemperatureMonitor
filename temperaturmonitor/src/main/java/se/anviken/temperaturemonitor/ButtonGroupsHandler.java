package se.anviken.temperaturemonitor;

import java.util.List;

public class ButtonGroupsHandler extends SensorHandler{
	// All sensors buttons
		protected List<String> selectedOptions;

		public List<String> getSensorNames() {
			return getListOf(SensorHandler.SENSOR_NAME, null);
		}

		public List<String> getSelectedOptions() {
			return selectedOptions;
		}

		public void setSelectedOptions(List<String> selectedOptions) {
			this.selectedOptions = selectedOptions;
			updateSeries();
		}
		// ----------------------
	
		// Room sensor buttons
				protected List<String> selectedRoomSensors;

				public List<String> getRoomSensorNames() {
					return getListOf(SensorHandler.SENSOR_NAME, TYPE_ROOM);
				}

				public List<String> getSelectedRoomSensors() {
					return selectedRoomSensors;
				}

				public void setSelectedRoomSensors(List<String> selectedOptions) {
					this.selectedRoomSensors = selectedOptions;
					updateSeries();
				}
				// VP sensor buttons
				protected List<String> selectedHeatPumpSensors;

				public List<String> getHeatPumpSensorNames() {
					return getListOf(SensorHandler.SENSOR_NAME, TYPE_HEATING_HP);
				}

				public List<String> getSelectedHeatPumpSensors() {
					return selectedHeatPumpSensors;
				}

				public void setSelectedHeatPumpSensors(List<String> selectedOptions) {
					this.selectedHeatPumpSensors = selectedOptions;
					updateSeries();
				}

		public void updateSeries() {
			// Needs to be overridden			
		}
}
