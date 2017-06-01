import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PhonePictureBeaconAction implements BeaconActionInterface
{
	private Object								mObject					= new Object();

	private Map<String, BufferedImageReceiver>	mBufferedImageReceivers	= null;
	private PhoneFeedFrame						mPhoneFeedFrame;
	private EdgeDetectedScreenshotFrame			mScreenshotFrame;
	private QRFrame								mQRFrame;

	public PhonePictureBeaconAction(PhoneFeedFrame phoneFeedFrame, EdgeDetectedScreenshotFrame screenshotFrame,
			QRFrame qrFrame)
	{
		mBufferedImageReceivers = new HashMap<>();
		mPhoneFeedFrame = phoneFeedFrame;
		mScreenshotFrame = screenshotFrame;
		mQRFrame = qrFrame;
	}

	@Override
	public void actionSuccess(String address)
	{
		synchronized (mObject)
		{
			if (!mBufferedImageReceivers.containsKey(address))
			{
				try
				{
					// TODO: Handle the case when the map grows too much.
					mBufferedImageReceivers.put(address,
							new BufferedImageReceiver(address, Constants.Ports.PICTURE_STREAM_SERVER_PORT));
				}
				catch (IOException e)
				{
					System.out.println("Error creating BufferedImageReceiver!");
					return;
				}
			}

			BufferedImageReceiver bufferedImageReceiver = mBufferedImageReceivers.get(address);
			BufferedImage receivedBufferedImage = bufferedImageReceiver.receive();

			if (receivedBufferedImage == null)
			{
				bufferedImageReceiver.close();
				mBufferedImageReceivers.remove(address);
				System.out.println("Closed image buffer receiver!");
				return;
			}

			receivedBufferedImage = ImageDistanceCalculator.resize(receivedBufferedImage);

			mPhoneFeedFrame.updateFrame(receivedBufferedImage);
			mQRFrame.setTitle("phone address: " + address);
			mQRFrame.displayInCorner();

			BufferedImage screenshotBufferedImage = mScreenshotFrame.getNewScreenshot();

			int comparisonResult = ImageComparerOpenCV.compare(receivedBufferedImage, screenshotBufferedImage);
			bufferedImageReceiver.sendInt(comparisonResult);
		}
	}

	@Override
	public void actionFailure()
	{
		synchronized (mObject)
		{
			// TODO: Create a timer that periodically clears the hash map? This
			// solution is simpler yet not efficient.
			clean();
		}
		mPhoneFeedFrame.hideFrame();
		mQRFrame.hideFrame();
	}

	private void clean()
	{
		for (BufferedImageReceiver bufferedImageReceiver : mBufferedImageReceivers.values())
		{
			bufferedImageReceiver.close();
		}

		mBufferedImageReceivers.clear();
	}
}
