package tree.study.branch_vm;

import androidx.lifecycle.MutableLiveData;

import tree.study.base.BaseViewModel;

public class BluetoothViewModel extends BaseViewModel {
	private MutableLiveData<String> power;
	private MutableLiveData<String> find;

	public MutableLiveData<String> getPower() {
		if (power == null) {
			power = new MutableLiveData<>();
		}
		return power;
	}

	public MutableLiveData<String> getFind() {
		if (find == null) {
			find = new MutableLiveData<>();
		}
		return find;
	}
}
