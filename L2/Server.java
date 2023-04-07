import java.net.*;
import java.io.*;

public class Server {
    public static void main(String[] args) {
        try {
            // Crear un socket de servidor y escuchar en el puerto 7000
            ServerSocket s = new ServerSocket(7000);

            // Bucle infinito para aceptar conexiones de clientes
            for (;;) {
                // Aceptar una conexión de cliente
                Socket cl = s.accept();
                System.out.println("Conexión establecida desde" + cl.getInetAddress() + ":" + cl.getPort());

                // Crear un flujo de entrada para recibir datos del cliente
                DataInputStream dis = new DataInputStream(cl.getInputStream());

                // Leer la cantidad de archivos que se recibirán desde el cliente
                int numFiles = dis.readInt();

                // Recibir cada archivo enviado por el cliente
                for (int i = 0; i < numFiles; i++) {
                    byte[] b = new byte[1024];
                    String nombre = dis.readUTF();
                    System.out.println("Recibimos el archivo:" + nombre);
                    long tam = dis.readLong();

                    // Crear un flujo de salida para guardar el archivo recibido en el servidor
                    DataOutputStream dos = new DataOutputStream(new FileOutputStream(nombre));
                    long recibidos = 0;
                    int n, porcentaje;

                    // Leer y guardar el contenido del archivo enviado por el cliente
                    while (recibidos < tam) {
                        int restantes = (int) Math.min(1024, tam - recibidos);
                        n = dis.read(b, 0, restantes);
                        dos.write(b, 0, n);
                        dos.flush();
                        recibidos = recibidos + n;
                        porcentaje = (int) (recibidos * 100 / tam);
                        System.out.print("Recibido: " + nombre + " " + porcentaje + "%\r");
                    } // While

                    // Cerrar el flujo de salida del archivo y mostrar el mensaje de archivo
                    // recibido
                    System.out.println("\n\nArchivo recibido: " + nombre);
                    dos.close();
                }

                // Cerrar el flujo de entrada y la conexión con el cliente
                dis.close();
                cl.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } // catch
    }
}
