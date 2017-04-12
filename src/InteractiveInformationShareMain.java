import java.awt.Image;
import java.net.SocketException;
import java.net.UnknownHostException;

public class InteractiveInformationShareMain
{
	public static void main(String[] args)
	{
		LocalAddressGetter localAddressGetter = new LocalAddressGetter();
		String localAddress = localAddressGetter.getLocalAddress();

		Image qrCode = QRCodeGenerator.generateQRCode(localAddress);

		QRFrame qrFrame = new QRFrame();
		qrFrame.display(qrCode, localAddress);

		ContentReceiver contentReceiver;
		try
		{
			contentReceiver = new ContentReceiver(localAddress);
		}
		catch (SocketException | UnknownHostException e)
		{
			e.printStackTrace();
			return;
		}

		while (qrFrame.isDisplayable())
		{
			String content = contentReceiver.getContent();
			if (content != null)
			{
				System.out.println(content);
			}
		}

		contentReceiver.close();
	}
}