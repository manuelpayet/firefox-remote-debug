import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class Main {
	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		final Main main = new Main();
		final Socket socket = main.connect("localhost", 1234);
		
		new Thread() {
			public void run() {
				while (true) {
					try {
						
						if(socket.getInputStream().available()>0) {
							final Map<String, Object> readPayload = main.readPayload(socket.getInputStream());
							if(readPayload.containsKey("tabs")) {
								final String threadActor = (String) ((List<Map<String, Object>>)readPayload.get("tabs")).get(0).get("actor");
								System.out.println("threadActor=" + threadActor);
								main.attachToThread(socket.getOutputStream(), threadActor);
							}
							System.out.println(readPayload);
						}
						
					} catch (IOException e) {
						e.printStackTrace();
					} 
				}
			};
		}.start();

//		System.out.println(main.readPayload(socket.getInputStream()));
		
		main.listTabs(socket.getOutputStream());
		while(socket.isConnected()) {
			Thread.sleep(1000);
		}
//		System.out.println(main.readPayload(socket.getInputStream()));
//		System.out.println(main.readPayload(socket.getInputStream()));

	}

	public Socket connect(final String hostname, final int port) throws UnknownHostException, IOException {
		final Socket socket = new Socket(hostname, port);
		return socket;
	}

	private int getPayloadLength(final InputStream inputStream) throws IOException {
		char currentChar;
		final StringBuilder builder = new StringBuilder(3);

		while (':' != (currentChar = (char) inputStream.read())) {
			builder.append(currentChar);
		}
		return Integer.valueOf(builder.toString());
	}

	private final Gson gson = new Gson();

	private Map<String, Object> readPayload(final InputStream inputStream) throws IOException {
		System.out.println(getPayloadLength(inputStream));
		final JsonReader jsonReader = new JsonReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));

		final Map<String, Object> capabilities = gson.fromJson(jsonReader, Map.class);
		return capabilities;
	}

	private void attachToThread(final OutputStream outputStream, final String actor) throws IOException {
		sendMessage(outputStream, actor, "attach");
	}
	private void listTabs(final OutputStream outputStream) throws IOException {
		sendMessage(outputStream, "root", "listTabs");
	}

	private void sendMessage(final OutputStream outputStream, final String actor, final String action)
			throws IOException {
		final Map<String, Object> payload = new HashMap<>();
		payload.put("to", actor);
		payload.put("type", action);
		final String payloadString = gson.toJson(payload);
		final String payloadLength = String.format("%d:%s", payloadString.length(), payloadString);
		outputStream.write((payloadLength).getBytes());
	}
}
