package tree.study.branch_act;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.util.List;

import tree.study.base.BaseActivity;
import tree.study.branch_vm.LocationViewModel;
import tree.study.databinding.ActivityLocationBinding;

public class LocationActivity extends BaseActivity {
	private LocationViewModel vm;
	private ActivityLocationBinding bind;
	private FusedLocationProviderClient client;
	private LocationRequest request;
	private LocationCallback callback;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		vm = new ViewModelProvider(this).get(LocationViewModel.class);
		bind = ActivityLocationBinding.inflate(getLayoutInflater());
		setContentView(bind.getRoot());

		vm.getLatitude().observe(this, doubleV -> bind.latitude.setText(get_position_dms(doubleV)));
		vm.getLongitude().observe(this, doubleV -> bind.longitude.setText(get_position_dms(doubleV)));
		vm.getAltitude().observe(this, doubleV -> bind.altitude.setText(doubleV + ""));
		vm.getAccuracy().observe(this, floatV -> bind.accuracy.setText(floatV + ""));
		vm.getSpeed().observe(this, floatV -> bind.speed.setText(floatV + ""));
		vm.getAddress().observe(this, addr -> bind.address.setText(addr));
		vm.getUpdates().observe(this, bool -> bind.updateTv.setText(bool ? "ON" : "OFF"));
		vm.getSensor().observe(this, bool -> bind.sensorTv.setText(bool ? "GPS Sensor" : "Cell Tower + WIFI"));

		bind.updatesSw.setOnCheckedChangeListener((view, bool) -> {
			vm.getUpdates().setValue(bool);

			if (bool) start_location_updates();
			else stop_location_updates();
		});
		bind.sensorSw.setOnCheckedChangeListener((view, bool) -> {
			vm.getSensor().setValue(bool);

			if (bool) request.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
			else request.setPriority(Priority.PRIORITY_LOW_POWER);

			if (Boolean.TRUE.equals(vm.getUpdates().getValue())) {
				stop_location_updates();
				start_location_updates();
			}
		});

		if (!check_permission(perm_tool.get_fore_location_permissions()))
			request_permission(permissions);

		client = LocationServices.getFusedLocationProviderClient(this);
		request = get_location_request();
		callback = get_location_callback();
	}

	private void get_last_location() {
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
		    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			request_permission(perm_tool.get_not_granted_permission(perm_tool.get_fore_location_permissions()));
			return;
		}
		client.getLastLocation().addOnSuccessListener(this, location -> {
			vm.setLocation(location);
			set_address(location.getLatitude(), location.getLongitude());
		});
	}

	private LocationRequest get_location_request() {
		long second = 1000L;
		long interval_millis = 5L * second;
		long fast_interval_millis = 2L * second;
		return LocationRequest.create()
		                      .setInterval(interval_millis)
		                      .setFastestInterval(fast_interval_millis)
		                      .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY);
	}

	private LocationCallback get_location_callback() {
		return new LocationCallback() {
			@Override
			public void onLocationResult(@NonNull LocationResult locationResult) {
				Location location = locationResult.getLastLocation();
				if (location != null) {
					vm.setLocation(location);
					set_address(location.getLatitude(), location.getLongitude());
				}
			}
		};
	}

	private void start_location_updates() {
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
		    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			request_permission(perm_tool.get_not_granted_permission(perm_tool.get_fore_location_permissions()));
			return;
		}
		client.requestLocationUpdates(request, callback, null);
	}

	private void stop_location_updates() {
		client.removeLocationUpdates(callback);
	}

	private void set_address(double lat, double lng) {
		Geocoder geocoder = new Geocoder(this);

		try {
			List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
			vm.getAddress().setValue(addresses.get(0).getAddressLine(0));
		} catch (Exception e) {
			vm.getAddress().setValue("주소 정보 찾는 중");
		}
	}

	private String get_position_dms(double latlng) {
		double val = latlng;
		int degree = (int) val;
		int minutes = (int) ((60 * val) - (60 * degree));
		int seconds = (int) ((3600 * val) - (3600 * degree) - (60 * minutes));
		return String.format("%3d도 %2d분 %2d초", degree, minutes, seconds);
	}
}