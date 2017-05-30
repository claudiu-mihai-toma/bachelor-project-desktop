import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ContentTransferServer extends AbstractTransferServer<Content>
{

	public ContentTransferServer(int port) throws IOException
	{
		super(port);
	}

	@Override
	public boolean send(Content content)
	{
		try
		{
			Socket socket = mServerSocket.accept();
			socket.setSoTimeout(Constants.Timeouts.DATA_TRANSFER_SOCKET_TIMEOUT);

			DataOutputStream os = new DataOutputStream(socket.getOutputStream());

			os.writeInt(content.getType().getId());
			os.writeUTF(content.getTitle());
			byte[] data = content.getData();
			if (data != null)
			{
				os.writeInt(data.length);
				os.write(data);
			}

			os.close();
			socket.close();
			return true;
		}
		catch (IOException e)
		{
			// e.printStackTrace();
		}
		return false;
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

			is.close();
			socket.close();

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
