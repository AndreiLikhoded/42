import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.LinkedList;

public class EchoServer {


    private final int port;
    private final ExecutorService pool = Executors.newCachedThreadPool();
    public static LinkedList<Socket> serverList = new LinkedList<>();

    private EchoServer(int port){
        this.port = port;
    }

    public static EchoServer bindToPort(int port){
        return new EchoServer(port);
    }

    public void run(){
        try(var server = new ServerSocket(port)){
            while(!server.isClosed()){
                Socket clientSocket = server.accept();
                pool.submit(() -> ServerWork.handle(clientSocket));
                serverList.add(clientSocket);
            }

        }catch (IOException e){
            System.out.printf("Connection is failed, port %s is busy", port);
            e.printStackTrace();
        }
    }
    private static String makeName(){
        String name = String.valueOf(serverList.size());
        return name;
    }
}