package tree.study.branch_act;

import android.os.Bundle;

import tree.study.base.BaseActivity;
import tree.study.databinding.ActivityBluetoothBinding;

public class BluetoothActivity extends BaseActivity {
	private ActivityBluetoothBinding bind;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bind = ActivityBluetoothBinding.inflate(getLayoutInflater());
		setContentView(bind.getRoot());
	}
}