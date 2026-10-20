public class TestCompteur {
    public static void main(String[] args) throws InterruptedException {
        int nbExecutions = 50;
        int nbThreads = 9;
        int nbIncrementsParThread = 1_111_111;
        int valeurAttendue = nbThreads * nbIncrementsParThread;

        int succes = 0;
        int minObtenu = Integer.MAX_VALUE;
        int maxObtenu = Integer.MIN_VALUE;

        System.out.println("Lancement des " + nbExecutions + " tests (Attendu : " + valeurAttendue + ")...");

        for (int i = 1; i <= nbExecutions; i++) {
            CompteurDangereux.resetCompteur();
            Thread[] threads = new Thread[nbThreads];

            for (int t = 0; t < nbThreads; t++) {
                threads[t] = new Thread(new CompteurDangereux.Incrementeur("Thread-" + (t + 1), nbIncrementsParThread));
                threads[t].start();
            }

            for (Thread t : threads) {
                t.join();
            }

            int resultat = CompteurDangereux.getCompteur();

            if (resultat == valeurAttendue) {
                succes++;
            }

            if (resultat < minObtenu) minObtenu = resultat;
            if (resultat > maxObtenu) maxObtenu = resultat;

            System.out.printf("Test %2d/%d | Résultat : %d | Pertes : %d%n",
                    i, nbExecutions, resultat, (valeurAttendue - resultat));
        }

        double tauxSucces = (succes / (double) nbExecutions) * 100.0;

        System.out.println("\n=== Statistiques globales ===");
        System.out.println("Nombre d'exécutions     : " + nbExecutions);
        System.out.println("Nombre de réussites     : " + succes + " / " + nbExecutions);
        System.out.printf("Taux de réussite        : %.2f %%\n", tauxSucces);
        System.out.println("Valeur minimale obtenue : " + minObtenu);
        System.out.println("Valeur maximale obtenue : " + maxObtenu);
        System.out.println("Perte maximale          : " + (valeurAttendue - minObtenu) + " incréments");
    }
}