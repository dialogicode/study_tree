package tree.study.base;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

import tree.study.tool.PermissionTool;

public class BaseActivity extends AppCompatActivity {
	protected final String TAG_PERM = "DEV_PERM_ACT";
	protected PermissionTool perm_tool;
	protected String[] permissions;
	private final ActivityResultLauncher<String> launcher_request_permission = registerForActivityResult(
			new ActivityResultContracts.RequestPermission(),
			this::launcher_callback_permission);
	private final ActivityResultLauncher<String[]> launcher_request_permissions = registerForActivityResult(
			new ActivityResultContracts.RequestMultiplePermissions(),
			this::launcher_callback_permissions);

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		perm_tool = new PermissionTool(getApplicationContext());
	}

	protected final boolean check_permission(String[] permission_list) {
		permissions = perm_tool.get_not_granted_permission(permission_list);
		if (permissions.length == 0) return true;
		for (String perm : permissions) Log.i(TAG_PERM, "미허용 권한 > " + perm);
		return false;
	}

	protected final void request_permission(@NonNull String[] permission_list) {
		if (permission_list.length == 1) {
			Log.i(TAG_PERM, permission_list[0] + " 권한 요청");
			launcher_request_permission.launch(permission_list[0]);
		} else if (permission_list.length > 1) {
			launcher_request_permissions.launch(permission_list);
		}
	}

	protected void launcher_callback_permission(Boolean granted) {
		Log.i(TAG_PERM, (granted ? "권한 요청 승인" : "권한 요청 거부"));
	}

	protected void launcher_callback_permissions(Map<String, Boolean> result) {
		result.forEach((perm, granted) -> {
			Log.i(TAG_PERM,perm + (granted ? " 권한 요청 승인" : " 권한 요청 거부"));
		});
	}

	protected final boolean check_app_runtime_permissions() {
		Log.i(TAG_PERM, "check_app_runtime_permissions");
		return check_permission(perm_tool.get_app_runtime_permissions());
	}
}