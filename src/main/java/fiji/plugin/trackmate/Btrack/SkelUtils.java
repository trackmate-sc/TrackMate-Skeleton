package fiji.plugin.trackmate.Btrack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fiji.plugin.trackmate.Spot;
import fiji.plugin.trackmate.detection.MaskUtils;
import net.imagej.ops.OpService;
import net.imglib2.Cursor;
import net.imglib2.Interval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.RealLocalizable;
import net.imglib2.RealPoint;
import net.imglib2.converter.Converter;
import net.imglib2.converter.Converters;
import net.imglib2.img.ImgFactory;
import net.imglib2.img.cell.CellImgFactory;
import net.imglib2.roi.labeling.ImgLabeling;
import net.imglib2.roi.labeling.LabelRegion;
import net.imglib2.roi.labeling.LabelRegionCursor;
import net.imglib2.roi.labeling.LabelRegions;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.util.Util;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;
import net.imagej.ImageJ;
import skeleton.SkeletonAnalyzer;
import skeleton.SkeletonCreator;

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
		final ImgLabeling< Integer, IntType > imgLabeling = MaskUtils.toLabeling( input, interval, 0, numThreads );
	
		ImageJ ij = new ImageJ();
		SkeletonCreator<BitType> skelmake = new SkeletonCreator<BitType>(imgLabeling, ij.op());
		ArrayList<RandomAccessibleInterval<BitType>> skels =  skelmake.getSkeletons();
		ArrayList<RealLocalizable> endPoints = new ArrayList<RealLocalizable>();
		for (RandomAccessibleInterval<BitType> skeleton : skels) {

			SkeletonAnalyzer<BitType> skelanalyze = new SkeletonAnalyzer<BitType>(skeleton, ij.op());
			RandomAccessibleInterval<BitType> Ends = skelanalyze.getEndpoints();
			RandomAccessibleInterval<BitType> Branches = skelanalyze.getBranchpoints();

			Cursor<BitType> skeletoncursor = Views.iterable(skeleton).localizingCursor();

			
			Cursor<BitType> skelcursor = Views.iterable(Ends).localizingCursor();
			while (skelcursor.hasNext()) {

				skelcursor.next();

				RealPoint addPoint = new RealPoint(skelcursor);
				if (skelcursor.get().getInteger() > 0) {

					endPoints.add(addPoint);

				}

			}
		}
		
		return spots;
	}

}
