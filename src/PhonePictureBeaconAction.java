import java.awt.image.BufferedImage;
import java.io.IOException;

public class PhonePictureBeaconAction implements BeaconActionInterface
{

	private BufferedImageReceiver		mBufferedImageReceiver	= null;
	private PhoneFeedFrame				mPhoneFeedFrame;
	private EdgeDetectedScreenshotFrame	mScreenshotFrame;
	private QRFrame						mQRFrame;

	public PhonePictureBeaconAction(PhoneFeedFrame phoneFeedFrame, EdgeDetectedScreenshotFrame screenshotFrame,
			QRFrame qrFrame)
	{
		mPhoneFeedFrame = phoneFeedFrame;
		mScreenshotFrame = screenshotFrame;
		mQRFrame = qrFrame;
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
				System.out.println("Error creating BufferedImageReceiver!");
				return;
			}
		}

		BufferedImage receivedBufferedImage = mBufferedImageReceiver.receive();

		if (receivedBufferedImage == null)
		{
			mBufferedImageReceiver.close();
			mBufferedImageReceiver = null;
			System.out.println("Resetting image buffer receiver!");
			return;
		}

		receivedBufferedImage = ImageDistanceCalculator.resize(receivedBufferedImage);

		mPhoneFeedFrame.updateFrame(receivedBufferedImage);
		mQRFrame.setTitle("phone address: " + address);
		mQRFrame.displayInCorner();

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
			System.out.println("Broadcast action failure!");
		}

		mPhoneFeedFrame.hideFrame();
		mQRFrame.hideFrame();
	}

}
