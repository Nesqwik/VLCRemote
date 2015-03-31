package fr.nesqwik.vlcremote.network;

import android.os.AsyncTask;
import android.util.Log;
import fr.nesqwik.vlcremote.MainActivity;

public class IPScanner extends AsyncTask<Void, Void, Void>{
	
	//private JmDNSHelper mJmDNSHelper;
	private NsdHelper mNsdHelper;
	
	public IPScanner(MainActivity activity) {
		//mJmDNSHelper = new JmDNSHelper();
		mNsdHelper = new NsdHelper(activity);
	}

	@Override
	protected Void doInBackground(Void... params) {
		Log.d("IPSCANNER", "STARTING");
		//mJmDNSHelper.start();
		mNsdHelper.discoverServices();
		Log.d("IPSCANNER", "ENDED");
		return null;
	}
}