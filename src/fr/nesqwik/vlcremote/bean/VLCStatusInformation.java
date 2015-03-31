package fr.nesqwik.vlcremote.bean;

public class VLCStatusInformation {
	private int chapter;
	private int title;
	private VLCStatusInformationCategory category;
	
	public int getChapter() {
		return chapter;
	}
	public void setChapter(int chapter) {
		this.chapter = chapter;
	}
	public int getTitle() {
		return title;
	}
	public void setTitle(int title) {
		this.title = title;
	}
	public VLCStatusInformationCategory getCategory() {
		return category;
	}
	public void setCategory(VLCStatusInformationCategory category) {
		this.category = category;
	}
}
