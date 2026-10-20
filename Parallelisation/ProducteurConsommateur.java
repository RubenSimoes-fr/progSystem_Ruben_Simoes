import java.util.concurrent.Semaphore;
import java.util.LinkedList;
import java.util.Queue;

public class ProducteurConsommateur {

    private static final int TAILLE_BUFFER = 5;
    private final Integer[] buffer = new Integer[TAILLE_BUFFER];

    private final Semaphore placesLibres = new Semaphore(TAILLE_BUFFER);
    private final Semaphore elementsDisponibles = new Semaphore(0);  
    private final Semaphore mutexBuffer = new Semaphore(1); // quel erreur ici ? vous pouvez le résoudre plus tard

    class Producteur implements Runnable {
        public void run() {
            for (int i = 0; i < 10; i++) {
                int produit = genererProduit();

                // TODO: Attendre une place libre
                placesLibres.acquire();
                // TODO: Acquérir l'accès exclusif au buffer
                mutexBuffer.acquire();  
                // TODO: Ajouter l'élément au buffer
                if (placesLibres > 0) {
                    buffer[placesLibres] = produit;

                }
                // TODO: Libérer l'accès au buffer
                mutexBuffer.release();
                // TODO: Signaler qu'un élément est disponible
                placesLibres.release();
            }
        }
    }
    class Consommateur implements Runnable {
    public void run() {
        for (int i = 0; i < 7; i++) {
            // TODO: Attendre qu'un élément soit disponible
            elementsDisponibles.acquire();
            // TODO: Acquérir l'accès exclusif au buffer
            mutexBuffer.acquire();
            // TODO: Retirer un élément du buffer
            if (elementsDisponibles > 0) {
                bInteger produit = buffer.poll();
            }
            // TODO: Libérer l'accès au buffer
            mutexBuffer.release();
            // TODO: Signaler qu'une place est libre
            placesLibres.release();
        }
    }
}
}