package tree.study.branch_act;

import android.os.Bundle;

import tree.study.base.BaseActivity;
import tree.study.databinding.ActivityArchitectureBinding;

public class ArchitectureActivity extends BaseActivity {
	private ActivityArchitectureBinding bind;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bind = ActivityArchitectureBinding.inflate(getLayoutInflater());
		setContentView(bind.getRoot());
	}
}