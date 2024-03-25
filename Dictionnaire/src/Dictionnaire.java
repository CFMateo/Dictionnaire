import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Dictionnaire {
	
	// Constante pour définir la taille maximale de l'historique
    private static final int TAILLE_MAX_HISTORIQUE = 5;
    
    // Liste pour stocker l'historique des mots recherchés
    private static List<String> historiqueMots = new ArrayList<>();
    
    private static final int MIN_LONGUEUR_PREFIXE = 1; // Valeur minimale de la longueur du préfixe à considérer
	
    public static void main(String[] args) {
    	
    	Scanner scanner = new Scanner(System.in);
    	while (true) {
            // Affichage du menu
            System.out.println("Menu Principal :");
            System.out.println("1) Rechercher un mot");
            System.out.println("2) Afficher l'historique");
            System.out.println("3) Traduire un mot");
            System.out.println("4) Quitter");
            System.out.print("Entrez votre choix : ");

            int choix = scanner.nextInt();
            scanner.nextLine(); // Pour consommer la nouvelle ligne après la saisie de l'entier

            switch (choix) {
                case 1: // Afin de rechercher un mot dans le dictionnaire
                	rechercherMot(scanner);
                    break;
                case 2: // Afin d'afficher l'historique
                	afficherHistorique();
                    break;
                case 3: // Afin de traduire un mot 
                	traduireMot(scanner);
                    break;
                case 4:
                    System.out.println("Au revoir !");
                    System.exit(0); // Quitte le programme
                default:
                    System.out.println("Choix invalide !");
            }
            
            // Attendre que l'utilisateur appuie sur Entrée pour réafficher le menu
            System.out.println("Appuyez sur la touche Entree pour continuer.");
            String input = scanner.nextLine(); // Attendre l'entrée de l'utilisateur
            
            // Vérifier si la chaîne entrée est vide (seulement "Entrée" a été pressée)
            if (!input.isEmpty()) {
                System.out.println("Vous devez appuyer sur Entree pour continuer.");
                // Attendre jusqu'à ce que l'utilisateur appuie sur Entrée
                while (!scanner.nextLine().isEmpty()) {
                    // Attendre jusqu'à ce que l'utilisateur appuie sur Entrée
                }
            }
        }
    }
    
    
    
    
    public static void rechercherMot(Scanner scanner) {
        System.out.print("Entrez le mot que vous souhaitez chercher: ");
        String mot = scanner.nextLine().toLowerCase(); // Convertir le mot entré en minuscules
        
        // Ajouter le mot à l'historique
        ajouterAHistorique(mot);
        
        rechercheDictionnaire(mot,false);   
        
    }

    public static void traduireMot(Scanner scanner) {
        System.out.print("Entrez le mot que vous souhaitez traduire: ");
        String mot = scanner.nextLine().toLowerCase(); // Convertir le mot entré en minuscules
        
        rechercheDictionnaire(mot,true);
    }
    
    private static void rechercheDictionnaire(String mot, Boolean booltraduire) {
    	// Lecture du fichier CSV et recherche du mot
        try (BufferedReader br = new BufferedReader(new FileReader("dictionary.csv"))) {
            String line;
            List<String> motsTrouves = new ArrayList<>();
            int longueurMaxPrefixe = 0;
            
            while ((line = br.readLine()) != null) {
                String[] elements = line.split(",");
                String motDansDictionnaire = elements[0].toLowerCase(); // Convertir le mot du dictionnaire en minuscules
                
                if (motDansDictionnaire.equals(mot)) { // Utiliser equals() pour une comparaison sensible à la casse
                    // Le mot est trouvé, affichez ses détails
                    afficherDetails(elements,booltraduire);
                    return; // Sortir de la méthode car le mot est trouvé
                } else { // On va chercher des mots similaires, selon leurs préfixes
                    int longueurPrefixe = getLongueurPrefixe(mot, motDansDictionnaire);
                    if (longueurPrefixe > longueurMaxPrefixe && longueurPrefixe >= MIN_LONGUEUR_PREFIXE) {
                        longueurMaxPrefixe = longueurPrefixe;
                        motsTrouves.clear(); // Effacer les mots précédents avec un préfixe plus court
                    }
                    if (longueurPrefixe == longueurMaxPrefixe && longueurPrefixe >= MIN_LONGUEUR_PREFIXE) {
                        motsTrouves.add(elements[0]); // Ajouter le mot sans conversion en minuscules
                    }
                }
            }
            
            // Afficher les mots trouvés avec le préfixe le plus long
            if (!motsTrouves.isEmpty()) {
                System.out.println("Aucun resultat trouve pour '" + mot + "'. \nVoici des mots similaires:");
                for (String motTrouve : motsTrouves) {
                    System.out.println("- " + motTrouve);
                }
            } else {
                System.out.println("Aucun mot similaire trouve pour '" + mot + "'.");
            }
            
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier: " + e.getMessage());
        }
        
        System.out.println(" ");
    }

    
    
    private static void afficherDetails(String[] elements, Boolean booltraduire) {
        String francais = elements[1];
        String type = elements[2];
        String sens = elements[3];
        System.out.println("Mot: " + elements[0]);
        System.out.println("Traduction en français: " + francais);
        if(!booltraduire) {
	        System.out.println("Type: " + type);
	        System.out.println("Sens: " + sens);
        }
    }
    
    private static int getLongueurPrefixe(String mot, String motDansDictionnaire) {
        int longueurCommune = 0;
        int minLength = Math.min(mot.length(), motDansDictionnaire.length());
        for (int i = 0; i < minLength; i++) {
            if (mot.charAt(i) != motDansDictionnaire.charAt(i)) {
                break;
            }
            longueurCommune++;
        }
        return longueurCommune;
    }
    
    private static void ajouterAHistorique(String mot) {
        // Ajouter le mot à l'historique
        historiqueMots.add(mot);
        
        // Si la taille de l'historique dépasse la taille maximale, supprimer le premier élément
        if (historiqueMots.size() > TAILLE_MAX_HISTORIQUE) {
            historiqueMots.remove(0);
        }
    }
    
    public static void afficherHistorique() {
        System.out.println("Historique des derniers mots recherches:");
        for (int i = historiqueMots.size() - 1; i >= 0; i--) {
            System.out.println("- " + historiqueMots.get(i));
        }
        System.out.println(" ");
    }

}