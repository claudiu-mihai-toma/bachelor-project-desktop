import java.awt.Image;

public class InteractiveInformationShareMain
{
	public static void main(String[] args)
	{
		LocalAddressGetter localAddressGetter = new LocalAddressGetter();
		String localAddress = localAddressGetter.getLocalAddress();
		
		Image qrCode = QRCodeGenerator.generateQRCode(localAddress);
		
		QRFrame qrFrame = new QRFrame();
		qrFrame.display(qrCode);
	}
}