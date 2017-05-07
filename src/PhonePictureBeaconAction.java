import java.awt.image.BufferedImage;
import java.io.IOException;

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
		
		int comparisonResult = ImageComparerOpenCV.compare(receivedBufferedImage, screenshotBufferedImage);
		mBufferedImageReceiver.sendInt(comparisonResult);
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
