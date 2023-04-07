import javax.swing.JFileChooser;
import java.net.*;
import java.io.*;

public class Cliente {
    public static void main(String[] args) {
        try {
            // Leer la dirección del servidor y el puerto desde la entrada estándar
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.printf("Escriba la dirección del servidor:");
            String host = br.readLine();
            System.out.printf("\n\nEscriba el puerto:");
            int pto = Integer.parseInt(br.readLine());

            // Establecer conexión con el servidor
            Socket cl = new Socket(host, pto);

            // Crear un selector de archivos y permitir la selección de múltiples archivos
            JFileChooser jf = new JFileChooser();
            jf.setMultiSelectionEnabled(true);
            int r = jf.showOpenDialog(null);

            // Comprobar si se han seleccionado archivos
            if (r == JFileChooser.APPROVE_OPTION) {
                File[] files = jf.getSelectedFiles();
                DataOutputStream dos = new DataOutputStream(cl.getOutputStream());

                // Enviar la cantidad de archivos seleccionados al servidor
                dos.writeInt(files.length);
                dos.flush();

                // Enviar cada archivo seleccionado al servidor
                for (File f : files) {
                    String archivo = f.getAbsolutePath();
                    String nombre = f.getName();
                    long tam = f.length();

                    // Enviar el nombre del archivo y su tamaño al servidor
                    DataInputStream dis = new DataInputStream(new FileInputStream(archivo));
                    dos.writeUTF(nombre);
                    dos.flush();
                    dos.writeLong(tam);
                    dos.flush();

                    // Leer y enviar el contenido del archivo al servidor
                    byte[] b = new byte[1024];
                    long enviados = 0;
                    int porcentaje, n;
                    while (enviados < tam) {
                        n = dis.read(b);
                        dos.write(b, 0, n);
                        dos.flush();
                        enviados = enviados + n;
                        porcentaje = (int) (enviados * 100 / tam);
                        System.out.print("Enviado: " + nombre + " " + porcentaje + "%\r");
                    } // While

                    // Cerrar el flujo de entrada del archivo y mostrar el mensaje de archivo
                    // enviado
                    dis.close();
                    System.out.print("\n\nArchivo enviado: " + nombre);
                }

                // Cerrar los flujos de salida y la conexión con el servidor
                dos.close();
                cl.close();
            } // if
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
