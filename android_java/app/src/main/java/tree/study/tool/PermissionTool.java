package tree.study.tool;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;

import tree.study.base.BaseTool;

public class PermissionTool extends BaseTool {
	private final Context context;

	public PermissionTool(Context context) {
		this.context = context;
	}

	public String[] get_app_runtime_permissions() {
		ArrayList<String> list = new ArrayList<>();

		list.add(Manifest.permission.ACCESS_COARSE_LOCATION);
		list.add(Manifest.permission.ACCESS_FINE_LOCATION);
		list.add(Manifest.permission.RECORD_AUDIO);
		list.add(Manifest.permission.CAMERA);
		list.add(Manifest.permission.READ_CALENDAR);
		list.add(Manifest.permission.READ_CONTACTS);
		list.add(Manifest.permission.READ_SMS);
		list.add(Manifest.permission.RECEIVE_SMS);
		list.add(Manifest.permission.RECORD_AUDIO);
		list.add(Manifest.permission.SEND_SMS);
		list.add(Manifest.permission.WRITE_CALENDAR);
		list.add(Manifest.permission.WRITE_CONTACTS);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			list.add(Manifest.permission.READ_CALL_LOG);
			list.add(Manifest.permission.WRITE_CALL_LOG);
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT && Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
			list.add(Manifest.permission.READ_EXTERNAL_STORAGE);
			list.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
			list.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION);
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
			list.add(Manifest.permission.BLUETOOTH_ADVERTISE);
			list.add(Manifest.permission.BLUETOOTH_CONNECT);
			list.add(Manifest.permission.BLUETOOTH_SCAN);
		}

		return strListToArray(list);
	}

	public String[] get_bluetooth_permissions() {
		ArrayList<String> list = new ArrayList<>();

		list.add(Manifest.permission.ACCESS_COARSE_LOCATION);
		list.add(Manifest.permission.ACCESS_FINE_LOCATION);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
			list.add(Manifest.permission.BLUETOOTH_ADVERTISE);
			list.add(Manifest.permission.BLUETOOTH_CONNECT);
			list.add(Manifest.permission.BLUETOOTH_SCAN);
		}

		return strListToArray(list);
	}

	public String[] get_location_permissions() {
		ArrayList<String> list = new ArrayList<>();

		list.add(Manifest.permission.ACCESS_COARSE_LOCATION);
		list.add(Manifest.permission.ACCESS_FINE_LOCATION);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
			list.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION);
		}

		return strListToArray(list);
	}

	public String[] get_fore_location_permissions() {
		ArrayList<String> list = new ArrayList<>();

		list.add(Manifest.permission.ACCESS_COARSE_LOCATION);
		list.add(Manifest.permission.ACCESS_FINE_LOCATION);

		return strListToArray(list);
	}

	public String[] get_back_location_permissions() {
		ArrayList<String> list = new ArrayList<>();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
			list.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION);
		}

		return strListToArray(list);
	}

	private boolean is_granted_permission(String permission) {
		return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
	}

	public final String[] get_not_granted_permission(String[] permission_array) {
		ArrayList<String> list = new ArrayList<>();
		for (String permission : permission_array) {
			if (!is_granted_permission(permission)) list.add(permission);
		}
		return strListToArray(list);
	}
}