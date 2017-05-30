import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class InteractiveInformationShareMain
{

	public static void main(String[] args)
	{
		Content contentToSend = handleArgs(args);

		LocalAddressGetter localAddressGetter = new LocalAddressGetter();
		String localAddress = localAddressGetter.getLocalAddress();

		Image qrCode = QRCodeGenerator.generateQRCode(localAddress);

		QRFrame qrFrame = new QRFrame();
		qrFrame.initialize(qrCode, localAddress);

		EdgeDetectedScreenshotFrame screenshotFrame = new EdgeDetectedScreenshotFrame();
		PhoneFeedFrame phoneFeedFrame = new PhoneFeedFrame();

		ContentTransferServer contentTransferServer;
		BroadcastBeaconReceiver phonePictureBroadcastBeaconReceiver;

		ScheduledExecutorService phonePictureService = Executors.newScheduledThreadPool(1);

		try
		{
			contentTransferServer = new ContentTransferServer(Constants.Ports.CONTENT_RECEIVER_PORT);

			phonePictureBroadcastBeaconReceiver = new BroadcastBeaconReceiver(
					Constants.Ports.PICTURE_STREAM_BEACON_PORT, Constants.Timeouts.PHONE_IMAGE_SOCKET_TIMEOUT,
					new PhonePictureBeaconAction(phoneFeedFrame, screenshotFrame, qrFrame));
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return;
		}
		phonePictureService.scheduleWithFixedDelay(phonePictureBroadcastBeaconReceiver, 0,
				Constants.Delays.PHONE_IMAGE_TIMER_DELAY, TimeUnit.MILLISECONDS);

		while (qrFrame.isDisplayable())
		{
			if (contentToSend != null)
			{
				// System.out.println("Sending content...");
				boolean sendSucceeded = contentTransferServer.send(contentToSend);
				if (sendSucceeded)
				{
					System.out.println("Content sent.");
					contentToSend = null;
				}
			}
			else
			{
				Content content = contentTransferServer.receive();
				handleContent(content);
			}
			screenshotFrame.updateScreenshot();
		}

		phonePictureService.shutdown();
	}

	public static void handleContent(Content content)
	{
		if (content != null)
		{
			switch (content.getType())
			{
				case IMAGE:
					handleImageContent(content);
					break;
				case TEXT:
					handleTextContent(content);
					break;
				default:
					break;
			}
		}
	}

	public static void handleTextContent(Content content)
	{
		String stringContent = content.getTitle();
		System.out.println("content = [" + stringContent + "]\n");
	}

	public static void handleImageContent(Content content)
	{
		String fileName = content.getTitle();
		byte[] data = content.getData();

		FileOutputStream fos = null;
		try
		{
			fos = new FileOutputStream(fileName);
			fos.write(data);
			fos.close();

		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		System.out.println("content = [" + fileName + "]\n");
	}

	public static Content handleArgs(String[] args)
	{
		String title;
		if (args.length != 0)
		{
			if (args.length > 1)
			{
				throw new RuntimeException(
						"Too many arguments. Received " + args.length + " arguments instead of 0 or 1.");
			}

			System.out.println(args[0]);
			title = args[0];
			return handleTitle(title);
		}
		return null;
	}

	public static Content handleTitle(String title)
	{
		File file = new File(title);
		byte[] data = null;
		Content.ContentType type = Content.ContentType.TEXT;

		if (file.isFile())
		{
			Path path = Paths.get(title);
			try
			{
				data = Files.readAllBytes(path);
				type = Content.ContentType.IMAGE;
			}
			catch (IOException e)
			{
				// e.printStackTrace();
			}
		}

		return new Content(type, title, data);
	}
}