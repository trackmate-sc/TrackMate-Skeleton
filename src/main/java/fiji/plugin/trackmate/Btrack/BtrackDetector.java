package fiji.plugin.trackmate.Btrack;

import fiji.plugin.trackmate.SpotCollection;
import fiji.plugin.trackmate.detection.SpotGlobalDetector;
import net.imagej.ImgPlus;
import net.imglib2.Interval;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;

public class BtrackDetector< T extends RealType< T > & NativeType< T > > implements SpotGlobalDetector< T > {
	
	
	private final static String BASE_ERROR_MESSAGE = "BtrackDetector: ";

	protected final ImgPlus< T > img;

	protected final Interval interval;

	protected final double[] calibration;
	
	protected String baseErrorMessage;
	
	protected SpotCollection spots;
	
	public BtrackDetector(final ImgPlus< T > img,
			final Interval interval,
			final double[] calibration) {
		
		    this.img = img;
		    this.interval = interval;
		    this.calibration = calibration;
		    this.baseErrorMessage = BASE_ERROR_MESSAGE;
		
	}
	
	@Override
	public SpotCollection getResult() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean checkInput() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean process() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getErrorMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getProcessingTime() {
		// TODO Auto-generated method stub
		return 0;
	}

}
