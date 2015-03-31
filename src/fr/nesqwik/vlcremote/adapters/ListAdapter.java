package fr.nesqwik.vlcremote.adapters;

import java.util.ArrayList;
import java.util.List;

import com.example.vlcremote.R;

import fr.nesqwik.vlcremote.bean.VLCService;
import android.content.Context;
import android.net.nsd.NsdServiceInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter {

	
	 
     private LayoutInflater mLayoutInflater;                              

     private List<VLCService> mEntries = new ArrayList<VLCService>();          

     public ListAdapter(Context context) {                           
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
     }

     @Override
     public int getCount() {
        return mEntries.size();
     }

     @Override
     public Object getItem(int position) {
        return mEntries.get(position);
     }

     @Override
     public long getItemId(int position) {
        return position;
     }

     @Override
     public View getView(int position, View convertView, ViewGroup parent) {                                           
        RelativeLayout itemView;
        if (convertView == null) {                                        
           itemView = (RelativeLayout) mLayoutInflater.inflate(R.layout.list_services, parent, false);

        } else {
           itemView = (RelativeLayout) convertView;
        }
                  
        TextView titleText = (TextView)
           itemView.findViewById(R.id.tv_title_service);                        
        TextView descriptionText = (TextView)
           itemView.findViewById(R.id.tv_subtitle_service);                  
        

        String title = mEntries.get(position).getName();
        titleText.setText(title);
        String description = mEntries.get(position).getIp();
        descriptionText.setText(description);

        return itemView;
     }

     public void updateEntries(List<VLCService> entries) {
        mEntries = entries;
        notifyDataSetChanged();
     }
}
