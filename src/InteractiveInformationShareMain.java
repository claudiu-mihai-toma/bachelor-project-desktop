import java.awt.Image;
import java.awt.image.BufferedImage;
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

		EdgeDetectedScreenshotFrame screenshotFrame = new EdgeDetectedScreenshotFrame();
		PhoneFeedFrame phoneFeedFrame = new PhoneFeedFrame();
		boolean phoneFeedFrameInitialized = false;

		//int iteration = 0;

		while (qrFrame.isDisplayable())
		{
			/*
			 * String content = contentReceiver.getStringContent(); if (content
			 * != null) { System.out.println("CONTENT: [" + content + "]"); }
			 */
			BufferedImage contentImage = contentReceiver.getImageContent();
			screenshotFrame.updateScreenshot();

			if (contentImage != null)
			{
				contentImage = ImageDistanceCalculator.resize(contentImage);
				contentImage = EdgeDetectedScreenshotFrame.toEdgeDetectedImage(contentImage);

				if (!phoneFeedFrameInitialized)
				{
					phoneFeedFrame.initialize(contentImage);
				}
				else
				{
					phoneFeedFrame.updateFrame(contentImage);
				}

				BufferedImage cameraImage = screenshotFrame.getNewScreenshot();
				
				ImageComparerOpenCV.compare(contentImage, cameraImage);
				
				/*double compareResult = ImageDistanceCalculator.compare(contentImage, cameraImage);

				System.out.println(compareResult);
				System.out.println();*/

				/*++iteration;

				FileOutputStream webFos = null;
				FileOutputStream phoneFos = null;
				PrintWriter compareFos = null;
				File file = null;
				try
				{

					file = new File("compare_results/" + iteration + "_web.jpeg");
					file.createNewFile();
					webFos = new FileOutputStream(file);

					file = new File("compare_results/" + iteration + "_phone.jpeg");
					file.createNewFile();
					phoneFos = new FileOutputStream(file);

					file = new File("compare_results/" + iteration + "_compare.txt");
					file.createNewFile();
					compareFos = new PrintWriter(file);
				}
				catch (FileNotFoundException e1)
				{
					e1.printStackTrace();
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try
				{
					ImageIO.write(cameraImage, "jpeg", webFos);
					ImageIO.write(contentImage, "jpeg", phoneFos);
					compareFos.println(compareResult);
					compareFos.flush();
				}
				catch (IOException e)
				{
				}*/
			}
		}

		timer.cancel();
		contentReceiver.close();
		broadcastBeaconReceiver.close();
	}
}