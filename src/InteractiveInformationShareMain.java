import java.awt.Image;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class InteractiveInformationShareMain
{

	public static void main(String[] args)
	{
		LocalAddressGetter localAddressGetter = new LocalAddressGetter();
		String localAddress = localAddressGetter.getLocalAddress();

		Image qrCode = QRCodeGenerator.generateQRCode(localAddress);

		QRFrame qrFrame = new QRFrame();
		qrFrame.initialize(qrCode, localAddress);

		EdgeDetectedScreenshotFrame screenshotFrame = new EdgeDetectedScreenshotFrame();
		PhoneFeedFrame phoneFeedFrame = new PhoneFeedFrame();

		StringReceiver contentStringReceiver;
		BroadcastBeaconReceiver phonePictureBroadcastBeaconReceiver;

		ScheduledExecutorService phonePictureService = Executors.newScheduledThreadPool(1);
		
		try
		{
			contentStringReceiver = new StringReceiver(Constants.Ports.CONTENT_RECEIVER_PORT);

			phonePictureBroadcastBeaconReceiver = new BroadcastBeaconReceiver(
					Constants.Ports.PICTURE_STREAM_BEACON_PORT, Constants.PHONE_IMAGE_SOCKET_TIMEOUT,
					new PhonePictureBeaconAction(phoneFeedFrame, screenshotFrame, qrFrame));
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return;
		}
		phonePictureService.scheduleWithFixedDelay(phonePictureBroadcastBeaconReceiver, 0, Constants.PHONE_IMAGE_TIMER_PERIOD, TimeUnit.MILLISECONDS);
		
		while (qrFrame.isDisplayable())
		{
			String content = contentStringReceiver.receive();

			if (content != null)
			{
				System.out.println("content = [" + content + "]\n");
			}

			screenshotFrame.updateScreenshot();
		}
		
		phonePictureService.shutdown();
	}
}