import java.net.*;
import java.io.*;

public class Server {
    public static void main(String args[]) {
        try {
            ServerSocket s = new ServerSocket(1234);

            System.out.println("Esperando cliente ...");

            for (;;) {
                Socket cl = s.accept();

                System.out.println("Conexién establecida desde" + cl.getInetAddress() + ":" + cl.getPort());

                String mensaje = "ACR 3CV13 Martinez Olivares Vicente Jafet";
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(cl.getOutputStream()));
                pw.println(mensaje);

                pw.flush();

                pw.close();
                cl.close();

            } // for

        } catch (Exception e) {
            e.printStackTrace();

        } // catch
    }// main
}