package tree.study.branch_vm;

import androidx.lifecycle.MutableLiveData;

import tree.study.base.BaseViewModel;

public class ArchitectureViewModel extends BaseViewModel {
	public MutableLiveData<Integer> count = new MutableLiveData<>();

	public ArchitectureViewModel() {
		this.count.setValue(0);
	}

	public void increase() {
		count.setValue(count.getValue() + 1);
	}

	public void decrease() {
		count.setValue(count.getValue() - 1);
	}
}
