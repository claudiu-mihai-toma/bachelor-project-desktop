import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ij.process.ColorProcessor;

public class EdgeDetectedScreenshotFrame extends JFrame
{
	private static final long	serialVersionUID	= 1L;
	JLabel						mLabel				= null;
	ScreenshotGrabber			mScreenshotGrabber	= null;

	public EdgeDetectedScreenshotFrame()
	{
		mScreenshotGrabber = new ScreenshotGrabber();

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		panel.setBackground(Color.BLACK);
		ImageIcon icon = new ImageIcon(getNewScreenshot());
		mLabel = new JLabel();
		mLabel.setIcon(icon);
		panel.add(mLabel);
		this.getContentPane().add(panel);
		this.pack();
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setAlwaysOnTop(true);
		this.setVisible(true);
	}

	public BufferedImage getNewScreenshot()
	{
		BufferedImage image = mScreenshotGrabber.getScreenshot();
		image = ImageDistanceCalculator.resize(image);
		image = toEdgeDetectedImage(image);

		return image;
	}

	public void updateScreenshot()
	{
		BufferedImage image = getNewScreenshot();

		updateScreenshot(image);
	}

	public void updateScreenshot(BufferedImage image)
	{
		ImageIcon icon = new ImageIcon(image);
		mLabel.setIcon(icon);
		mLabel.repaint();
		this.pack();
	}

	public static BufferedImage toEdgeDetectedImage(BufferedImage image)
	{
		if (InteractiveInformationShareMain.RUN_MAIN)
		{
			return image;
		}

		ColorProcessor ip = new ColorProcessor(image);
		ip.findEdges();
		BufferedImage edgeImage = ip.getBufferedImage();

		return edgeImage;
	}
}
