package tree.study.branch_act;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;

import tree.study.base.BaseActivity;
import tree.study.branch_vm.LocationViewModel;
import tree.study.databinding.ActivityLocationBinding;

public class LocationActivity extends BaseActivity {
	private LocationViewModel vm;
	private ActivityLocationBinding bind;

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

		bind.updatesSw.setOnCheckedChangeListener((view, bool) -> vm.getUpdates().setValue(bool));
		bind.sensorSw.setOnCheckedChangeListener((view, bool) -> vm.getSensor().setValue(bool));
	}
}