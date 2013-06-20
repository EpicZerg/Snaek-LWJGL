package epiczerg.snaek;

public class Clock {
	public long ms = 0;
	long startms = 0;
	long timerstartms = 0;
	long timerstopms = 0;
	double s = 0;
	public Clock(){
		startms = System.currentTimeMillis();
	}
	public void delay(long ms){
		this.ms = System.currentTimeMillis() + ms;
		while(System.currentTimeMillis() < this.ms);
	}
	public long getMillisecondsPassed(){
		return System.currentTimeMillis() - startms;
	}
	public void startTimer(){
		timerstartms = getMillisecondsPassed();		
	}
	public void stopTimer(){
		timerstopms = getMillisecondsPassed();
	}
	public long getTimerValue(boolean isTimerStopped){
		return isTimerStopped ? timerstopms - timerstartms : getMillisecondsPassed() - timerstartms;
	}

	
}
