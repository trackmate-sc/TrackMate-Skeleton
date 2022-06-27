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

	
	
	public static < T extends RealType< T > & NativeType< T > > List<Spot>run(
			final ImgPlus< T > input,
			final Interval interval,
			final double[] calibration
			) 
	{

		final int numThreads = Runtime.getRuntime().availableProcessors();
	
			final List< Spot > spotsThisFrame = SkelUtils.fromSkel(input, interval, calibration, numThreads); 
			
			
	return spotsThisFrame;	
	}
}
