package fr.nesqwik.vlcremote.utils;

import fr.nesqwik.vlcremote.bean.Time;

public class TimeConvertor {

	
	public static Time getTime(int seconds) {
		
		
		int hours = (int) seconds / 3600;
	    int remainder = (int) seconds - hours * 3600;
	    int minutes = remainder / 60;
	    remainder = remainder - minutes * 60;
	    seconds = remainder;
		
		return new Time(seconds, minutes, hours);
	}
	
	
}
