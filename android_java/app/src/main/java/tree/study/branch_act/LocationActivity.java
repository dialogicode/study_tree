package tree.study.branch_act;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import tree.study.base.BaseActivity;
import tree.study.branch_vm.LocationViewModel;
import tree.study.databinding.ActivityLocationBinding;

public class LocationActivity extends BaseActivity {
	private LocationViewModel vm;
	private ActivityLocationBinding bind;
	private FusedLocationProviderClient client;

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
			get_last_location();
		});
		bind.sensorSw.setOnCheckedChangeListener((view, bool) -> vm.getSensor().setValue(bool));

		if (!check_permission(perm_tool.get_fore_location_permissions()))
			request_permission(permissions);

		client = LocationServices.getFusedLocationProviderClient(this);
	}

	private void get_last_location() {
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
		    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			request_permission(perm_tool.get_not_granted_permission(perm_tool.get_fore_location_permissions()));
			return;
		}
		client.getLastLocation().addOnSuccessListener(this, location -> vm.setLocation(location));
	}
}