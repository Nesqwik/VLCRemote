package fr.nesqwik.vlcremote.network;

import java.util.HashMap;

import android.os.AsyncTask;
import fr.nesqwik.vlcremote.activities.Updatable;
import fr.nesqwik.vlcremote.bean.VLCPlaylistTree;
import fr.nesqwik.vlcremote.bean.VLCResourceType;
import fr.nesqwik.vlcremote.bean.VLCResponse;
import fr.nesqwik.vlcremote.bean.VLCStatus;

public class VLCController extends AsyncTask<String, Void, String> {

	private Updatable activity;
	private HashMap<String, VLCResourceType<? extends VLCResponse>> commands = new HashMap<String, VLCResourceType<? extends VLCResponse>>();
	private boolean updateStatus = true;

	public VLCController(Updatable activity) {
		commands.put("get_playlist", new VLCResourceType<VLCPlaylistTree>("playlist.json", VLCPlaylistTree.class));
		commands.put("get_status",  new VLCResourceType<VLCStatus>("status.json", VLCStatus.class));
		commands.put("toggle_pause", new VLCResourceType<VLCStatus>("status.json?command=pl_pause", VLCStatus.class));
		commands.put("next_track", new VLCResourceType<VLCStatus>("status.json?command=pl_next", VLCStatus.class));
		commands.put("previous_track", new VLCResourceType<VLCStatus>("status.json?command=pl_previous", VLCStatus.class));
		commands.put("stop", new VLCResourceType<VLCStatus>("status.json?command=pl_stop", VLCStatus.class));
		commands.put("play_id", new VLCResourceType<VLCStatus>("status.json?command=pl_play&id=", VLCStatus.class));
		commands.put("seek", new VLCResourceType<VLCStatus>("status.json?command=seek&val=", VLCStatus.class));
		commands.put("volume", new VLCResourceType<VLCStatus>("status.json?command=volume&val=", VLCStatus.class));
		commands.put("toggle_fullscreen", new VLCResourceType<VLCStatus>("status.json?command=fullscreen", VLCStatus.class));
		this.activity = activity;
	}

	@Override
	protected String doInBackground(String... resources) {
		VLCHTTPClient client = VLCHTTPClient.getInstance();
		VLCResponse status = null;

		if(resources.length == 1)
			status = client.httpGET(commands.get(resources[0]).getUrl(), commands.get(resources[0]).getType());

		else if(resources.length == 2)
			status = client.httpGET(commands.get(resources[0]).getUrl() + resources[1], commands.get(resources[0]).getType());

		
		if(updateStatus && status != null) {
			activity.updateStatus(status);
		}
		return "OK";
	}

	public boolean isUpdateStatus() {
		return updateStatus;
	}

	public void setUpdateStatus(boolean updateStatus) {
		this.updateStatus = updateStatus;
	}
}
