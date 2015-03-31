package fr.nesqwik.vlcremote.network;

/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;
import fr.nesqwik.vlcremote.MainActivity;
import fr.nesqwik.vlcremote.bean.VLCService;
public class NsdHelper {
	MainActivity mContext;
	NsdManager mNsdManager;
	NsdManager.ResolveListener mResolveListener;
	NsdManager.DiscoveryListener mDiscoveryListener;
	public static final String SERVICE_TYPE = "_nvstream._tcp.";
	public static final String TAG = "NsdHelper";
	public String mServiceName = "NsdChat";
	List<NsdServiceInfo> mServices;
	public NsdHelper(MainActivity context) {
		mServices = new ArrayList<NsdServiceInfo>();
		mContext = context;
		mNsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
		initializeNsd();
	}
	public void initializeNsd() {
		initializeResolveListener();
		//mNsdManager.init(mContext.getMainLooper(), this);
	}
	public void initializeDiscoveryListener() {
		mDiscoveryListener = new NsdManager.DiscoveryListener() {
			@Override
			public void onDiscoveryStarted(String regType) {
				Log.d(TAG, "Service discovery started");
			}
			@Override
			public void onServiceFound(NsdServiceInfo service) {
				Log.d(TAG, "Service discovery success" + service + ":" + service.getServiceName());
				if (!service.getServiceType().equals(SERVICE_TYPE)) {
					Log.d(TAG, "Unknown Service Type: " + service.getServiceType());
				} else {
					Log.d(TAG, "Resolving");
					mNsdManager.resolveService(service, mResolveListener);
				}
			}
			@Override
			public void onServiceLost(NsdServiceInfo service) {
				Log.e(TAG, "service lost" + service);
				mServices.remove(service);
			}
			@Override
			public void onDiscoveryStopped(String serviceType) {
				Log.i(TAG, "Discovery stopped: " + serviceType);
			}
			@Override
			public void onStartDiscoveryFailed(String serviceType, int errorCode) {
				Log.e(TAG, "Discovery failed: Error code:" + errorCode);
			}
			@Override
			public void onStopDiscoveryFailed(String serviceType, int errorCode) {
				Log.e(TAG, "Discovery failed: Error code:" + errorCode);
			}
		};
	}
	public void initializeResolveListener() {
		mResolveListener = new NsdManager.ResolveListener() {
			@Override
			public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
				Log.e(TAG, "Resolve failed" + errorCode);
			}
			@Override
			public void onServiceResolved(NsdServiceInfo serviceInfo) {
				Log.d(TAG, "Resolve Succeeded. " + serviceInfo);
				mServices.add(serviceInfo);
				
				
				List<VLCService> services = new ArrayList<VLCService>();
				for(NsdServiceInfo info : mServices) {
					services.add(new VLCService(info.getHost().getHostAddress(), info.getServiceName()));
				}
				
				NsdHelper.this.mContext.updateStatus(services);
			}
		};
	}


	public void discoverServices() {
		stopDiscovery();  // Cancel any existing discovery request
		initializeDiscoveryListener();
		mNsdManager.discoverServices(
				SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);
	}
	public void stopDiscovery() {
		if (mDiscoveryListener != null) {
			try {
				mNsdManager.stopServiceDiscovery(mDiscoveryListener);
			} finally {
			}
			mDiscoveryListener = null;
		}
	}
	public List<NsdServiceInfo> getChosenServiceInfo() {
		return mServices;
	}
}