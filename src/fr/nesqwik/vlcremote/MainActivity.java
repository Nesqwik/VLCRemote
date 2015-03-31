package fr.nesqwik.vlcremote;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.vlcremote.R;

import fr.nesqwik.vlcremote.activities.RemoteActivity;
import fr.nesqwik.vlcremote.adapters.ListAdapter;
import fr.nesqwik.vlcremote.bean.VLCService;
import fr.nesqwik.vlcremote.network.IPScanner;

public class MainActivity extends ListActivity {

	private ListAdapter adapter;
	private List<VLCService> mServices;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		
		Intent intent = new Intent(this, RemoteActivity.class);
		intent.putExtra("ip", "192.168.200.169");
		startActivity(intent);
		
		
		adapter = new ListAdapter(this);
		setListAdapter(adapter);

		IPScanner ipScanner = new IPScanner(this);
		ipScanner.execute();
	}
	
	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(v.getContext(), RemoteActivity.class);
		intent.putExtra("ip", mServices.get(position).getIp());
		intent.putExtra("name", mServices.get(position).getName());
		startActivity(intent);
    }

	public void updateStatus(List<VLCService> mServices) {
		adapter.updateEntries(mServices);
		this.mServices = mServices;
	}


}
