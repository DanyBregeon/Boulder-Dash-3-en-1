package outils;

import java.io.File;
import java.io.PrintWriter;

public class Ecrivain {
    public static void ecrire(String aEcrire, String nom, String repertoire) {
        try {
            File dir = new File(repertoire);
            dir.mkdirs();
            File destinationFile = new File(repertoire + nom);
            destinationFile.createNewFile();
            PrintWriter ecrivain = new PrintWriter(repertoire + nom, "UTF-8");
            ecrivain.print(aEcrire);
            System.out.println("Chemin parcouru enregistre sous : " + repertoire + nom);
            ecrivain.close();
        } catch (Exception e) {
            System.err.println("Impossible d'enregistrer le chemin parcouru");
        }
    }
}
