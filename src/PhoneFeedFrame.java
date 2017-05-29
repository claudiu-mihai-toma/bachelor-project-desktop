import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.security.InvalidParameterException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PhoneFeedFrame extends JFrame
{
	private static final long	serialVersionUID			= 1L;
	private static final String	DEFAULT_TITLE				= "Phone feed frame";
	private int					mXcoordinate;
	private int					mYcoordinate;
	private JLabel				mLabel;
	boolean						mPhoneFeedFrameInitialized	= false;

	public void initialize(Image image)
	{
		initialize(image, DEFAULT_TITLE);
	}

	public void initialize(Image image, String title)
	{
		if (image == null)
		{
			throw new InvalidParameterException();
		}
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		panel.setBackground(Color.BLACK);
		ImageIcon icon = new ImageIcon(image);
		mLabel = new JLabel();
		mLabel.setIcon(icon);
		panel.add(mLabel);
		this.getContentPane().add(panel);
		this.pack();
		this.setTitle(title);

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
		Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
		mXcoordinate = (int) rect.getMaxX() - this.getWidth();
		mYcoordinate = 0;
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setAlwaysOnTop(true);
		displayInCorner();

		mPhoneFeedFrameInitialized = true;
	}

	public void displayInCorner()
	{
		this.setLocation(mXcoordinate, mYcoordinate);
		this.setVisible(true);
	}

	public void hideFrame()
	{
		this.setVisible(false);
	}

	public void updateFrame(BufferedImage image)
	{
		if (mPhoneFeedFrameInitialized)
		{
			//System.out.println("Phone feed already initialized.");
			ImageIcon icon = new ImageIcon(image);
			mLabel.setIcon(icon);
			mLabel.repaint();
			this.pack();
			this.displayInCorner();
		}
		else
		{
			//System.out.println("Initializing phone feed...");
			initialize(image);
		}
	}
}
