package maquinarefrescs_davidsantome;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author David Santomé Galván
 *
 * Enlace al vídeo:
 *
 * Segunda práctica evaluable de programación concurrente. En este ejercicio se
 * pretende simular una máquina de refrescos de capacidad máxima 10, con unos
 * clientes y unos reponedores. En caso de no haber reponedores se irán los
 * clientes, en caso de no haber clientes los reponedores terminarán y en caso
 * de haber ambos se irán alternando los clientes y los reponedores.
 */
public class MaquinaRefrescs_DavidSantome {

    // Tamaño de la máquina de refrescos
    static final int BUFFER_SIZE = 10;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // Generamos un número aleatorio de clientes y reponedores
        Random r = new Random();
        int producers = r.nextInt(3);
        int consumers = r.nextInt(10);

        // Mensajes del inicio del programa
        System.out.println("COMENÇA LA SIMULACIÓ");
        System.out.println("Avui hi ha " + consumers + " i " + producers + " reposadors");
        System.out.println("La màquina de refrescs està buida, hi caben " + BUFFER_SIZE + " refrescs");

        int t = 0, i;
        Thread[] threads = new Thread[producers + consumers];
        Maquina monitor = new Maquina(BUFFER_SIZE, consumers, producers);

        // Iniciamos los consumidores
        for (i = 0; i < consumers; i++) {
            try {

                // Obtenemos un nombre al azar de un fichero de nombres (510 líneas)
                int indxNom = r.nextInt(510);
                BufferedReader fNoms = new BufferedReader(new FileReader("LlistaNoms.txt"));
                String name = "";
                for (int j = 0; j < indxNom; j++) {
                    name = fNoms.readLine();
                }
                fNoms.close();

                // Generamos de manera aleatoria el número de consumiciones
                int to_consume = r.nextInt(3) + 1;
                threads[t] = new Thread(new Client(monitor, to_consume, name));
                threads[t].start();
                t++;
            } catch (IOException ex) {
                Logger.getLogger(MaquinaRefrescs_DavidSantome.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // Iniciamos los productores
        for (i = 0; i < producers; i++) {
            threads[t] = new Thread(new Reposador(monitor));
            threads[t].start();
            t++;
        }

        // Esperamos a todos los procesos
        for (i = 0; i < producers + consumers; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
            }
        }
    }
}
