package maquinarefrescs_davidsantome;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author David Santomé Galván
 *
 * La clase client define un cliente que a cada iteración consumirá un refresco
 * notifica el número de refrescos totales que ha consumido
 */
public class Client implements Runnable {

    Maquina monitor;
    int operations;
    String name;
    int nConsum = 0;

    /**
     * @param mon Tipo monitor
     * @param ops Tipo int que define el número de consumiciones
     * @param nm Tipo String que define el nombre del cliente
     */
    public Client(Maquina mon, int ops, String nm) {
        monitor = mon;
        operations = ops;
        name = nm;
    }

    /**
     * Override método run
     */
    @Override
    public void run() {

        // Notifica cuando ha llegado el cliente
        System.out.println("    " + name + " arriba i farà " + operations + " consumicions");
        for (int i = 0; i < operations; i++) {

            // Mientras haya reponedores
            if (monitor.getNRepos() != 0) {

                // Consume un refresco
                monitor.take();

                // Delay para la simulación
                try {
                    Thread.sleep(150);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
                nConsum++;

                // Notifica el número de consumiciones totales ha realizado
                System.out.println("    " + name + " agafa un refresc - consumició: " + nConsum);
            } else {
                operations = 0;

                // Se va avisando de que no hay reponedores
                System.out.println(name + ": Aquí no hi ha ningú per la màquina!!!!");
            }
        }

        // Termina el cliente
        monitor.removeC();
        System.out.println("--->" + name + " se'n va, queden " + monitor.getNCons() + " clients");
    }
}
