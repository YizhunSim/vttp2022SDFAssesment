package vttp2022.task02.client;

import java.io.IOException;

public class Main {
  public static void main(String[] args) throws IOException{
    HttpClientConnection client = new HttpClientConnection();

    client.start();
  }
}
