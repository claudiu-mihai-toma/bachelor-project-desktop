import java.awt.image.BufferedImage;

import com.github.sarxos.webcam.Webcam;

public class ScreenshotGrabber
{
	private Webcam mWebcam = null;
	
	public ScreenshotGrabber()
	{
		mWebcam = Webcam.getDefault();
		mWebcam.open();
	}
	
	public BufferedImage getScreenshot()
	{
		return mWebcam.getImage();
	}
	
	public void close()
	{
		mWebcam.close();
	}
}
