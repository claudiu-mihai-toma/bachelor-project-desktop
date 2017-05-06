import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class ImageDistanceCalculator
{
	public static final int	SCALE_WIDTH		= 300;
	public static final int	SCALE_HEIGHT	= 300;

	public static double compare(BufferedImage image1, BufferedImage image2)
	{
		BufferedImage img1 = resize(image1);
		BufferedImage img2 = resize(image2);

		long diff = 0;
		for (int y = 0; y < SCALE_HEIGHT; y++)
		{
			for (int x = 0; x < SCALE_WIDTH; x++)
			{
				int rgb1 = img1.getRGB(x, y);
				int rgb2 = img2.getRGB(x, y);
				int r1 = (rgb1 >> 16) & 0xff;
				int g1 = (rgb1 >> 8) & 0xff;
				int b1 = (rgb1) & 0xff;
				int r2 = (rgb2 >> 16) & 0xff;
				int g2 = (rgb2 >> 8) & 0xff;
				int b2 = (rgb2) & 0xff;
				diff += Math.abs(r1 - r2);
				diff += Math.abs(g1 - g2);
				diff += Math.abs(b1 - b2);
			}
		}
		double n = SCALE_WIDTH * SCALE_HEIGHT * 3;
		double p = diff / n / 255.0;

		return p;
	}

	public static BufferedImage resize(BufferedImage img)
	{
		return resize(crop(img), SCALE_WIDTH, SCALE_HEIGHT);
	}

	public static BufferedImage resize(BufferedImage img, int newW, int newH)
	{
		Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
		BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();

		return dimg;
	}

	public static BufferedImage crop(BufferedImage src)
	{
		BufferedImage dest;
		int height = src.getHeight();
		int width = src.getWidth();

		int halfDiff = Math.abs(height - width) / 2;

		if (height > width)
			dest = src.getSubimage(0, halfDiff, width, width);
		else
			dest = src.getSubimage(halfDiff, 0, height, height);

		return dest;
	}
}
