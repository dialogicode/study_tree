package tree.study.branch_act;

import android.os.Bundle;

import tree.study.base.BaseActivity;
import tree.study.databinding.ActivityLocationBinding;

public class LocationActivity extends BaseActivity {
	private ActivityLocationBinding bind;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bind = ActivityLocationBinding.inflate(getLayoutInflater());
		setContentView(bind.getRoot());
	}
}