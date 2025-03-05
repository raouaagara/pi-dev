package tools;

public class MySMTP {
    private String smtpHost; // Serveur SMTP (exemple : smtp.gmail.com)
    private String smtpPort; // Port SMTP (exemple : 587)
    private String username; // Nom d'utilisateur SMTP (exemple : votre@gmail.com)
    private String password; // Mot de passe SMTP (ou mot de passe d'application)

    private static MySMTP instance; // Instance unique de MySMTP

    // Constructeur privé pour empêcher l'instanciation directe
    private MySMTP() {
        // Initialisation des valeurs par défaut (vous pouvez les charger depuis un fichier de configuration)
        this.smtpHost = "smtp.gmail.com"; // Exemple avec Gmail
        this.smtpPort = "587"; // Port TLS
        this.username = "ecosport3a30@gmail.com"; // Remplacez par votre adresse e-mail
        this.password = "xaxr qfdq eavw gsrg"; // Remplacez par votre mot de passe
    }

    // Méthode pour obtenir l'instance unique de MySMTP
    public static MySMTP getInstance() {
        if (instance == null) {
            instance = new MySMTP();
        }
        return instance;
    }

    // Getters pour les propriétés SMTP
    public String getSmtpHost() {
        return smtpHost;
    }

    public String getSmtpPort() {
        return smtpPort;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}