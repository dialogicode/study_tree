package tree.study.branch_act;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import tree.study.base.BaseActivity;
import tree.study.databinding.ActivityBluetoothBinding;

public class BluetoothActivity extends BaseActivity {
	private final String TAG = "DEV_BT_ACT";
	private ActivityBluetoothBinding bind;
	private BluetoothAdapter bt_adapter;
	private BT_Receiver bt_receiver;

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

	@Override
	protected void onResume() {
		register_bt_receiver();
		super.onResume();
	}

	@Override
	protected void onPause() {
		unregister_bt_receiver();
		super.onPause();
	}

	private void not_support_bt() {
		Log.i(TAG, "블루투스 미지원 기기");
	}

	private void support_bt() {
		Log.i(TAG, "블루투스 지원 기기");
		bt_receiver = new BT_Receiver();
	}

	private void register_bt_receiver() {
		if (bt_receiver == null) return;
		IntentFilter filter = new IntentFilter();

		filter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);

		filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
		filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
		filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
		filter.addAction(BluetoothDevice.ACTION_FOUND);

		registerReceiver(bt_receiver, filter);
	}

	private void unregister_bt_receiver() {
		if (bt_receiver == null) return;
		unregisterReceiver(bt_receiver);
	}

	private class BT_Receiver extends BroadcastReceiver {
		private final String TAG_RCV = "DEV_BT_RCV";

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			// 연결 상태 변화
			if (BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED.equals(action)) {
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				int conn = intent.getIntExtra(BluetoothAdapter.EXTRA_CONNECTION_STATE, -1);
				int conn_pre = intent.getIntExtra(BluetoothAdapter.EXTRA_PREVIOUS_CONNECTION_STATE, -1);
				Log.i(TAG_RCV, "CONNECTION | addr: " + device.getAddress() + " | " +
				               get_adapter_str(conn_pre) + " > " + get_adapter_str(conn));
			}

			// 원격 기기 검색 종료
			else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
				Log.i(TAG_RCV, "ACTION_DISCOVERY_FINISHED");
			}

			// 원격 기기 검색 시작
			else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
				Log.i(TAG_RCV, "ACTION_DISCOVERY_STARTED");
			}

			// 로컬 기기 활성 상태 변화
			else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
				int power = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
				int power_pre = intent.getIntExtra(BluetoothAdapter.EXTRA_PREVIOUS_STATE, -1);

				Log.i(TAG_RCV, "STATE | " + get_adapter_str(power_pre) + " > " + get_adapter_str(power));
			}

			// ACL 연결 생성
			else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
				Log.i(TAG_RCV, "ACL_CONNECTED");
			}

			// ACL 연결 종료
			else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
				Log.i(TAG_RCV, "ACL_DISCONNECTED");
			}

			// 원격 기기 페어링 상태 변화
			else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				int bond = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, -1);
				int bond_pre = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, -1);

				Log.i(TAG_RCV, "BOND | addr: " + device.getAddress() + " | " +
				               get_device_str(bond_pre) + " > " + get_device_str(bond));
			}

			// 원격 기기 발견
			else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				BluetoothClass device_class = intent.getParcelableExtra(BluetoothDevice.EXTRA_CLASS);

				Log.i(TAG_RCV, "FOUND | addr: " + device.getAddress() + " | class: " + device_class);
			}
		}

		private String get_adapter_str(int num) {
			if (num == BluetoothAdapter.STATE_DISCONNECTED) return "DISCONNECTED";
			if (num == BluetoothAdapter.STATE_CONNECTING) return "CONNECTING";
			if (num == BluetoothAdapter.STATE_CONNECTED) return "CONNECTED";
			if (num == BluetoothAdapter.STATE_DISCONNECTING) return "DISCONNECTING";

			if (num == BluetoothAdapter.STATE_OFF) return "OFF";
			if (num == BluetoothAdapter.STATE_TURNING_ON) return "TURNING ON";
			if (num == BluetoothAdapter.STATE_ON) return "ON";
			if (num == BluetoothAdapter.STATE_TURNING_OFF) return "TURNING OFF";

			return "NULL";
		}

		private String get_device_str(int num) {
			if (num == BluetoothDevice.BOND_NONE) return "BOND_NONE";
			if (num == BluetoothDevice.BOND_BONDING) return "BOND_BONDING";
			if (num == BluetoothDevice.BOND_BONDED) return "BOND_BONDED";

			return "NULL";
		}
	}
}