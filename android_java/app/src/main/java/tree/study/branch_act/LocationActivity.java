package tree.study.branch_act;

import android.Manifest;
import android.content.pm.PackageManager;
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

		vm.getLatitude().observe(this, doubleV -> bind.latitude.setText(doubleV + ""));
		vm.getLongitude().observe(this, doubleV -> bind.longitude.setText(doubleV + ""));
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
		client.getLastLocation().addOnSuccessListener(this, location -> vm.setLocation(location));
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
				if (location != null) vm.setLocation(location);
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
}