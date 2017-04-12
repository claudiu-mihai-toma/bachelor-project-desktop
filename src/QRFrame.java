import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.security.InvalidParameterException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class QRFrame extends JFrame
{
	private static final long serialVersionUID = 1L;
	private static final String DEFAULT_TITLE = "Local IP Address";

	public void display(Image image)
	{
		display(image, DEFAULT_TITLE);
	}

	public void display(Image image, String title)
	{
		if (image == null)
		{
			throw new InvalidParameterException();
		}
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		panel.setBackground(Color.BLACK);
		ImageIcon icon = new ImageIcon(image);
		JLabel label = new JLabel();
		label.setIcon(icon);
		panel.add(label);
		this.getContentPane().add(panel);
		this.pack();
		this.setTitle(title);
		
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
        Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
        int x = (int) rect.getMaxX() - this.getWidth();
        int y = (int) rect.getMaxY() - this.getHeight();
        this.setLocation(x, y);
        this.setVisible(true);
	}
}