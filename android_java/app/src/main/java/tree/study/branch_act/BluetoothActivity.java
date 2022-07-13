package tree.study.branch_act;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import tree.study.base.BaseActivity;
import tree.study.branch_vm.BluetoothViewModel;
import tree.study.databinding.ActivityBluetoothBinding;

public class BluetoothActivity extends BaseActivity {
	private final String TAG = "DEV_BT_ACT";
	private BluetoothViewModel vm;
	private ActivityBluetoothBinding bind;
	private BluetoothAdapter bt_adapter;
	private BT_Receiver bt_receiver;
	private ActivityResultLauncher<Intent> launcher_bt_on;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		vm = new ViewModelProvider(this).get(BluetoothViewModel.class);
		bind = ActivityBluetoothBinding.inflate(getLayoutInflater());
		setContentView(bind.getRoot());

		vm.getPower().observe(this, nextAction -> bind.btBtn1.setText(nextAction));
		vm.getFind().observe(this, nextAction -> bind.btBtn2.setText(nextAction));

		bind.btBtn1.setOnClickListener(this::click_bt_btn_1);
		bind.btBtn2.setOnClickListener(this::click_bt_btn_2);

		if (!check_permission(perm_tool.get_bluetooth_permissions()))
			request_permission(permissions);

		bt_adapter = BluetoothAdapter.getDefaultAdapter();

		if (bt_adapter == null) not_support_bt();
		else support_bt();
	}

	@Override
	protected void onResume() {
		ready_next_action();
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
		launcher_bt_on = registerForActivityResult(
				new ActivityResultContracts.StartActivityForResult(),
				this::launcher_callback_bt_on);
	}

	private void launcher_callback_bt_on(ActivityResult result) {
		int resultCode = result.getResultCode();
		if (resultCode == RESULT_OK) {
			Log.i(TAG, "사용자 블루투스 활성화 승인");
			vm.getPower().setValue("OFF");
		} else if (resultCode == RESULT_CANCELED) {
			Log.i(TAG, "사용자 블루투스 활성화 거부");
			vm.getPower().setValue("ON");
		} else {
			ready_next_action();
		}
	}

	@SuppressLint("MissingPermission")
	private void ready_next_action() {
		if (bt_adapter == null) {
			vm.getPower().setValue("NOT");
			vm.getFind().setValue("NOT");
		} else {
			vm.getPower().setValue(bt_adapter.isEnabled() ? "OFF" : "ON");
			vm.getFind().setValue(bt_adapter.isDiscovering() ? "STOP" : "FIND");
		}
	}

	private void click_bt_btn_1(View view) {
		String next_action = vm.getPower().getValue();

		if ("NOT".equals(next_action)) return;
		if ("ON".equals(next_action)) bt_power_on_user();
		else if ("OFF".equals(next_action)) bt_power_off();
		else ready_next_action();
	}

	private void click_bt_btn_2(View view) {
		String next_action = vm.getFind().getValue();

		if ("NOT".equals(next_action)) return;
		if ("FIND".equals(next_action)) bt_find_start();
		else if ("STOP".equals(next_action)) bt_find_stop();
		else ready_next_action();
	}

	private void bt_power_on_code() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
		    ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
			request_permission(new String[] { Manifest.permission.BLUETOOTH_CONNECT });
			return;
		}
		if (bt_adapter.isEnabled()) return;
		bt_adapter.enable();
	}

	private void bt_power_on_user() {
		if (bt_adapter.isEnabled()) return;
		vm.getPower().setValue("~");
		launcher_bt_on.launch(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE));
	}

	private void bt_power_off() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
		    ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
			request_permission(new String[] { Manifest.permission.BLUETOOTH_CONNECT });
			return;
		}
		if (!bt_adapter.isEnabled()) return;
		bt_adapter.disable();
	}

	private void bt_find_start() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
		    ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
			request_permission(new String[] { Manifest.permission.BLUETOOTH_SCAN });
			return;
		}
		if (bt_adapter.isDiscovering()) return;
		bt_adapter.startDiscovery();
	}

	private void bt_find_stop() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
		    ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
			request_permission(new String[] { Manifest.permission.BLUETOOTH_SCAN });
			return;
		}
		if (!bt_adapter.isDiscovering()) return;
		bt_adapter.cancelDiscovery();
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
				Log.i(TAG_RCV, "DISCOVERY | FINISHED");
				vm.getFind().setValue("FIND");
			}

			// 원격 기기 검색 시작
			else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
				Log.i(TAG_RCV, "DISCOVERY | STARTED");
				vm.getFind().setValue("STOP");
			}

			// 로컬 기기 활성 상태 변화
			else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
				int power = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
				int power_pre = intent.getIntExtra(BluetoothAdapter.EXTRA_PREVIOUS_STATE, -1);

				Log.i(TAG_RCV, "STATE | " + get_adapter_str(power_pre) + " > " + get_adapter_str(power));

				if (power == BluetoothAdapter.STATE_OFF) vm.getPower().setValue("ON");
				else if (power == BluetoothAdapter.STATE_ON) vm.getPower().setValue("OFF");
				else vm.getPower().setValue("~");
			}

			// ACL 연결 생성
			else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
				Log.i(TAG_RCV, "ACL | CONNECTED");
			}

			// ACL 연결 종료
			else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
				Log.i(TAG_RCV, "ACL | DISCONNECTED");
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