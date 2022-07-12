package tree.study.branch_act;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import tree.study.base.BaseActivity;
import tree.study.branch_vm.ArchitectureViewModel;
import tree.study.databinding.ActivityArchitectureBinding;

public class ArchitectureActivity extends BaseActivity {
	private ArchitectureViewModel vm;
	private ActivityArchitectureBinding bind;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		vm = new ViewModelProvider(this).get(ArchitectureViewModel.class);
		bind = ActivityArchitectureBinding.inflate(getLayoutInflater());
		setContentView(bind.getRoot());

		bind.show.setText(vm.count + "");
		bind.plus.setOnClickListener(this::click_plus);
		bind.minus.setOnClickListener(this::click_minus);
	}

	private void click_plus(View view) {
		vm.count++;
		bind.show.setText(vm.count + "");
	}

	private void click_minus(View view) {
		vm.count--;
		bind.show.setText(vm.count + "");
	}
}