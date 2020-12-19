package maquinarefrescs_davidsantome;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @author David Santomé Galván
 *
 * La clase maquina define una máquina de refrescos que tiene una cantidad de
 * refrescos, de clientes, de reponedores. Se pueden introducir y retirar
 * refrescos de ella.
 */
public class Maquina {

    int size;
    volatile int nClients;
    volatile int nRepos;
    volatile boolean replenishing = false;
    Deque<Integer> buffer = new LinkedList<>();

    /**
     * @param size Tipo int que define la capacidad de la máquina
     * @param nC Tipo int que define la cantidad de clientes
     * @param nR Tipo int que define la cantidad de reponedores
     */
    public Maquina(int size, int nC, int nR) {
        this.size = size;
        this.nClients = nC;
        this.nRepos = nR;
    }

    /**
     * Método para consumir un refresco
     */
    synchronized public void take() {
        
        // Mientras la máquina esté vacía o estén rellenando
        while (buffer.isEmpty() || replenishing) {
            
            // Bloqueamos al cliente
            try {
                this.wait();
            } catch (InterruptedException e) {
            }
        }
        
        // Consume un refresco y lo notifica 
        buffer.remove();
        this.notifyAll();
    }

    /**
     * Método para notificar que hay un cliente menos
     */
    synchronized public void removeC() {
        nClients--;
        this.notifyAll();
    }

    /**
     * @param data tipo Integer que define un supuesto refresco
     *
     * Método para reponer un refresco
     *
     * @return total Tipo int que representa el número de refrescos repuestos
     */
    synchronized public int append(Integer data) {
        
        // Mientras la máquina esté llena y haya clientes
        while (buffer.size() == size && nClients > 0) {
            
            // Bloqueamos al reponedor
            try {
                this.wait();
            } catch (InterruptedException e) {
            }
        }
        
        // Notifica que está rellenando
        replenishing = true;
        this.notifyAll();
        int total = 0;
        
        // Rellena con refrescos la máquina hasta el máximo de la capacidad
        while (buffer.size() != 10) {
            buffer.add(data);
            total++;
        }
        return total;
    }

    /**
     * Método para notificar que no se está reponiendo
     */
    synchronized public void unlockReplenishing() {
        replenishing = false;
        this.notifyAll();
    }

    /**
     * Método para obtener el número de clientes que quedan
     *
     * @return nClients
     */
    synchronized public int getNCons() {
        return nClients;
    }

    /**
     * Método para obtener el número de reponedores que quedan
     *
     * @return nRepos
     */
    synchronized public int getNRepos() {
        return nRepos;
    }
}
