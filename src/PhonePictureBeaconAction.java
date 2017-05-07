import java.awt.image.BufferedImage;
import java.io.IOException;

import ij.process.ImageConverter;

public class PhonePictureBeaconAction implements BeaconActionInterface
{

	private BufferedImageReceiver		mBufferedImageReceiver	= null;
	private PhoneFeedFrame				mPhoneFeedFrame;
	private EdgeDetectedScreenshotFrame	mScreenshotFrame;

	public PhonePictureBeaconAction(PhoneFeedFrame phoneFeedFrame, EdgeDetectedScreenshotFrame screenshotFrame)
	{
		mPhoneFeedFrame = phoneFeedFrame;
		mScreenshotFrame = screenshotFrame;
	}

	@Override
	public void actionSuccess(String address)
	{
		if (mBufferedImageReceiver == null)
		{
			try
			{
				mBufferedImageReceiver = new BufferedImageReceiver(address, Constants.Ports.PICTURE_STREAM_SERVER_PORT);
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
		}

		BufferedImage receivedBufferedImage = mBufferedImageReceiver.receive();

		if (receivedBufferedImage == null)
		{
			mBufferedImageReceiver.close();
			mBufferedImageReceiver = null;
			return;
		}
		
		receivedBufferedImage = ImageDistanceCalculator.resize(receivedBufferedImage);

		mPhoneFeedFrame.updateFrame(receivedBufferedImage);

		BufferedImage screenshotBufferedImage = mScreenshotFrame.getNewScreenshot();
		
		ImageComparerOpenCV.compare(receivedBufferedImage, screenshotBufferedImage);
	}

	@Override
	public void actionFailure()
	{
		if (mBufferedImageReceiver != null)
		{
			mBufferedImageReceiver.close();
			mBufferedImageReceiver = null;
		}
		
		mPhoneFeedFrame.hideFrame();
	}

}
