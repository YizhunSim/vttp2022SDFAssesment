package vttp2022.task02.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class HttpClientConnection {
	private int port = 80;
	private String host = "68.183.239.26";

  private final String name = "SIM YIZHUN";
  private final String email = "sim.yizhun@gmail.com";

  private int clientTimeOut15s = 15000;

	private Socket sock;
	private InputStream is;
	private ObjectInputStream ois;
	private OutputStream os;
	private ObjectOutputStream oos;

	public HttpClientConnection(){
	}

	public void start() throws UnknownHostException, IOException{
		try{
			System.out.printf("Connecting to %s on port %d...\n", host, port);
			this.sock = new Socket(host, port);
			System.out.println("Connected");

			System.out.println("Initialising Streams A");
		  os = sock.getOutputStream();

      System.out.println("Initialising Streams B");
      is = sock.getInputStream();

      System.out.println("Initialising Streams C");
      oos = new ObjectOutputStream(os);

      System.out.println("Initialising Streams D");
		  ois = new ObjectInputStream(is);

      sock.setSoTimeout(clientTimeOut15s);

			String request = ois.readUTF();

      // Perform some operation on the request
			List<String> requestTerms = parseRequestResponse(request);

			// System.out.println(requestTerms);
      String requestId = requestTerms.get(0);
      String name = requestTerms.get(1);
      String email = requestTerms.get(2);
      String averageResult = requestTerms.get(3);

      oos.writeUTF(requestId);
      oos.writeUTF(name);
      oos.writeUTF(email);
      oos.writeFloat(Float.parseFloat(averageResult));

      oos.flush();
      String response = ois.readUTF();

      if (Boolean.parseBoolean(response)){
        System.out.println("SUCCESS");
      } else{
        response = ois.readUTF();
        System.out.println("FAILED: " + response);
      }
			close();
			sock.close();
		} catch (IOException ex){
			System.err.println(ex);
			ex.printStackTrace();
		}
	}

  private List<String> parseRequestResponse(String request){
    List<String> result = new ArrayList<>();
    String[] terms = request.split(" ");
    String requestId = terms[0];

    String[] numbers = terms[1].split(",");
    float sum = 0;
    float average = 0;
    for (int i = 1; i < numbers.length; i++){
      sum += Integer.parseInt(numbers[i]);
    }
    average = sum / numbers.length;
    result.add(requestId);
    result.add(this.name);
    result.add(this.email);
    result.add(Float.toString(average));
    return result;
  }

	private void close() throws IOException {
		is.close();
		os.close();
	}

}