import java.awt.Image;
import java.io.IOException;
import java.util.Timer;

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
		BroadcastBeaconReceiver qrBroadcastBeaconReceiver;
		BroadcastBeaconReceiver phonePictureBroadcastBeaconReceiver;

		Timer qrTimer = new Timer();
		Timer phonePictureTimer = new Timer();
		try
		{
			contentStringReceiver = new StringReceiver(Constants.Ports.CONTENT_RECEIVER_PORT);
			qrBroadcastBeaconReceiver = new BroadcastBeaconReceiver(Constants.Ports.QR_BEACON_PORT,
					Constants.QR_SOCKET_TIMEOUT, new QRBeaconAction(qrFrame));

			phonePictureBroadcastBeaconReceiver = new BroadcastBeaconReceiver(
					Constants.Ports.PICTURE_STREAM_BEACON_PORT, Constants.PHONE_IMAGE_SOCKET_TIMEOUT,
					new PhonePictureBeaconAction(phoneFeedFrame, screenshotFrame));
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return;
		}
		qrTimer.schedule(qrBroadcastBeaconReceiver, 0, Constants.QR_TIMER_PERIOD);
		phonePictureTimer.schedule(phonePictureBroadcastBeaconReceiver, 0, Constants.PHONE_IMAGE_TIMER_PERIOD);

		while (qrFrame.isDisplayable())
		{
			contentStringReceiver.receive();
			screenshotFrame.updateScreenshot();
		}

		qrTimer.cancel();
	}
}