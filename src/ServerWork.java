import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ServerWork {


    public static void handle(Socket socket){

        System.out.printf("Client is on: %s%n", socket);

        try(Scanner reader = getReader(socket);
            PrintWriter writer = getWriter(socket);
            socket){
            sendResponse("Hello " + socket, writer);
            while(true){
                String message = reader.nextLine().strip();
                System.out.printf("Got: %s%n", message);

                if(isEmptyMsg(message) || isQuitMsg(message)){
                    break;
                }
                for (var r : EchoServer.serverList) {
                    sendResponse(message.toUpperCase(), getWriter(r));
                    if(!writer.equals(EchoServer.serverList)){
                        sendResponse(message.toUpperCase(), getWriter(r));
                    }
                }
            }
        }catch (NoSuchElementException e){
            System.out.println("Client dropped the connection!");
        }catch (IOException ex){
            ex.printStackTrace();
            System.out.printf("Client is off: %s%n", socket);
        }
    }

    private static PrintWriter getWriter(Socket socket) throws IOException{
        OutputStream stream = socket.getOutputStream();
        return new PrintWriter(stream);
    }

    private static Scanner getReader(Socket socket) throws IOException{
        InputStream stream = socket.getInputStream();
        InputStreamReader input = new InputStreamReader(stream, "UTF-8");
        return new Scanner(input);
    }

    private static boolean isQuitMsg(String message){
        return "bye".equalsIgnoreCase(message);
    }

    private static boolean isEmptyMsg(String message){
        return message == null || message.isBlank();
    }

    private static void sendResponse(String response, Writer writer) throws IOException{
        writer.write(response);
        writer.write(System.lineSeparator());
        writer.flush();
    }
}
