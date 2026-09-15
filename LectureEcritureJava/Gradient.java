public class Gradient {
    public static void main(String[] args) {
        int width = 200;
        int height = 100;
        Image img = new Image(200, 100);

        // Génération du dégradé de bleu
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Interpolation linéaire du bleu entre 0 et 255 selon x
                int bleu = (x * 255) / (width - 1);
                img.setPixel(x, y, 0, 0, bleu);
            }
        }

        try {
            img.save("gradient.ppm");
            System.out.println("Dégradé créé avec succès !");
        } catch (Exception e) {
            System.err.println("Erreur lors de la création du dégradé : " + e.getMessage());
        }
    }
}