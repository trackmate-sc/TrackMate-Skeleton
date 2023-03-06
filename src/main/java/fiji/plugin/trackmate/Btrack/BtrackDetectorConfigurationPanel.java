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

import static fiji.plugin.trackmate.detection.DetectorKeys.KEY_TARGET_CHANNEL;
import static fiji.plugin.trackmate.gui.Fonts.BIG_FONT;
import static fiji.plugin.trackmate.gui.Fonts.FONT;
import static fiji.plugin.trackmate.gui.Fonts.SMALL_FONT;
import static fiji.plugin.trackmate.gui.Icons.PREVIEW_ICON;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.scijava.prefs.PrefService;

import fiji.plugin.trackmate.Logger;
import fiji.plugin.trackmate.Model;
import fiji.plugin.trackmate.Settings;
import fiji.plugin.trackmate.detection.DetectionUtils;
import fiji.plugin.trackmate.util.FileChooser;
import fiji.plugin.trackmate.util.FileChooser.DialogType;
import fiji.plugin.trackmate.util.JLabelLogger;
import fiji.plugin.trackmate.util.TMUtils;

public class BtrackDetectorConfigurationPanel extends BtrackDetectorBaseConfigurationPanel
{

	private static final long serialVersionUID = 1L;


	private static final String TITLE = BtrackDetectorFactory.NAME;

	private final JSlider sliderChannel;

	protected final PrefService prefService;

