/*-
 * #%L
 * TrackMate detector based on Skeletonization.
 * %%
 * Copyright (C) 2022 - 2026 TrackMate developers.
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

import static fiji.plugin.trackmate.detection.DetectorKeys.DEFAULT_TARGET_CHANNEL;
import static fiji.plugin.trackmate.detection.DetectorKeys.KEY_TARGET_CHANNEL;

import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import org.scijava.plugin.Plugin;

import fiji.plugin.trackmate.Model;
import fiji.plugin.trackmate.Settings;
import fiji.plugin.trackmate.detection.SpotDetector;
import fiji.plugin.trackmate.detection.SpotDetectorFactory;
import fiji.plugin.trackmate.gui.components.ConfigurationPanel;
import fiji.plugin.trackmate.util.TMUtils;
import net.imagej.ImgPlus;
import net.imagej.axis.Axes;
import net.imglib2.Interval;
import net.imglib2.img.display.imagej.ImgPlusViews;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;

@Plugin( type = SpotDetectorFactory.class )
public class BtrackDetectorFactory< T extends RealType< T > & NativeType< T >> implements SpotDetectorFactory< T >
{

	/*
	 * CONSTANTS
	 */

	/** A string key identifying this factory. */
	public static final String DETECTOR_KEY = "BTRACK_DETECTOR";

	/** The pretty name of the target detector. */
	public static final String NAME = "Btrack";

	/** An html information text. */
	public static final String INFO_TEXT = "<html>"
	        + "<p>"
	        + "This product is a testament to our expertise at KapoorLabs, where we specialize in creating cutting-edge solutions. "
	        + "We offer bespoke pipeline development services, transforming your developmental biology questions into publishable figures with our advanced computer vision and AI tools. "
	        + "Leverage our expertise and resources to achieve end-to-end solutions that make your research stand out."
	        + "</p>"
	        + "<p><b>Note:</b> The tools and pipelines showcased here represent only a fraction of what we can achieve. "
	        + "For tailored and comprehensive solutions beyond what was done in the referenced publication, engage with us directly. "
	        + "Our team is ready to provide the expertise and custom development you need to take your research to the next level. "
	        + "Visit us at <a href='https://www.kapoorlabs.org/'>KapoorLabs</a>."
	        + "</p>"
	        + "</html>";

	/*
	 * METHODS
	 */

	@Override
	public SpotDetector< T > getDetector( final ImgPlus< T > img, final Map< String, Object > settings, final Interval interval, final int frame )
	{
		final ImgPlus< T > imageThisFrame = TMUtils.hyperSlice( prepareImg( img, settings ), 0, frame );
		final double[] calibration = TMUtils.getSpatialCalibration( img );
		final BtrackDetector< T > detector = new BtrackDetector<>(
				imageThisFrame,
				interval,
				calibration );
		return detector;
	}

	@Override
	public boolean has2Dsegmentation()
	{
		return true;
	}

	@Override
	public ConfigurationPanel getDetectorConfigurationPanel( final Settings settings, final Model model )
	{
		return new BtrackDetectorConfigurationPanel( settings, model );
	}

	@Override
	public Map< String, Object > getDefaultSettings()
	{
		final Map< String, Object > settings = new HashMap<>();
		settings.put( KEY_TARGET_CHANNEL, DEFAULT_TARGET_CHANNEL );
		return settings;
	}

	@Override
	public String getInfoText()
	{
		return INFO_TEXT;
	}

	@Override
	public ImageIcon getIcon()
	{
		return null;
	}

	@Override
	public String getKey()
	{
		return DETECTOR_KEY;
	}

	@Override
	public String getName()
	{
		return NAME;
	}

	/**
	 * Return 1-channel, all time-points, all-Zs if any.
	 *
	 * @return an {@link ImgPlus}.
	 */
	protected ImgPlus< T > prepareImg( final ImgPlus< T > img, final Map< String, Object > settings )
	{
		final int cDim = img.dimensionIndex( Axes.CHANNEL );
		final ImgPlus< T > imFrame;
		if ( cDim < 0 )
		{
			imFrame = img;
		}
		else
		{
			// In ImgLib2, dimensions are 0-based.
			final int channel = ( Integer ) settings.get( KEY_TARGET_CHANNEL ) - 1;
			imFrame = ImgPlusViews.hyperSlice( img, cDim, channel );
		}
		return imFrame;
	}
}
