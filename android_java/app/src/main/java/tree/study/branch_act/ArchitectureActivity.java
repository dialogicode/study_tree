package tree.study.branch_act;

import android.os.Bundle;
import android.view.View;

import tree.study.base.BaseActivity;
import tree.study.databinding.ActivityArchitectureBinding;

public class ArchitectureActivity extends BaseActivity {
	private ActivityArchitectureBinding bind;
	private int count;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bind = ActivityArchitectureBinding.inflate(getLayoutInflater());
		setContentView(bind.getRoot());

		bind.show.setText(count + "");
		bind.plus.setOnClickListener(this::click_plus);
		bind.minus.setOnClickListener(this::click_minus);
	}

	private void click_plus(View view) {
		count++;
		bind.show.setText(count + "");
	}

	private void click_minus(View view) {
		count--;
		bind.show.setText(count + "");
	}
}