import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;


public class GymApp extends JFrame {
    private JTable table;
    private JButton btnAddMember, btnRenewSubscription, btnScheduleClass, btnRegisterToClass, btnMarkPresence, btnAddEquipment;
    private Connection conn;
    private DefaultTableModel model, coachModel, sessionModel;
    private JTable tableCoaches;
    private JTable tableSessions;
    

    // Constructeur de l'application avec l'interface graphique
    public GymApp() {
        // Connexion à la base de données
        conn = connect(); // Connexion via la méthode connect() intégrée dans cette classe

        // Configuration de la fenêtre principale
        setTitle("Gestion de Salle de Sport");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Create a main panel with BoxLayout to hold all three tables
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Créer le tableau pour afficher les membres
        String[] columns = {"ID", "Nom", "Prénom", "Email", "Abonnement"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        JPanel memberPanel = new JPanel(new BorderLayout());
        memberPanel.add(new JLabel("---- Membres ----"), BorderLayout.NORTH);
        memberPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(memberPanel);

        // Table for Coaches
        String[] coachColumns = {"ID Coach", "Nom", "Prénom", "Specialité", "Téléphone", "Email", "Tarif Séance"};
        coachModel = new DefaultTableModel(coachColumns, 0);
        tableCoaches = new JTable(coachModel);
        JScrollPane scrollPaneCoaches = new JScrollPane(tableCoaches);
        JPanel coachPanel = new JPanel(new BorderLayout());
        coachPanel.add(new JLabel("---- Coaches ----"), BorderLayout.NORTH);
        coachPanel.add(scrollPaneCoaches, BorderLayout.CENTER);
        mainPanel.add(coachPanel);

        // Table for Sessions
        String[] sessionColumns = {"ID Séance", "Nom Séance", "Activité", "Coach", "Date", "Heure Début", "Heure Fin", "Capacité Max", "Inscriptions"};
        sessionModel = new DefaultTableModel(sessionColumns, 0);
        tableSessions = new JTable(sessionModel);
        JScrollPane scrollPaneSessions = new JScrollPane(tableSessions);
        JPanel sessionPanel = new JPanel(new BorderLayout());
        sessionPanel.add(new JLabel("---- Scheduled Sessions ----"), BorderLayout.NORTH);
        sessionPanel.add(scrollPaneSessions, BorderLayout.CENTER);
        mainPanel.add(sessionPanel);

        add(mainPanel, BorderLayout.CENTER);

        // Créer le panneau avec les boutons
        JPanel panel = new JPanel();
        btnAddMember = new JButton("Ajouter Membre");
        btnRenewSubscription = new JButton("Renouveler Abonnement");
        btnScheduleClass = new JButton("Planifier un Cours");
        
        btnMarkPresence = new JButton("Marquer Présence");
        btnAddEquipment = new JButton("Ajouter Equipement");
        panel.add(btnAddMember);
        panel.add(btnRenewSubscription);
        panel.add(btnScheduleClass);
        
        panel.add(btnMarkPresence);
        panel.add(btnAddEquipment);
        add(panel, BorderLayout.SOUTH);

        // Action des boutons
        btnAddMember.addActionListener(e -> openAddMemberDialog());
        btnRenewSubscription.addActionListener(e -> openRenewSubscriptionDialog());
        btnScheduleClass.addActionListener(e -> openScheduleClassDialog());
        
        btnMarkPresence.addActionListener(e -> openMarkPresenceDialog());
        btnAddEquipment.addActionListener(e -> openAddEquipmentDialog());

        // Afficher les membres au démarrage
        displayMembers();
        displayCoaches();
        displaySessions();
    }

    // Méthode pour afficher les membres
    private void displayMembers() {
        try {
            if (conn != null) {
                String query = "SELECT * FROM MEMBRE";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    String[] row = {
                        String.valueOf(rs.getInt("id_membre")),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("id_abonnement")
                    };
                    model.addRow(row);
                }
            } else {
                System.out.println("La connexion à la base de données a échoué !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
     private void displayCoaches() {
        try {
            if (conn != null) {
                String query = "SELECT * FROM COACH";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    String[] row = {
                        String.valueOf(rs.getInt("id_coach")),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("specialite"),
                        rs.getString("telephone"),
                        rs.getString("email"),
                        String.valueOf(rs.getDouble("tarif_seance"))
                    };
                    coachModel.addRow(row);
                }
            } else {
                System.out.println("La connexion à la base de données a échoué !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour afficher les séances
    private void displaySessions() {
        try {
            if (conn != null) {
                String query = "SELECT * FROM SEANCE";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    String[] row = {
                        String.valueOf(rs.getInt("id_seance")),
                        rs.getString("nom_seance"),
                        rs.getString("type_activite"),
                        String.valueOf(rs.getInt("id_coach")),  // You can also join to get the coach name here
                        rs.getString("date_seance"),
                        rs.getString("heure_debut"),
                        rs.getString("heure_fin"),
                        String.valueOf(rs.getInt("capacite_max")),
                        String.valueOf(rs.getInt("nb_inscrits"))
                    };
                    sessionModel.addRow(row);
                }
            } else {
                System.out.println("La connexion à la base de données a échoué !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

    // Ouvrir une fenêtre pop-up pour ajouter un membre
    private void openAddMemberDialog() {
        JDialog dialog = new JDialog(this, "Ajouter un Membre", true);
        dialog.setLayout(new GridLayout(6, 2));

        JTextField txtNom = new JTextField();
        JTextField txtPrenom = new JTextField();
        JTextField txtEmail = new JTextField();
        JComboBox<String> comboAbonnement = new JComboBox<>(new String[] { "Formule Basique", "Formule Premium" });
        JButton btnSubmit = new JButton("Ajouter");

        dialog.add(new JLabel("Nom:"));
        dialog.add(txtNom);
        dialog.add(new JLabel("Prénom:"));
        dialog.add(txtPrenom);
        dialog.add(new JLabel("Email:"));
        dialog.add(txtEmail);
        dialog.add(new JLabel("Abonnement:"));
        dialog.add(comboAbonnement);
        dialog.add(new JLabel());
        dialog.add(btnSubmit);

        btnSubmit.addActionListener(e -> addMember(txtNom.getText(), txtPrenom.getText(), txtEmail.getText(), comboAbonnement.getSelectedItem().toString()));

        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // Ajouter un membre à la base de données
    private void addMember(String nom, String prenom, String email, String abonnement) {
        try {
            String query = "INSERT INTO MEMBRE (nom, prenom, email, id_abonnement) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nom);
            stmt.setString(2, prenom);
            stmt.setString(3, email);
            stmt.setInt(4, abonnement.equals("Formule Basique") ? 1 : 2);  // Abonnement 1 pour Basique, 2 pour Premium
            stmt.executeUpdate();
            model.addRow(new Object[]{null, nom, prenom, email, abonnement});
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Ouvrir une fenêtre pour renouveler un abonnement
    private void openRenewSubscriptionDialog() {
        JDialog dialog = new JDialog(this, "Renouveler Abonnement", true);
        dialog.setLayout(new GridLayout(3, 2));

        JTextField txtIdMembre = new JTextField();
        JTextField txtNewEndDate = new JTextField();
        JButton btnSubmit = new JButton("Renouveler");

        dialog.add(new JLabel("ID Membre:"));
        dialog.add(txtIdMembre);
        dialog.add(new JLabel("Nouvelle Date de Fin:"));
        dialog.add(txtNewEndDate);
        dialog.add(new JLabel());
        dialog.add(btnSubmit);

        btnSubmit.addActionListener(e -> renewSubscription(Integer.parseInt(txtIdMembre.getText()), txtNewEndDate.getText()));

        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // Renouveler un abonnement
    private void renewSubscription(int idMembre, String newEndDate) {
        try {
            String query = "UPDATE MEMBRE SET date_fin_abonnement = ? WHERE id_membre = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, newEndDate);
            stmt.setInt(2, idMembre);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Abonnement renouvelé avec succès pour le membre ID: " + idMembre, "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Ouvrir une fenêtre pour planifier un cours
    private void openScheduleClassDialog() {
    JDialog dialog = new JDialog(this, "Planifier un Cours", true);
    dialog.setLayout(new GridLayout(0, 1, 8, 8)); // ONE column, vertical, spacing

    JTextField txtCourseName = new JTextField();
    JTextField txtCoach = new JTextField();
    JTextField txtDate = new JTextField();
    JTextField txtStartTime = new JTextField();
    JTextField txtEndTime = new JTextField();
    JTextField txtCapacity = new JTextField();
    JButton btnSubmit = new JButton("Planifier");

    dialog.add(new JLabel("Nom du Cours:"));
    dialog.add(txtCourseName);

    dialog.add(new JLabel("Coach (Nom Prénom):"));
    dialog.add(txtCoach);

    dialog.add(new JLabel("Date de Séance (yyyy-MM-dd):"));
    dialog.add(txtDate);

    dialog.add(new JLabel("Heure de Début (HH:mm:ss):"));
    dialog.add(txtStartTime);

    dialog.add(new JLabel("Heure de Fin (HH:mm:ss):"));
    dialog.add(txtEndTime);

    dialog.add(new JLabel("Capacité:"));
    dialog.add(txtCapacity);

    dialog.add(btnSubmit);

    btnSubmit.addActionListener(e -> {
        scheduleClass(
            txtCourseName.getText(),
            txtCoach.getText(),
            txtDate.getText(),
            txtStartTime.getText(),
            txtEndTime.getText(),
            Integer.parseInt(txtCapacity.getText())
        );
        dialog.dispose();
    });

    dialog.setSize(400, 420);
    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);
}

   

    // Planifier un cours
    private void scheduleClass(String courseName, String coach, String date, String startTime, String endTime, int capacity) {
    try {
        // Get coach ID from the coach name (You might want to adjust this part based on your logic)
        String queryCoachId = "SELECT id_coach FROM COACH WHERE nom = ? AND prenom = ?";
        PreparedStatement stmtCoach = conn.prepareStatement(queryCoachId);
        stmtCoach.setString(1, coach.split(" ")[0]);  // First part of the coach name (assuming first name)
        stmtCoach.setString(2, coach.split(" ")[1]);  // Second part of the coach name (assuming last name)
        ResultSet rsCoach = stmtCoach.executeQuery();
        
        int coachId = 0;
        if (rsCoach.next()) {
            coachId = rsCoach.getInt("id_coach");
        }

        // Insert into the SEANCE table
        String query = "INSERT INTO SEANCE (nom_seance, type_activite, id_coach, date_seance, heure_debut, heure_fin, capacite_max, nb_inscrits) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        
        stmt.setString(1, courseName);  // Course name
        stmt.setString(2, "Yoga");      // You can set the activity type here; assuming it is Yoga for simplicity
        stmt.setInt(3, coachId);        // Coach ID (from the coach table)
        stmt.setString(4, date);        // Date of the session (in 'yyyy-MM-dd' format)
        stmt.setString(5, startTime);   // Start time (in 'HH:mm:ss' format)
        stmt.setString(6, endTime);     // End time (in 'HH:mm:ss' format)
        stmt.setInt(7, capacity);       // Capacity of the session
        stmt.setInt(8, 0);              // Default number of people registered (0 initially)

        stmt.executeUpdate();
        JOptionPane.showMessageDialog(this, "Séance planifiée avec succès : " + courseName, "Succès", JOptionPane.INFORMATION_MESSAGE);
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Erreur lors de la planification de la séance.", "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}


    // Ouvrir une fenêtre pour inscrire un membre à un cours
    private void openRegisterToClassDialog() {
        JDialog dialog = new JDialog(this, "S'inscrire à un Cours", true);
        dialog.setLayout(new GridLayout(3, 2));

        JTextField txtIdMembre = new JTextField();
        JTextField txtCourseId = new JTextField();
        JButton btnSubmit = new JButton("S'inscrire");

        dialog.add(new JLabel("ID Membre:"));
        dialog.add(txtIdMembre);
        dialog.add(new JLabel("ID Cours:"));
        dialog.add(txtCourseId);
        dialog.add(new JLabel());
        dialog.add(btnSubmit);

        btnSubmit.addActionListener(e -> registerToClass(Integer.parseInt(txtIdMembre.getText()), Integer.parseInt(txtCourseId.getText())));

        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // Inscrire un membre à un cours
    private void registerToClass(int idMembre, int courseId) {
        try {
            String query = "INSERT INTO COURSE_REGISTRATIONS (id_membre, id_cours) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, idMembre);
            stmt.setInt(2, courseId);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Membre ID " + idMembre + " inscrit avec succès au cours ID " + courseId, "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Ouvrir une fenêtre pour marquer la présence d'un membre à un cours
    private void openMarkPresenceDialog() {
        JDialog dialog = new JDialog(this, "Marquer Présence", true);
        dialog.setLayout(new GridLayout(3, 2));

        JTextField txtIdMembre = new JTextField();
        JTextField txtCourseId = new JTextField();
        JButton btnSubmit = new JButton("Marquer Présence");

        dialog.add(new JLabel("ID Membre:"));
        dialog.add(txtIdMembre);
        dialog.add(new JLabel("ID Cours:"));
        dialog.add(txtCourseId);
        dialog.add(new JLabel());
        dialog.add(btnSubmit);

        btnSubmit.addActionListener(e -> markPresence(Integer.parseInt(txtIdMembre.getText()), Integer.parseInt(txtCourseId.getText())));

        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // Marquer la présence d'un membre
    private void markPresence(int idMembre, int courseId) {
        try {
            String query = "UPDATE COURSE_REGISTRATIONS SET presence = TRUE WHERE id_membre = ? AND id_cours = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, idMembre);
            stmt.setInt(2, courseId);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Présence marquée avec succès pour le membre ID " + idMembre + " au cours ID " + courseId, "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Ouvrir une fenêtre pour ajouter un équipement
    private void openAddEquipmentDialog() {
        JDialog dialog = new JDialog(this, "Ajouter un Équipement", true);
        dialog.setLayout(new GridLayout(3, 2));

        JTextField txtEquipmentName = new JTextField();
        JTextField txtQuantity = new JTextField();
        JButton btnSubmit = new JButton("Ajouter");

        dialog.add(new JLabel("Nom de l'équipement:"));
        dialog.add(txtEquipmentName);
        dialog.add(new JLabel("Quantité:"));
        dialog.add(txtQuantity);
        dialog.add(new JLabel());
        dialog.add(btnSubmit);

        btnSubmit.addActionListener(e -> addEquipment(txtEquipmentName.getText(), Integer.parseInt(txtQuantity.getText())));

        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // Ajouter un équipement
    private void addEquipment(String equipmentName, int quantity) {
        try {
            String query = "INSERT INTO EQUIPMENTS (equipment_name, quantity) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, equipmentName);
            stmt.setInt(2, quantity);
            stmt.executeUpdate();
           JOptionPane.showMessageDialog(this, "Équipement ajouté avec succès : " + equipmentName + " (Quantité: " + quantity + ")", "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour se connecter à la base de données (avec la chaîne de connexion correcte)
    public static Connection connect() {
        try {
            // Charger le driver MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            // URL de connexion à la base de données
            String url = "jdbc:mysql://localhost:3307/GymManagement";
            String user = "root";
            String password = "root";

            // Retourner la connexion à la base de données
            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Méthode principale pour démarrer l'application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GymApp().setVisible(true));
    }
}
