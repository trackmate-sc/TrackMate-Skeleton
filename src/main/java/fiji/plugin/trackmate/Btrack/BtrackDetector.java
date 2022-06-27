package fiji.plugin.trackmate.Btrack;

import java.io.IOException;

import fiji.plugin.trackmate.Logger;
import fiji.plugin.trackmate.SpotCollection;
import fiji.plugin.trackmate.detection.SpotGlobalDetector;
import net.imagej.ImgPlus;
import net.imagej.axis.Axes;
import net.imglib2.Interval;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;

public class BtrackDetector< T extends RealType< T > & NativeType< T > > implements SpotGlobalDetector< T > {
	
	
	private final static String BASE_ERROR_MESSAGE = "BtrackDetector: ";

	protected final ImgPlus< T > img;

	protected final Interval interval;

	protected final double[] calibration;
	
	protected String baseErrorMessage;
	
	protected String errorMessage;
	
	private final Logger logger;
	
	protected long processingTime;
	
	protected SpotCollection spots;
	
	public BtrackDetector(final ImgPlus< T > img,
			final Interval interval,
			final double[] calibration,
			final Logger logger ) {
		    this.img = img;
		    this.interval = interval;
		    this.calibration = calibration;
		    this.logger = ( logger == null ) ? Logger.VOID_LOGGER : logger;
		    this.baseErrorMessage = BASE_ERROR_MESSAGE;
		
	}
	
	@Override
	public SpotCollection getResult() {
		return spots;
	}

	@Override
	public boolean checkInput() {
		if ( null == img )
		{
			errorMessage = baseErrorMessage + "Image is null.";
			return false;
		}
	
		return true;
	}

	@Override
	public boolean process() {
		
		final long start = System.currentTimeMillis();
		
		
		try {
			spots = Btrackrunner.run(img, interval, calibration, logger);
		} catch (IOException e) {
		
			e.printStackTrace();
		}
		
		
		final long end = System.currentTimeMillis();
		this.processingTime = end - start;

		
		return true;
	}

	@Override
	public String getErrorMessage() {
		return errorMessage;
	}

	@Override
	public long getProcessingTime() {
		return processingTime;
	}

}
