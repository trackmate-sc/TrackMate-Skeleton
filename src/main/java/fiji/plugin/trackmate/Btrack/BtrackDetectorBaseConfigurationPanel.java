package fiji.plugin.trackmate.Btrack;

import java.net.URL;

import javax.swing.ImageIcon;

import fiji.plugin.trackmate.Model;
import fiji.plugin.trackmate.Settings;
import fiji.plugin.trackmate.gui.components.ConfigurationPanel;

public abstract class BtrackDetectorBaseConfigurationPanel extends ConfigurationPanel
{

	private static final long serialVersionUID = 1L;

	protected static final ImageIcon ICON = new ImageIcon( getResource( "images/kapoorlogo.png" ) );

	protected final Settings settings;

	protected final Model model;

	public BtrackDetectorBaseConfigurationPanel( final Settings settings, final Model model )
	{
		this.settings = settings;
		this.model = model;
	}

	protected abstract BtrackDetectorFactory< ? > getDetectorFactory();

	protected static URL getResource( final String name )
	{
		return BtrackDetectorFactory.class.getClassLoader().getResource( name );
	}
	
}