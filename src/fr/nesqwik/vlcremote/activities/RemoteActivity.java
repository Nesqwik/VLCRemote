package fr.nesqwik.vlcremote.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.vlcremote.R;

import fr.nesqwik.vlcremote.MainActivity;
import fr.nesqwik.vlcremote.adapters.ImageListAdapter;
import fr.nesqwik.vlcremote.adapters.ListAdapter;
import fr.nesqwik.vlcremote.bean.VLCPlaylistTree;
import fr.nesqwik.vlcremote.bean.VLCResponse;
import fr.nesqwik.vlcremote.bean.VLCStatus;
import fr.nesqwik.vlcremote.network.IPScanner;
import fr.nesqwik.vlcremote.network.VLCController;
import fr.nesqwik.vlcremote.network.VLCHTTPClient;
import fr.nesqwik.vlcremote.utils.TimeConvertor;

public class RemoteActivity extends ActionBarActivity implements Updatable {
	private SeekBar sb_position;
	private SeekBar sb_volume;
	private TextView tv_time;
	private TextView tv_length;
	private TextView tv_remains;
	private Handler m_handler;
	private ImageButton b_togglePause;
	private ListView lv_playlist;
	private ImageListAdapter imageListAdapter;

	private VLCStatus lastStatus;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_remote);

		findViews();
		initLists();
		initListeners();

		VLCHTTPClient.getInstance().setServerIp(getIntent().getExtras().getString("ip"));

		m_handler = new Handler();
		m_statusChecker.run();

		VLCController controller = new VLCController(this);
		controller.execute("get_playlist");
	}

	private void initLists() {
		imageListAdapter = new ImageListAdapter(this);
		lv_playlist.setAdapter(imageListAdapter);
	}

	Runnable m_statusChecker = new Runnable() {
		@Override 
		public void run() {
			new VLCController(RemoteActivity.this).execute("get_status");
			m_handler.postDelayed(m_statusChecker, 1000);
		}
	};

	private void findViews() {
		sb_position = (SeekBar) findViewById(R.id.sb_position);
		sb_volume = (SeekBar) findViewById(R.id.sb_volume);
		sb_volume.setMax(VLCStatus.MAX_VOLUME);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_length = (TextView) findViewById(R.id.tv_length);
		tv_remains = (TextView) findViewById(R.id.tv_remains);
		b_togglePause = (ImageButton) findViewById(R.id.b_togglePause);
		lv_playlist = (ListView) findViewById(R.id.lv_playlist);
	}

	private void initListeners() {
		sb_position.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if(fromUser) {
					VLCController controller = new VLCController(RemoteActivity.this);
					controller.setUpdateStatus(false);
					controller.execute("seek", ""+progress);
				}
			}
		});

		sb_volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if(fromUser) {
					VLCController controller = new VLCController(RemoteActivity.this);
					controller.setUpdateStatus(false);
					controller.execute("volume", ""+progress);
				}
			}
		});

		lv_playlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				VLCPlaylistTree touchedTree = imageListAdapter.getTree().getChildren().get(position);
				Log.d("REMOTE_ACTIVITY", "name=" + touchedTree.getName() + ":id=" + touchedTree.getId());
				if(touchedTree.getType().equals("back")) {
					imageListAdapter.backToParent();
				} else {
					if(touchedTree.getType().equals("node")) {
						imageListAdapter.setNewTree(touchedTree);
					} else {
						VLCController controller = new VLCController(RemoteActivity.this);
						controller.execute("play_id", ""+touchedTree.getId());
					}
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		//m_statusChecker.run();
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_fullscreen) {
			VLCController controller = new VLCController(this);
			controller.execute("toggle_fullscreen");
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void doTogglePause(View v) {
		VLCController controller = new VLCController(this);
		controller.execute("toggle_pause");
	}

	public void doStop(View v) {
		VLCController controller = new VLCController(this);
		controller.execute("stop");
	}

	public void doNextTrack(View v) {
		VLCController controller = new VLCController(this);
		controller.execute("next_track");
	}

	public void doPreviousTrack(View v) {
		VLCController controller = new VLCController(this);
		controller.execute("previous_track");
	}

	public void doTreeBack(View v) {
		imageListAdapter.backToParent();
	}

	public void updateStatus(VLCResponse response) {
		if(response instanceof VLCStatus) {
			final VLCStatus status = (VLCStatus) response;
			lastStatus = status;
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					sb_position.setMax(status.getLength());
					sb_position.setProgress(status.getTime());
					sb_volume.setProgress(status.getVolume());
					tv_length.setText(TimeConvertor.getTime(status.getLength()).toString());
					tv_time.setText(TimeConvertor.getTime(status.getTime()).toString());
					tv_remains.setText(TimeConvertor.getTime(status.getLength() - status.getTime()).toString());

					if(status.getState().equals(VLCStatus.STATE_STOP)) {
						RemoteActivity.this.setTitle(imageListAdapter.getTree().getName());
					} else {
						if(status.getState().equals(VLCStatus.STATE_PAUSE))
							b_togglePause.setImageResource(R.drawable.ic_play);
						else if(status.getState().equals(VLCStatus.STATE_PLAY))
							b_togglePause.setImageResource(R.drawable.ic_pause);
						if(status.getInformation() != null)
							RemoteActivity.this.setTitle(status.getInformation().getCategory().getMeta().getFilename());
					}
				}
			});
		} else if(response instanceof VLCPlaylistTree) {
			final VLCPlaylistTree playlist = (VLCPlaylistTree) response;
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					imageListAdapter.updateEntries(playlist);
				}
			});
		}
	}

	public VLCStatus getLastStatus() {
		return lastStatus;
	}
}
