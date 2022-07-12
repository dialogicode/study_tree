package tree.study.branch_act;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.util.Log;

import tree.study.base.BaseActivity;
import tree.study.databinding.ActivityBluetoothBinding;

public class BluetoothActivity extends BaseActivity {
	private final String TAG = "DEV_BT_ACT";
	private ActivityBluetoothBinding bind;
	private BluetoothAdapter bt_adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bind = ActivityBluetoothBinding.inflate(getLayoutInflater());
		setContentView(bind.getRoot());

		if (!check_permission(perm_tool.get_bluetooth_permissions()))
			request_permission(permissions);

		bt_adapter = BluetoothAdapter.getDefaultAdapter();

		if (bt_adapter == null) not_support_bt();
		else support_bt();
	}

	private void not_support_bt() {
		Log.i(TAG, "블루투스 미지원 기기");
	}

	private void support_bt() {
		Log.i(TAG, "블루투스 지원 기기");
	}
}