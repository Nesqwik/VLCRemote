package fr.nesqwik.vlcremote.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.vlcremote.R;

import fr.nesqwik.vlcremote.activities.RemoteActivity;
import fr.nesqwik.vlcremote.bean.VLCPlaylistTree;
import fr.nesqwik.vlcremote.bean.VLCStatus;
import fr.nesqwik.vlcremote.utils.TimeConvertor;

public class ImageListAdapter extends BaseAdapter {

	private RemoteActivity activity;
	private LayoutInflater mLayoutInflater;
	private VLCPlaylistTree mEntries = new VLCPlaylistTree();

	public ImageListAdapter(RemoteActivity activity) {   
		this.activity = activity;
		mLayoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}


	public VLCPlaylistTree getTree() {
		return mEntries;
	}
	@Override
	public int getCount() {
		return mEntries.childrenCount();
	}

	@Override
	public Object getItem(int position) {
		return mEntries.getChildren().get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RelativeLayout itemView;
		if (convertView == null) {                                        
			itemView = (RelativeLayout) mLayoutInflater.inflate(R.layout.list_playlist, parent, false);

		} else {
			itemView = (RelativeLayout) convertView;
		}

		TextView tv_title_artwork = (TextView) itemView.findViewById(R.id.tv_title_artwork);                        
		TextView tv_subtitle_artwork = (TextView) itemView.findViewById(R.id.tv_subtitle_artwork);


		String title = mEntries.getChildren().get(position).getName();
		tv_title_artwork.setText(title);
		String description = TimeConvertor.getTime(mEntries.getChildren().get(position).getDuration()).toString();
		tv_subtitle_artwork.setText(description);

		return itemView;
	}

	public void updateEntries(VLCPlaylistTree entries) {
		setEntries(entries);
		notifyDataSetChanged();
	}

	public void backToParent() {
		if(mEntries.getFather() != null) {
			mEntries = mEntries.getFather();
			notifyDataSetChanged();
		}
	}

	private void setEntries(VLCPlaylistTree entries) {
		mEntries = entries;
		if((mEntries.getChildren().size() == 0 || !mEntries.getChildren().get(0).getType().equals("back")) && mEntries.getId() != 1) {
			VLCPlaylistTree goBackChild = new VLCPlaylistTree();
			goBackChild.setName("..");
			goBackChild.setType("back");
			mEntries.getChildren().add(0, goBackChild);
		}
	}

	public void setNewTree(VLCPlaylistTree tree) {
		VLCPlaylistTree father = mEntries;
		setEntries(tree);
		mEntries.setFather(father);
		
		if(activity.getLastStatus().getState().equals(VLCStatus.STATE_STOP))
			activity.setTitle(tree.getName());
		notifyDataSetChanged();
	}

}
