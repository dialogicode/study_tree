package tree.study.branch_vm;

import androidx.lifecycle.MutableLiveData;

import tree.study.base.BaseViewModel;

public class ArchitectureViewModel extends BaseViewModel {
	private MutableLiveData<Integer> count;

	public ArchitectureViewModel() {
		getCount().setValue(0);
	}

	public MutableLiveData<Integer> getCount() {
		if (count == null) {
			count = new MutableLiveData<>();
		}
		return count;
	}

	public void increase() {
		getCount().setValue(getCount().getValue() + 1);
	}

	public void decrease() {
		getCount().setValue(getCount().getValue() - 1);
	}
}
