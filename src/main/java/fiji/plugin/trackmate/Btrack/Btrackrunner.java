package fiji.plugin.trackmate.Btrack;

import java.io.IOException;
import java.util.List;

import org.scijava.Context;
import org.scijava.app.StatusService;
import org.scijava.log.LogService;
import org.scijava.options.OptionsService;

import fiji.plugin.trackmate.Logger;
import fiji.plugin.trackmate.Spot;
import fiji.plugin.trackmate.SpotCollection;
import fiji.plugin.trackmate.util.TMUtils;
import net.imagej.ImgPlus;
import net.imagej.axis.Axes;
import net.imagej.ops.MetadataUtil;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.ImgView;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.Views;

public class Btrackrunner {

	private final static Context context = TMUtils.getContext();
	
	
	public static < T extends RealType< T > & NativeType< T > > SpotCollection run(
			final ImgPlus< T > input,
			final Interval interval,
			final double[] calibration,
			final Logger logger
			) throws IOException
	{

		final int numThreads = Runtime.getRuntime().availableProcessors();
		/*
		 * Properly set the image to process: crop it.
		 */

		final RandomAccessibleInterval< T > crop = Views.interval( input, interval );
		final RandomAccessibleInterval< T > zeroMinCrop = Views.zeroMin( crop );

		final ImgPlus< T > cropped = new ImgPlus<>( ImgView.wrap( zeroMinCrop, input.factory() ) );
		MetadataUtil.copyImgPlusMetadata( input, cropped );
		final SpotCollection spots = new SpotCollection();
		final int timeIndex = cropped.dimensionIndex( Axes.TIME );
		final int t0 = interval.numDimensions() > 2 ? ( int ) interval.min( 2 ) : 0;
		
		logger.log("Running Skeletonization");
		for ( int t = 0; t < cropped.dimension( timeIndex ); t++ )
		{
			logger.setProgress(t);
			final List< Spot > spotsThisFrame;
			final RandomAccessibleInterval< T > imageThisFrame = TMUtils.hyperSlice( cropped, 0, t );

			spotsThisFrame = SkelUtils.fromSkel(imageThisFrame, interval, calibration, numThreads); 
			
			spots.put(t + t0, spotsThisFrame);
			
		}
		
	return spots;	
	}
}
