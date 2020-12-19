package maquinarefrescs_davidsantome;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author David Santomé Galván
 *
 * La clase reposador define un reponedor que a cada iteración se encarga de
 * rellenar la máquina de refrescos hasta su máximo y notifica la operación que
 * ha realizado
 */
public class Reposador implements Runnable {

    Maquina monitor;

    /**
     * @param mon Tipo monitor
     */
    public Reposador(Maquina mon) {
        monitor = mon;
    }

    /**
     * Override método run
     */
    @Override
    public void run() {
        long id = Thread.currentThread().getId();

        // Notifica cuando ha llegado el reponedor 
        System.out.println("Reposador " + id + " arriba");

        // Mientras queden clientes 
        while (monitor.getNCons() != 0) {

            // Intenta rellenar la máquina hasta el máximo
            int operations = monitor.append(1);

            // Delay para la simulación
            try {
                Thread.sleep(300);
            } catch (InterruptedException ex) {
                Logger.getLogger(Reposador.class.getName()).log(Level.SEVERE, null, ex);
            }

            // Notifica la operación 
            if (operations > 0) {
                System.out.println("Reposador " + id + " reposa la màquina, hi ha " + (10 - operations) + " i en posa " + operations);
            }

            // Notifica que terminó de rellenar
            monitor.unlockReplenishing();
        }

        // Notifica que se va
        System.out.println("Reposador " + id + " se'n va");
    }
}
