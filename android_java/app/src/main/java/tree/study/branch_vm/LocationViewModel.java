package tree.study.branch_vm;

import android.location.Location;

import androidx.lifecycle.MutableLiveData;

import tree.study.base.BaseViewModel;

public class LocationViewModel extends BaseViewModel {
	private MutableLiveData<Double> latitude;
	private MutableLiveData<Double> longitude;
	private MutableLiveData<Double> altitude;
	private MutableLiveData<Float> accuracy;
	private MutableLiveData<Float> speed;
	private MutableLiveData<String> address;
	private MutableLiveData<Boolean> updates;
	private MutableLiveData<Boolean> sensor;

	public LocationViewModel() {
		getLatitude().setValue(0.0);
		getLongitude().setValue(0.0);
		getAltitude().setValue(0.0);
		getAccuracy().setValue(0.0F);
		getSpeed().setValue(0.0F);
		getAddress().setValue("");
		getUpdates().setValue(false);
		getSensor().setValue(false);
	}

	public MutableLiveData<Double> getLatitude() {
		if (latitude == null) {
			latitude = new MutableLiveData<>();
		}
		return latitude;
	}

	public MutableLiveData<Double> getLongitude() {
		if (longitude == null) {
			longitude = new MutableLiveData<>();
		}
		return longitude;
	}

	public MutableLiveData<Double> getAltitude() {
		if (altitude == null) {
			altitude = new MutableLiveData<>();
		}
		return altitude;
	}

	public MutableLiveData<Float> getAccuracy() {
		if (accuracy == null) {
			accuracy = new MutableLiveData<>();
		}
		return accuracy;
	}

	public MutableLiveData<Float> getSpeed() {
		if (speed == null) {
			speed = new MutableLiveData<>();
		}
		return speed;
	}

	public MutableLiveData<String> getAddress() {
		if (address == null) {
			address = new MutableLiveData<>();
		}
		return address;
	}

	public MutableLiveData<Boolean> getUpdates() {
		if (updates == null) {
			updates = new MutableLiveData<>();
		}
		return updates;
	}

	public MutableLiveData<Boolean> getSensor() {
		if (sensor == null) {
			sensor = new MutableLiveData<>();
		}
		return sensor;
	}

	public void setLocation(Location location) {
		getLatitude().setValue(location.getLatitude());
		getLongitude().setValue(location.getLongitude());
		getAltitude().setValue(location.getAltitude());
		getAccuracy().setValue(location.getAccuracy());
		getSpeed().setValue(location.getSpeed());
	}
}