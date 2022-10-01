/*-
 * #%L
 * TrackMate detector based on Skeletonization.
 * %%
 * Copyright (C) 2022 TrackMate developers.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package fiji.plugin.trackmate.Btrack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fiji.plugin.trackmate.Spot;
import fiji.plugin.trackmate.detection.DetectionUtils;
import fiji.plugin.trackmate.detection.MaskUtils;
import net.imagej.ops.OpService;
import net.imglib2.Cursor;
import net.imglib2.Interval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.RealLocalizable;
import net.imglib2.RealPoint;
import net.imglib2.algorithm.labeling.ConnectedComponents;
import net.imglib2.converter.Converter;
import net.imglib2.converter.Converters;
import net.imglib2.img.ImgFactory;
import net.imglib2.img.cell.CellImgFactory;
import net.imglib2.roi.labeling.ImgLabeling;
import net.imglib2.roi.labeling.LabelRegion;
import net.imglib2.roi.labeling.LabelRegionCursor;
import net.imglib2.roi.labeling.LabelRegions;
import net.imglib2.type.NativeType;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.util.Util;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;
import net.imagej.ImageJ;
import skeleton.Regions;
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
	 * @return a list of spots, without ROI.
	 */
	public static <T extends RealType<T> & NativeType<T>> List< Spot > fromSkel(
			final RandomAccessibleInterval< T > input,
			final Interval interval,
			final double[] calibration,
			final int numThreads)
	{
		
		
		
	
			
		final ImgLabeling< Integer, IntType > imgLabeling  = Regions.asImgLabeling(input,
				ConnectedComponents.StructuringElement.FOUR_CONNECTED );
			
		
	
		ImageJ ij = new ImageJ();
		SkeletonCreator<BitType> skelmake = new SkeletonCreator<BitType>(imgLabeling, ij.op());
		skelmake.run();
		ArrayList<RandomAccessibleInterval<BitType>> skels =  skelmake.getSkeletons();
		ArrayList<RealLocalizable> endPoints = new ArrayList<RealLocalizable>();
		final List< Spot > spots = new ArrayList<>(  );
		for (RandomAccessibleInterval<BitType> skeleton : skels) {

			SkeletonAnalyzer<BitType> skelanalyze = new SkeletonAnalyzer<BitType>(skeleton, ij.op());
			RandomAccessibleInterval<BitType> Ends = skelanalyze.getEndpoints();
			Cursor<BitType> skelcursor = Views.iterable(Ends).localizingCursor();
			while (skelcursor.hasNext()) {

				skelcursor.next();

				RealPoint addPoint = new RealPoint(skelcursor);
				if (skelcursor.get().getInteger() > 0) {

					endPoints.add(addPoint);

					final double x = calibration[ 0 ] * ( interval.min( 0 ) + addPoint.getDoublePosition(0) );
					final double y = calibration[ 1 ] * ( interval.min( 1 ) + addPoint.getDoublePosition(1) );
					double z = 0;
					if(addPoint.numDimensions() > 2 ) {
					 z = calibration[ 2 ] * ( interval.min( 2 ) + addPoint.getDoublePosition(2) ); 
					}
					double volume = 5;
					for ( int d = 0; d < calibration.length; d++ )
						if ( calibration[ d ] > 0 )
							volume *= calibration[ d ];
					
					spots.add( new Spot( x, y, z, volume, 5 ) );
				}

			}
		}
		
		return spots;
	}

}
