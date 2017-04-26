import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ij.process.ColorProcessor;

public class ScreenshotFrame extends JFrame
{
	private static final long	serialVersionUID	= 1L;
	JLabel						mLabel				= null;
	ScreenshotGrabber			mScreenshotGrabber	= null;

	public ScreenshotFrame()
	{
		mScreenshotGrabber = new ScreenshotGrabber();

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		panel.setBackground(Color.BLACK);
		ImageIcon icon = getEdgeImageIcon();
		mLabel = new JLabel();
		mLabel.setIcon(icon);
		panel.add(mLabel);
		this.getContentPane().add(panel);
		this.pack();
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setAlwaysOnTop(true);
		this.setVisible(true);
	}

	public ImageIcon getEdgeImageIcon()
	{
		BufferedImage image = mScreenshotGrabber.getScreenshot();

		ColorProcessor ip = new ColorProcessor(image);
		ip.findEdges();
		BufferedImage edgeImage = ip.getBufferedImage();
		
		ImageIcon edgeIcon = new ImageIcon(edgeImage);
		
		return edgeIcon;
	}

	public void updateScreenshot()
	{
		ImageIcon icon = getEdgeImageIcon();	
		mLabel.setIcon(icon);
		mLabel.repaint();
	}
}
