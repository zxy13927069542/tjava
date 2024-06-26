package com.ying.tjava.network.tcp;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

public class SocketClient {
	
	@Test
	public void testConnect() throws UnknownHostException, IOException {
		SocketClient.connect(9966);
	}
	
	public static void connect(int port) throws UnknownHostException, IOException {
		Socket socket = new Socket("127.0.0.1", port);
		try (InputStream in = socket.getInputStream();
				OutputStream out = socket.getOutputStream()) {
			handle(in, out);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		} finally {
			socket.close();
			
		}
		
	}

	public static void handle(InputStream input, OutputStream output) throws IOException {
		Writer writer = new OutputStreamWriter(output,StandardCharsets.UTF_8);
		BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
		Scanner sc = new Scanner(System.in);
		
		String welcome = reader.readLine();
		System.out.println(welcome);
		
		while(true) {
			String msg = sc.nextLine();
			writer.write(msg+ "\n");
			writer.flush();
			if (msg.equals("quit")) {
				break;
			}
		}
	}
}
