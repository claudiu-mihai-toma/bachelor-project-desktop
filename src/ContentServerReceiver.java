import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ContentServerReceiver extends AbstractServerReceiver<Content>
{

	public ContentServerReceiver(int port) throws IOException
	{
		super(port);
	}

	@Override
	public Content receive()
	{
		try
		{
			Socket socket = mServerSocket.accept();
			socket.setSoTimeout(Constants.Timeouts.DATA_TRANSFER_SOCKET_TIMEOUT);
			DataInputStream is = new DataInputStream(socket.getInputStream());

			int contentTypeId = is.readInt();
			String contentTitle = is.readUTF();
			byte[] contentData = null;
			Content.ContentType contentType = null;

			switch (contentTypeId)
			{
				case Constants.ContentTypeIDs.TEXT:
					contentType = Content.ContentType.TEXT;
					break;
				case Constants.ContentTypeIDs.IMAGE:
					contentType = Content.ContentType.IMAGE;
					int contentSize = is.readInt();
					contentData = new byte[contentSize];
					is.readFully(contentData);
					break;
			}

			Content receivedContent = new Content(contentType, contentTitle, contentData);

			return receivedContent;

		}
		catch (IOException e)
		{
			// e.printStackTrace();
		}

		return null;
	}

}
