package fr.nesqwik.vlcremote.bean;

import java.util.ArrayList;
import java.util.List;

public class VLCPlaylistTree extends VLCResponse {
	private String ro;
	private String type;
	private String name;
	private int duration;
	private String uri;
	private int id;
	private List<VLCPlaylistTree> children = new ArrayList<VLCPlaylistTree>();
	private VLCPlaylistTree father;
	
	public String getRo() {
		return ro;
	}
	public void setRo(String ro) {
		this.ro = ro;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public List<VLCPlaylistTree> getChildren() {
		return children;
	}
	public void setChildren(List<VLCPlaylistTree> children) {
		this.children = children;
	}
	public int childrenCount() {
		return children.size();
	}
	public VLCPlaylistTree getFather() {
		return father;
	}
	public void setFather(VLCPlaylistTree father) {
		this.father = father;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	
	public String toString() {
		String res = this.name;
		if(children != null) {
			for(VLCPlaylistTree child : children) {
				res += "\n" + child.toString();
			}
		}
		return res;
	}
}
