import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ScreenshotFrame extends JFrame
{
	private static final long serialVersionUID = 1L;
	JLabel mLabel = null;
	ScreenshotGrabber mScreenshotGrabber = null;
	
	public ScreenshotFrame()
	{
		mScreenshotGrabber = new ScreenshotGrabber();
		BufferedImage image = mScreenshotGrabber.getScreenshot();
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		panel.setBackground(Color.BLACK);
		ImageIcon icon = new ImageIcon(image);
		mLabel = new JLabel();
		mLabel.setIcon(icon);
		panel.add(mLabel);
		this.getContentPane().add(panel);
		this.pack();
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setAlwaysOnTop(true);
		this.setVisible(true);
	}
	
	void updateScreenshot()
	{
		BufferedImage image = mScreenshotGrabber.getScreenshot();
		ImageIcon icon = new ImageIcon(image);
		mLabel.setIcon(icon);
		mLabel.repaint();
	}
}
