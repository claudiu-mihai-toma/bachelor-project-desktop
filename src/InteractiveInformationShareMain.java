import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

// TODO: Make this a proper class that has a minimum amount of static code.
public class InteractiveInformationShareMain
{
	public static final boolean RUN_MAIN = true;

	public static void compare()
	{
		BufferedImage img1 = Utils.getBufferedImage("test/1");
		BufferedImage img2 = Utils.getBufferedImage("test/2");

		double res1 = ImageDistanceCalculator.compare(img1, img2);
		int res2 = ImageComparerOpenCV.compare(img1, img2, 45);

		System.out.println("res1 = [" + res1 + "]");
		System.out.println("res2 = [" + res2 + "]");
		
		img1 = EdgeDetectedScreenshotFrame.toEdgeDetectedImage(img1);
		
		File file  = new File("test/3");
		try
		{
			ImageIO.write(img1, "png", file);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		if (RUN_MAIN)
		{
			BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
			Content contentToSend = null;

			LocalAddressGetter localAddressGetter = new LocalAddressGetter();
			String localAddress = localAddressGetter.getLocalAddress();

			Image qrCode = QRCodeGenerator.generateQRCode(localAddress);

			QRFrame qrFrame = new QRFrame();
			qrFrame.initialize(qrCode, localAddress);

			EdgeDetectedScreenshotFrame screenshotFrame = new EdgeDetectedScreenshotFrame();
			PhoneFeedFrame phoneFeedFrame = new PhoneFeedFrame();

			ContentTransferServer contentReceiverServer;
			ContentTransferServer contentSenderServer;
			BroadcastBeaconReceiver phonePictureBroadcastBeaconReceiver;

			ScheduledExecutorService phonePictureService = Executors.newScheduledThreadPool(1);

			try
			{
				contentReceiverServer = new ContentTransferServer(Constants.Ports.CONTENT_RECEIVER_PORT);
				contentSenderServer = new ContentTransferServer(Constants.Ports.CONTENT_SENDER_PORT);

				phonePictureBroadcastBeaconReceiver = new BroadcastBeaconReceiver(
						Constants.Ports.PICTURE_STREAM_BEACON_PORT, Constants.Timeouts.PHONE_IMAGE_SOCKET_TIMEOUT,
						new PhonePictureBeaconAction(phoneFeedFrame, screenshotFrame, qrFrame));
			}
			catch (IOException e)
			{
				e.printStackTrace();
				phonePictureService.shutdown();
				return;
			}
			phonePictureService.scheduleWithFixedDelay(phonePictureBroadcastBeaconReceiver, 0,
					Constants.Delays.PHONE_IMAGE_TIMER_DELAY, TimeUnit.MILLISECONDS);

			while (qrFrame.isDisplayable())
			{
				try
				{
					if (inputReader.ready())
					{
						System.out.println("Input ready!");
						String title = inputReader.readLine();
						contentToSend = handleTitle(title);
						System.out.println("Title = [" + title + "]; sending " + contentToSend.getType());

						System.out.flush();
					}
				}
				catch (IOException e)
				{
					// e.printStackTrace();
				}

				if (contentToSend != null)
				{
					// System.out.println("Sending content...");
					boolean sendSucceeded = contentSenderServer.send(contentToSend);
					if (sendSucceeded)
					{
						System.out.println("Content sent.");
						// After sending content reenter in receive mode.
						contentToSend = null;
					}
				}
				{
					Content content = contentReceiverServer.receive();
					handleContent(content);
				}
				screenshotFrame.updateScreenshot();
			}

			contentReceiverServer.close();
			contentSenderServer.close();
			phonePictureService.shutdown();
		}
		else
		{
			compare();
		}
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
		System.out.println("text content received = [" + stringContent + "]\n");
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

		System.out.println("image content received = [" + fileName + "]\n");
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
		if (title == null || title.length() == 0)
		{
			return null;
		}

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
				title = Utils.getFileName(title);
			}
			catch (IOException e)
			{
				// e.printStackTrace();
			}
		}

		return new Content(type, title, data);
	}
}