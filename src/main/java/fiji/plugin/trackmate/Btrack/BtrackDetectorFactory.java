/*-
 * #%L
 * TrackMate detector based on Skeletonization.
 * %%
 * Copyright (C) 2022 - 2023 TrackMate developers.
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
import static fiji.plugin.trackmate.io.IOUtils.readDoubleAttribute;
import static fiji.plugin.trackmate.io.IOUtils.readIntegerAttribute;
import static fiji.plugin.trackmate.io.IOUtils.readStringAttribute;
import static fiji.plugin.trackmate.io.IOUtils.writeAttribute;
import static fiji.plugin.trackmate.io.IOUtils.writeTargetChannel;
import static fiji.plugin.trackmate.util.TMUtils.checkMapKeys;
import static fiji.plugin.trackmate.util.TMUtils.checkParameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import org.jdom2.Element;
import org.scijava.plugin.Plugin;

import fiji.plugin.trackmate.Logger;
import fiji.plugin.trackmate.Model;
import fiji.plugin.trackmate.Settings;
import fiji.plugin.trackmate.detection.LabelImageDetector;
import fiji.plugin.trackmate.detection.SpotDetector;
import fiji.plugin.trackmate.detection.SpotDetectorFactory;
import fiji.plugin.trackmate.detection.SpotGlobalDetector;
import fiji.plugin.trackmate.detection.SpotGlobalDetectorFactory;
import fiji.plugin.trackmate.gui.components.ConfigurationPanel;
import fiji.plugin.trackmate.io.IOUtils;
import fiji.plugin.trackmate.util.TMUtils;
import net.imagej.ImgPlus;
import net.imagej.axis.Axes;
import net.imagej.ops.MetadataUtil;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.ImgView;
import net.imglib2.img.display.imagej.ImgPlusViews;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.Views;

@Plugin( type = SpotDetectorFactory.class )
public class BtrackDetectorFactory< T extends RealType< T > & NativeType< T >> implements SpotDetectorFactory< T >
{

	/*
	 * CONSTANTS
	 */

	public static final String KEY_LOGGER = "LOGGER";
	/** A string key identifying this factory. */
	public static final String DETECTOR_KEY = "BTRACK_DETECTOR";

	/** The pretty name of the target detector. */
	public static final String NAME = "Btrack";

	/** An html information text. */
	public static final String INFO_TEXT = "<html>"
			+ "<p>" 
			+ "Btrack detector relies on skeletonization to detect end points of integer labelled regions."
			+ "<p>"
			+ "For this detector to work, the 'Btrack' update site "
			+ "must be activated in your Fiji installation. "
			+"<p>"
			+ "Made by Kapoorlabs"
			+ "</html>";

	/*
	 * FIELDS
	 */

	/** The image to operate on. Multiple frames, single channel. */
	protected ImgPlus< T > img;

	protected Map< String, Object > settings;

	protected String errorMessage;
	


	

	@Override
	public boolean setTarget( final ImgPlus< T > img, final Map< String, Object > settings )
	{
		this.img = img;
		this.settings = settings;
		return checkSettings( settings );
	}

	@Override
	public String getErrorMessage()
	{
		return errorMessage;
	}

	@Override
	public boolean marshall( final Map< String, Object > settings, final Element element )
	{
		final StringBuilder errorHolder = new StringBuilder();
		boolean ok = writeTargetChannel( settings, element, errorHolder );
		

		if ( !ok )
			errorMessage = errorHolder.toString();

		return ok;
	}

	@Override
	public boolean unmarshall( final Element element, final Map< String, Object > settings )
	{
		settings.clear();
		final StringBuilder errorHolder = new StringBuilder();
		boolean ok = true;
		ok = ok && readIntegerAttribute( element, settings, KEY_TARGET_CHANNEL, errorHolder );
		

		if ( !ok )
		{
			errorMessage = errorHolder.toString();
			return false;
		}
		return checkSettings( settings );
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
		settings.put( KEY_LOGGER, Logger.DEFAULT_LOGGER );
		return settings;
	}

	@Override
	public boolean checkSettings( final Map< String, Object > settings )
	{
		boolean ok = true;
		final StringBuilder errorHolder = new StringBuilder();
		ok = ok & checkParameter( settings, KEY_TARGET_CHANNEL, Integer.class, errorHolder );
		// If we have a logger, test it is of the right class.
				final Object loggerObj = settings.get( KEY_LOGGER );
				if ( loggerObj != null && !Logger.class.isInstance( loggerObj ) )
				{
					errorHolder.append( "Value for parameter " + KEY_LOGGER + " is not of the right class. "
							+ "Expected " + Logger.class.getName() + ", got " + loggerObj.getClass().getName() + ".\n" );
					ok = false;
				}
		final List< String > mandatoryKeys = new ArrayList<>();
		mandatoryKeys.add( KEY_TARGET_CHANNEL );
		ok = ok & checkMapKeys( settings, mandatoryKeys, null, errorHolder );
		if ( !ok )
			errorMessage = errorHolder.toString();
		final List< String > optionalKeys = Arrays.asList(
				KEY_LOGGER );
		ok = ok & checkMapKeys( settings, mandatoryKeys, optionalKeys, errorHolder );
		if ( !ok )
			errorMessage = errorHolder.toString();
		return ok;
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
	protected ImgPlus< T > prepareImg()
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

	@Override
	public boolean has2Dsegmentation()
	{
		return true;
	}

	@Override
	public BtrackDetectorFactory< T > copy()
	{
		return new BtrackDetectorFactory<>();
	}

	@Override
	public SpotDetector<T> getDetector(Interval interval, int frame) {
	
		final ImgPlus< T > imageThisFrame = TMUtils.hyperSlice( prepareImg(), 0, frame );
		final double[] calibration = TMUtils.getSpatialCalibration( img );
		final BtrackDetector< T > detector = new BtrackDetector<T>(
				imageThisFrame,
				interval,
				calibration);
		
		return detector;
		
	}
}
