package fr.nesqwik.vlcremote.bean;

public class VLCStatus extends VLCResponse {
	public static final String STATE_PAUSE = "paused";
	public static final String STATE_PLAY = "playing";
	public static final String STATE_STOP = "stopped";
	
	public static final int MAX_VOLUME = 512;
	
	private boolean fs; // can't call this "fullscreen" or the app crash (GSon try to put an integer into the boolean)
	private int volume;
	private int time;
	private int length;
	private boolean random;
	private String aspectratio;
	private String state;
	private double position;
	private VLCStatusInformation information;
	
	public boolean isFullscreen() {
		return fs;
	}
	public void setFullscreen(boolean fullscreen) {
		this.fs = fullscreen;
	}
	public void setFullscreen(int fullscreen) {
		this.fs = false;
	}
	
	public int getVolume() {
		return volume;
	}
	public void setVolume(int volume) {
		this.volume = volume;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public boolean isRandom() {
		return random;
	}
	public void setRandom(boolean random) {
		this.random = random;
	}
	public String getAspectratio() {
		return aspectratio;
	}
	public void setAspectratio(String aspectratio) {
		this.aspectratio = aspectratio;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public double getPosition() {
		return position;
	}
	public void setPosition(double position) {
		this.position = position;
	}
	public VLCStatusInformation getInformation() {
		return information;
	}
	public void setInformation(VLCStatusInformation information) {
		this.information = information;
	}
}
