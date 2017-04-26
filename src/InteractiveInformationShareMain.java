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

		ContentReceiver contentReceiver;
		BroadcastBeaconReceiver broadcastBeaconReceiver;
		Timer timer = new Timer();
		try
		{
			contentReceiver = new ContentReceiver();
			broadcastBeaconReceiver = new BroadcastBeaconReceiver(localAddress, qrFrame);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return;
		}
		timer.schedule(broadcastBeaconReceiver, 0, BroadcastBeaconReceiver.SOCKET_TIMEOUT);

		ScreenshotFrame screenshotFrame = new ScreenshotFrame();
		
		while (qrFrame.isDisplayable())
		{
			String content = contentReceiver.getContent();
			if (content != null)
			{
				System.out.println("CONTENT: [" + content + "]");
			}
			
			screenshotFrame.updateScreenshot();
		}
		
		timer.cancel();
		contentReceiver.close();
		broadcastBeaconReceiver.close();
	}
}