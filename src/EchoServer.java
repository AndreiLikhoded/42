import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EchoServer {


    private final int port;
    private final ExecutorService pool = Executors.newCachedThreadPool();

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
            }
        }catch (IOException e){
            System.out.printf("Connection is failed, port %s is busy", port);
            e.printStackTrace();
        }
    }
}