	/**
	 * Create the panel.
	 */
	public BtrackDetectorConfigurationPanel( final Settings settings, final Model model)
	{
		super( settings, model );
		this.prefService = TMUtils.getContext().getService( PrefService.class );

		final GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 144, 0, 32 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 84, 0, 27, 0, 0, 0, 0, 37, 23 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 0.0 };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0 };
		setLayout( gridBagLayout );

		final JLabel lblSettingsForDetector = new JLabel( "Settings for detector:" );
		lblSettingsForDetector.setFont( FONT );
		final GridBagConstraints gbc_lblSettingsForDetector = new GridBagConstraints();
		gbc_lblSettingsForDetector.gridwidth = 3;
		gbc_lblSettingsForDetector.insets = new Insets( 5, 5, 5, 0 );
		gbc_lblSettingsForDetector.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblSettingsForDetector.gridx = 0;
		gbc_lblSettingsForDetector.gridy = 0;
		add( lblSettingsForDetector, gbc_lblSettingsForDetector );

		final JLabel lblDetector = new JLabel( TITLE, ICON, JLabel.LEFT );
		lblDetector.setFont( BIG_FONT );
		lblDetector.setHorizontalAlignment( SwingConstants.CENTER );
		final GridBagConstraints gbc_lblDetector = new GridBagConstraints();
		gbc_lblDetector.gridwidth = 3;
		gbc_lblDetector.insets = new Insets( 5, 5, 5, 0 );
		gbc_lblDetector.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblDetector.gridx = 0;
		gbc_lblDetector.gridy = 1;
		add( lblDetector, gbc_lblDetector );

		/*
		 * Help text.
		 */
		final JLabel lblHelptext = new JLabel( BtrackDetectorFactory.INFO_TEXT
				.replace( "<br>", "" )
				.replace( "<p>", "<p align=\"justify\">" )
				.replace( "<html>", "<html><p align=\"justify\">" ) );
		lblHelptext.setFont( FONT.deriveFont( Font.ITALIC ) );
		final GridBagConstraints gbc_lblHelptext = new GridBagConstraints();
		gbc_lblHelptext.anchor = GridBagConstraints.NORTH;
		gbc_lblHelptext.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblHelptext.gridwidth = 3;
		gbc_lblHelptext.insets = new Insets( 5, 10, 5, 10 );
		gbc_lblHelptext.gridx = 0;
		gbc_lblHelptext.gridy = 2;
		add( lblHelptext, gbc_lblHelptext );

		/*
		 * Channel selector.
		 */

		final JLabel lblSegmentInChannel = new JLabel( "Segment in channel:" );
		lblSegmentInChannel.setFont( SMALL_FONT );
		final GridBagConstraints gbc_lblSegmentInChannel = new GridBagConstraints();
		gbc_lblSegmentInChannel.anchor = GridBagConstraints.EAST;
		gbc_lblSegmentInChannel.insets = new Insets( 5, 5, 5, 5 );
		gbc_lblSegmentInChannel.gridx = 0;
		gbc_lblSegmentInChannel.gridy = 3;
		add( lblSegmentInChannel, gbc_lblSegmentInChannel );

		sliderChannel = new JSlider();
		final GridBagConstraints gbc_sliderChannel = new GridBagConstraints();
		gbc_sliderChannel.fill = GridBagConstraints.HORIZONTAL;
		gbc_sliderChannel.insets = new Insets( 5, 5, 5, 5 );
		gbc_sliderChannel.gridx = 1;
		gbc_sliderChannel.gridy = 3;
		add( sliderChannel, gbc_sliderChannel );

		final JLabel labelChannel = new JLabel( "1" );
		labelChannel.setHorizontalAlignment( SwingConstants.CENTER );
		labelChannel.setFont( SMALL_FONT );
		final GridBagConstraints gbc_labelChannel = new GridBagConstraints();
		gbc_labelChannel.insets = new Insets( 5, 5, 5, 0 );
		gbc_labelChannel.gridx = 2;
		gbc_labelChannel.gridy = 3;
		add( labelChannel, gbc_labelChannel );

		sliderChannel.addChangeListener( l -> labelChannel.setText( "" + sliderChannel.getValue() ) );

		

		/*
		 * Logger.
		 */

		final JLabelLogger labelLogger = new JLabelLogger();
		final GridBagConstraints gbc_labelLogger = new GridBagConstraints();
		gbc_labelLogger.gridwidth = 3;
		gbc_labelLogger.gridx = 0;
		gbc_labelLogger.gridy = 10;
		add( labelLogger, gbc_labelLogger );
		final Logger localLogger = labelLogger.getLogger();

		/*
		 * Preview.
		 */

		final JButton btnPreview = new JButton( "Preview", PREVIEW_ICON );
		btnPreview.setFont( FONT );
		final GridBagConstraints gbc_btnPreview = new GridBagConstraints();
		gbc_btnPreview.gridwidth = 2;
		gbc_btnPreview.anchor = GridBagConstraints.SOUTHEAST;
		gbc_btnPreview.insets = new Insets( 5, 5, 5, 5 );
		gbc_btnPreview.gridx = 1;
		gbc_btnPreview.gridy = 9;
		add( btnPreview, gbc_btnPreview );

		/*
		 * Listeners and specificities.
		 */

		btnPreview.addActionListener( e -> DetectionUtils.preview(
				model,
				settings,
				getDetectorFactory(),
				getSettings(),
				settings.imp.getFrame() - 1,
				localLogger,
				b -> btnPreview.setEnabled( b ) ) );

		/*
		 * Deal with channels: the slider and channel labels are only visible if
		 * we find more than one channel.
		 */
		if ( null != settings.imp )
		{
			final int n_channels = settings.imp.getNChannels();
			sliderChannel.setMaximum( n_channels );
			sliderChannel.setMinimum( 1 );
			sliderChannel.setValue( settings.imp.getChannel() );

			if ( n_channels <= 1 )
			{
				labelChannel.setVisible( false );
				lblSegmentInChannel.setVisible( false );
				sliderChannel.setVisible( false );
			}
			else
			{
				labelChannel.setVisible( true );
				lblSegmentInChannel.setVisible( true );
				sliderChannel.setVisible( true );
			}
		}

		
	}

	@Override
	public Map< String, Object > getSettings()
	{
		final HashMap< String, Object > settings = new HashMap<>( 2 );

		final int targetChannel = sliderChannel.getValue();
		settings.put( KEY_TARGET_CHANNEL, targetChannel );

		
		return settings;
	}

	@Override
	public void setSettings( final Map< String, Object > settings )
	{
		sliderChannel.setValue( ( Integer ) settings.get( KEY_TARGET_CHANNEL ) );
		
	}

	@Override
	public void clean()
	{}

	@Override
	@SuppressWarnings( "rawtypes" )
	protected BtrackDetectorFactory< ? > getDetectorFactory()
	{
		return new BtrackDetectorFactory();
	}

	
}
