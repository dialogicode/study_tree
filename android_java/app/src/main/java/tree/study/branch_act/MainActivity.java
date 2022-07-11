package tree.study.branch_act;

import android.os.Bundle;

import tree.study.base.BaseActivity;
import tree.study.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity {
	private ActivityMainBinding bind;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bind = ActivityMainBinding.inflate(getLayoutInflater());
		setContentView(bind.getRoot());
	}
}