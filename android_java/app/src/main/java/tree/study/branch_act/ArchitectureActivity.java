package tree.study.branch_act;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import tree.study.R;
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
		bind = DataBindingUtil.setContentView(this, R.layout.activity_architecture);
		bind.setLifecycleOwner(this);
		bind.setVm(vm);
	}
}