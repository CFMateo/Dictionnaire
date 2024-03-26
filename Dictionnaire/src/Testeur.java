
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * Classe pour tester le dictionnaire.
 */
public class Testeur {
    /**
     * Méthode principale pour exécuter les tests.
     * @param args Les arguments de la ligne de commande (non utilisés).
     */
    public static void main(String[] args) {
        effectuerTests();
    }

    /**
     * Méthode qui lit le fichier "testPrefix.txt" et génére le fichier texte de sortie 
     * "testPrefixResult.txt" selon le nombre de mots dans le dictionnaire ayant la plus
     * longue sous-chaîne en commun présente dans le mot de la requête.
     */
    public static void effectuerTests() {
        try (BufferedReader br = new BufferedReader(new FileReader("testPrefix.txt"));
             PrintWriter writer = new PrintWriter("testPrefixResult.txt")) {
            String line;
            while ((line = br.readLine()) != null) {
                String mot = line.trim();
                Object result = Dictionnaire.rechercheDictionnaire(mot, "tester");
                if (result != null && result instanceof Integer) {
                    int nombreMotsSimilaires = ((Integer) result).intValue();
                    writer.println(nombreMotsSimilaires);
                } else {
                    // Gérer le cas où le mot est dans le dictionnaire:
                    writer.println(mot + " existe dans le dictionnaire. " );
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier de tests: " + e.getMessage());
        }
    }
	}
