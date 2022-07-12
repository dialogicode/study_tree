package tree.study.branch_act;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tree.study.R;
import tree.study.base.BaseActivity;
import tree.study.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity {
	private ActivityMainBinding bind;
	private ArrayList<MainRecyclerItem> recycler_data;
	private MainRecyclerAdapter recycler_adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bind = ActivityMainBinding.inflate(getLayoutInflater());
		setContentView(bind.getRoot());

		recycler_data = new ArrayList<>();
		recycler_data.add(new MainRecyclerItem("Architecture", ArchitectureActivity.class));
		recycler_data.add(new MainRecyclerItem("Location", LocationActivity.class));
		recycler_data.add(new MainRecyclerItem("Bluetooth", BluetoothActivity.class));

		recycler_adapter = new MainRecyclerAdapter(recycler_data);

		bind.recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
		bind.recycler.setAdapter(recycler_adapter);
	}

	private class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerViewHolder> {
		private ArrayList<MainRecyclerItem> data;

		public MainRecyclerAdapter(ArrayList<MainRecyclerItem> data) {
			this.data = data;
		}

		@NonNull
		@Override
		public MainRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false);
			return new MainRecyclerViewHolder(view);
		}

		@Override
		public void onBindViewHolder(@NonNull MainRecyclerViewHolder holder, int position) {
			MainRecyclerItem item = data.get(position);
			holder.subject.setText(item.subject_name);
			holder.subject.setOnClickListener(v ->  {
				Intent intent = new Intent(MainActivity.this, item.subject_class);
				startActivity(intent);
			});
		}

		@Override
		public int getItemCount() {
			return data.size();
		}
	}

	private class MainRecyclerViewHolder extends RecyclerView.ViewHolder {
		private Button subject;

		public MainRecyclerViewHolder(@NonNull View itemView) {
			super(itemView);
			subject = itemView.findViewById(R.id.subject);
		}
	}

	private class MainRecyclerItem {
		private String subject_name;
		private Class<?> subject_class;

		public MainRecyclerItem(String subject_name, Class<?> subject_class) {
			this.subject_name = subject_name;
			this.subject_class = subject_class;
		}
	}
}