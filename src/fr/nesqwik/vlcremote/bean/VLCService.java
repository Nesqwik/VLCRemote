package fr.nesqwik.vlcremote.bean;

public class VLCService {
	private String ip;
	private String name;
	
	public VLCService(String ip, String name) {
		this.ip = ip;
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
}
