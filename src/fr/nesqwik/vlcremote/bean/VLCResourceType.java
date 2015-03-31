package fr.nesqwik.vlcremote.bean;

public class VLCResourceType<T> {
	private String url;
	private Class<T> type;
	
	
	public VLCResourceType(String url, Class<T> type) {
		this.type = type;
		this.url = url;
	}
	
	public Class<T> getType() {
		return type;
	}
	public void setType(Class<T> type) {
		this.type = type;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
