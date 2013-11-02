package pwr.ibi.asmood.logic;

public interface ProgressListener {

	public void onStart();
	/**
	 * @param - progress percent 0% - 100%
	 * */
	public void onProgres(int progress_percent);
	public void onValueChange(int current, int whole_number);
	
	public void onFinish();
}
