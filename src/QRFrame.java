import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.security.InvalidParameterException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class QRFrame extends JFrame
{
	private static final long	serialVersionUID	= 1L;
	private static final String	DEFAULT_TITLE		= "Local IP Address";
	private int					mXcoordinate;
	private int					mYcoordinate;

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
		JLabel label = new JLabel();
		label.setIcon(icon);
		panel.add(label);
		this.getContentPane().add(panel);
		this.pack();
		this.setTitle(title);

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
		Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
		mXcoordinate = (int) rect.getMaxX() - this.getWidth();
		mYcoordinate = (int) rect.getMaxY() - this.getHeight();
		this.setLocation(mXcoordinate, mYcoordinate);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setAlwaysOnTop(true);
	}

	public void displayInCorner()
	{
		this.setLocation(mXcoordinate, mYcoordinate);
		this.setVisible(true);
	}

	public void hide()
	{
		this.setVisible(false);
	}
}