package fiji.plugin.trackmate.Btrack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fiji.plugin.trackmate.Spot;
import fiji.plugin.trackmate.detection.MaskUtils;
import net.imglib2.Interval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.roi.labeling.ImgLabeling;
import net.imglib2.roi.labeling.LabelRegion;
import net.imglib2.roi.labeling.LabelRegionCursor;
import net.imglib2.roi.labeling.LabelRegions;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

public class SkelUtils {
	
	
	/**
	 * Creates spots from a grayscale image, thresholded to create a mask. A
	 * spot is created for each connected-component of the mask, with a size
	 * that matches the mask size. The quality of the spots is read from another
	 * image, by taking the max pixel value of this image with the ROI.
	 * 
	 * @param <T>
	 *            the type of the input image. Must be real, scalar.
	 * @param input
	 *            the input image.
	 * @param interval
	 *            the interval in the input image to analyze.
	 * @param calibration
	 *            the physical calibration.
	
	 * @param numThreads
	 *            how many threads to use for multithreaded computation.
	 * @param qualityImage
	 *            the image in which to read the quality value.
	 * @return a list of spots, without ROI.
	 */
	public static < T extends RealType< T >, R extends RealType< R > > List< Spot > fromSkel(
			final RandomAccessible< T > input,
			final Interval interval,
			final double[] calibration,
			
			final int numThreads)
	{
		
		
		
		
		// Get labeling from mask.
		final ImgLabeling< Integer, IntType > labeling = MaskUtils.toLabeling( input, interval, 0, numThreads );

		
		// Parse each component.
		final LabelRegions< Integer > regions = new LabelRegions<>( labeling );
		final Iterator< LabelRegion< Integer > > iterator = regions.iterator();
		final List< Spot > spots = new ArrayList<>( regions.getExistingLabels().size() );
		while ( iterator.hasNext() )
		{
			final LabelRegion< Integer > region = iterator.next();
			final LabelRegionCursor cursor = region.localizingCursor();
			final int[] cursorPos = new int[ labeling.numDimensions() ];
			final long[] sum = new long[ 3 ];
			double quality = Double.NEGATIVE_INFINITY;
			while ( cursor.hasNext() )
			{
				cursor.fwd();

				// Position.
				cursor.localize( cursorPos );
				for ( int d = 0; d < sum.length; d++ )
					sum[ d ] += cursorPos[ d ];

				
			}

			final double[] pos = new double[ 3 ];
			for ( int d = 0; d < pos.length; d++ )
				pos[ d ] = sum[ d ] / ( double ) region.size();

			final double x = calibration[ 0 ] * ( interval.min( 0 ) + pos[ 0 ] );
			final double y = calibration[ 1 ] * ( interval.min( 1 ) + pos[ 1 ] );
			final double z = calibration[ 2 ] * ( interval.min( 2 ) + pos[ 2 ] );

			double volume = region.size();
			for ( int d = 0; d < calibration.length; d++ )
				if ( calibration[ d ] > 0 )
					volume *= calibration[ d ];

			final double radius = ( labeling.numDimensions() == 2 )
					? Math.sqrt( volume / Math.PI )
					: Math.pow( 3. * volume / ( 4. * Math.PI ), 1. / 3. );
			spots.add( new Spot( x, y, z, radius, quality ) );
		}

		return spots;
	}

}
