package eu.telecomnancy.labfx;

import eu.telecomnancy.labfx.messagerie.Conversation;
import eu.telecomnancy.labfx.messagerie.Message;
import java.util.Map;
import java.util.HashMap;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.sql.Timestamp;
import java.util.Comparator;





public class MessagerieController {

    private SkeletonController skeleton_controller;
    private User currentUser;
    private Conversation currentConversation;
    private ArrayList<Conversation> allConversations;
    private ObservableList<String> listOfContacts;
    private ArrayList<Message> listOfCurrentMessages;
    private String initialContact;
    private String initialMessage;
    private Map<String, Integer> unreadMessageCountMap = new HashMap<>(); // Utiliser une Map pour conserver le nombre de messages non lus pour chaque conversation
    
    @FXML private ListView<String> listContact;
    @FXML private TextField pseudoContact;
    @FXML private Label contactPseudoLabel;
    @FXML private ListView<Message> messageList;
    @FXML private TextField responseField;
    @FXML private Button sendButton;
    @FXML private Label noConv;
    @FXML private Label noSelectConv;
    @FXML private VBox convActive;

    public void setSkeletonController(SkeletonController skeleton_controller){
        this.skeleton_controller = skeleton_controller;
    }

    public void setInitialContact(String contactPseudo) {
        this.initialContact = contactPseudo;
    }

    public void setInitialMessage(String messageText) {
        this.initialMessage = messageText;
    }

    public void initialize() {
        currentUser = Main.getCurrentUser();
        
    }

    public void initializeListContact() {
        loadConversations();
        listContact.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null) {
                    setCurrentConversation(newValue);
                }
            }
        });
        listContact.setStyle("-fx-cursor: hand;");


        if (initialContact != null && !initialContact.isEmpty()) {
            // Créer une conversation avec le contact initial
            createConversationWith(initialContact);
            listContact.getSelectionModel().select(initialContact);
            setCurrentConversation(initialContact);
            if(initialMessage != null && !initialMessage.isEmpty()) {
                sendMessage(initialMessage);
            }
            initialContact = null; // Réinitialiser après utilisation
            initialMessage = null;
        }
        else {
            // Sélectionner le premier contact par défaut
            listContact.getSelectionModel().selectFirst();
            setCurrentConversation(listContact.getSelectionModel().getSelectedItem());
        }
    
    }

    //Accès à la dataBase
    // Fonction qui permet de récupérer la liste des conversations depuis la dataBase de l'utilisateur courant en fonction de son Id
    public ArrayList<Conversation> getAllConversations(int userId) {
        String sql = "SELECT conversation_id, participant1_id, participant2_id FROM conversations WHERE participant1_id = ? OR participant2_id = ?";
    
        try (Connection conn = DataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setInt(2, userId);
    
            ResultSet rs = pstmt.executeQuery();
            ArrayList<Conversation> allConversations = new ArrayList<>();
    
            while (rs.next()) {
                int conversationId = rs.getInt("conversation_id");
                int participant1Id = rs.getInt("participant1_id");
                int participant2Id = rs.getInt("participant2_id");
    
                Conversation conversation = new Conversation(conversationId, participant1Id, participant2Id);
                allConversations.add(conversation);
            }
    
            return allConversations;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    //Fonction qui permet de créer une conversation dans la dataBase entre 2 utilisateurs
    public static void addConversation(int participant1Id, int participant2Id) {
        String sql = "INSERT INTO conversations (participant1_id, participant2_id) VALUES (?, ?)";
    
        try (Connection conn = DataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
            pstmt.setInt(1, participant1Id);
            pstmt.setInt(2, participant2Id);
            pstmt.executeUpdate();
    
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Fonction qui permet de récupérer l'Id d'un utilisateur en fonction de son pseudo
    public static Integer getUserIdByPseudo(String pseudo) {
        String sql = "SELECT id FROM profil WHERE pseudo = ?";

        try (Connection conn = DataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, pseudo);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            } else {
                return null; // Aucun utilisateur trouvé avec ce pseudo
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    //Fonction qui permet de récupérer le pseudo d'un utilisateur en fonction de son Id
    public static String getUserPseudoById(int userId) {
        String sql = "SELECT pseudo FROM profil WHERE id = ?";

        try (Connection conn = DataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("pseudo");
            } else {
                return null; // Aucun utilisateur trouvé avec cet Id
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    // Fonction qui permet de vérifier si une conversation existe déjà entre 2 utilisateurs
    private boolean doesConversationExist(int userId1, int userId2) {
        String sql = "SELECT conversation_id FROM conversations WHERE (participant1_id = ? AND participant2_id = ?) OR (participant1_id = ? AND participant2_id = ?)";
    
        try (Connection conn = DataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
            pstmt.setInt(1, userId1);
            pstmt.setInt(2, userId2);
            pstmt.setInt(3, userId2);
            pstmt.setInt(4, userId1);
    
            ResultSet rs = pstmt.executeQuery();
    
            return rs.next(); // Retourne vrai si la conversation existe déjà
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    // Fonction qui permet de récupérer la liste des messages  d'une conversation en fonction de son Id depuis la base de donnée
    private ArrayList<Message> getCurrentMessages(int conversationId) {
        ArrayList<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM messages WHERE conversation_id = ? ORDER BY timestamp";
    
        try (Connection conn = DataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
            pstmt.setInt(1, conversationId);
            ResultSet rs = pstmt.executeQuery();
    
            while (rs.next()) {
                int messageId = rs.getInt("message_id");
                int senderId = rs.getInt("sender_id");
                int receiver_id = rs.getInt("receiver_id");
                String messageText = rs.getString("message_text");
                Timestamp timestamp = rs.getTimestamp("timestamp");
                boolean isRead = rs.getBoolean("is_read");
    
                messages.add(new Message(messageId, conversationId, senderId, receiver_id, messageText, timestamp, isRead));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving messages: " + e.getMessage());
        }
        return messages;
    }
    
    
    
    
    // Fonction qui permet d'ajouter un message à la base de donnée
    private void addMessageToDatabase(int conversationId, int senderId, int receiver_id, String messageText) {
        String sql = "INSERT INTO messages (conversation_id, sender_id, receiver_id, message_text, timestamp) VALUES (?, ?, ?, ?, ?)";
    
        try (Connection conn = DataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
            pstmt.setInt(1, conversationId);
            pstmt.setInt(2, senderId);
            pstmt.setInt(3, receiver_id);
            pstmt.setString(4, messageText);
            pstmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            pstmt.executeUpdate();
    
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Fonction qui permet de marquer tous les messages d'une conversation comme lus pour l'utilisateur actuel
    private void markMessagesAsRead(int conversationId) {
        String sql = "UPDATE messages SET is_read = TRUE WHERE conversation_id = ? AND sender_id != ? AND is_read = FALSE";

        try (Connection conn = DataBase.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, conversationId);
            pstmt.setInt(2, currentUser.getId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error updating message read status: " + e.getMessage());
        }
    }


    // Fonction qui permet de compter le nombre de messages non lus pour une conversation donnée
    private int countUnreadMessages(int conversationId, int userId) {
        String sql = "SELECT COUNT(*) FROM messages WHERE conversation_id = ? AND receiver_id = ? AND is_read = FALSE";
        
        try (Connection conn = DataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
            pstmt.setInt(1, conversationId);
            pstmt.setInt(2, userId);
        
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error counting unread messages: " + e.getMessage());
        }
        return 0;
    }

    // Fonction qui permet de supprimer une conversation de la base de donnée
    private void deleteConversation(int conversationId) {
        String sqlDeleteMessages = "DELETE FROM messages WHERE conversation_id = ?";
        String sqlDeleteConversation = "DELETE FROM conversations WHERE conversation_id = ?";
    
        try (Connection conn = DataBase.getConnection();
             PreparedStatement pstmtMessages = conn.prepareStatement(sqlDeleteMessages);
             PreparedStatement pstmtConversation = conn.prepareStatement(sqlDeleteConversation)) {
    
            // Supprimer les messages
            pstmtMessages.setInt(1, conversationId);
            pstmtMessages.executeUpdate();
    
            // Supprimer la conversation
            pstmtConversation.setInt(1, conversationId);
            pstmtConversation.executeUpdate();
    
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de la conversation : " + e.getMessage());
        }
    }
    

    
    
    


    // Fonction qui permet de récupérer la listes des conversations de l'utilisateur courant et de les afficher dans la liste des contacts
    private void loadConversations() {
        allConversations = getAllConversations(currentUser.getId());

        if (allConversations.isEmpty()) {
            noConv.setVisible(true);
            noSelectConv.setVisible(true);
            convActive.setVisible(false);
            listContact.setVisible(false);


        } else {
            noConv.setVisible(false);
            noSelectConv.setVisible(false);
            convActive.setVisible(true);
            listContact.setVisible(true);
        }
        allConversations.sort(Comparator.comparingInt(Conversation::getConversationId).reversed()); // Trier par ID de conversation

        for (Conversation conversation : allConversations) {
            int otherParticipantId = (conversation.getParticipant1Id() == currentUser.getId()) ? 
                                    conversation.getParticipant2Id() : 
                                    conversation.getParticipant1Id();
            String otherParticipantPseudo = getUserPseudoById(otherParticipantId);

            // Compter les messages non lus
            int unreadMessages = countUnreadMessages(conversation.getConversationId(), currentUser.getId());
            unreadMessageCountMap.put(otherParticipantPseudo, unreadMessages);
        }

        listOfContacts = FXCollections.observableArrayList(unreadMessageCountMap.keySet());
        listContact.setItems(listOfContacts);

        // Utiliser un CellFactory personnalisé
        listContact.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    int unreadMessages = unreadMessageCountMap.getOrDefault(item, 0);
                    String displayText = item + (unreadMessages > 0 ? " (" + unreadMessages + " Non lu)" : "");
                    setText(displayText);
                    if (unreadMessages > 0 && !item.equals(contactPseudoLabel.getText())) {
                        setStyle("-fx-background-color: lightcoral;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });
    }


    // Fonction qui permet de créer une conversation entre l'utilisateur courant et un autre utilisateur
    private void createConversationWith(String contactPseudo) {
        Integer contactId = getUserIdByPseudo(contactPseudo);
        if (contactId != null && !doesConversationExist(currentUser.getId(), contactId)) {
            addConversation(currentUser.getId(), contactId);
            loadConversations();
            setCurrentConversation(contactPseudo);
        } else if (contactId == null) {
            System.out.println("Aucun utilisateur trouvé avec ce pseudo");
            skeleton_controller.flash("Aucun utilisateur trouvé avec ce pseudo", "red");
        } else {
            System.out.println("Une conversation existe déjà avec cet utilisateur, chargement de la conversation...");

        }
    }

    // Fonction qui permet de récupérer la conversation sélectionnée dans la liste des contacts
    private void setCurrentConversation(String conversationPseudo) {
        for (Conversation conversation : allConversations) {
            String participantPseudo = conversation.getParticipant1Id() == currentUser.getId() ? 
                                       getUserPseudoById(conversation.getParticipant2Id()) : 
                                       getUserPseudoById(conversation.getParticipant1Id());
            if (participantPseudo.equals(conversationPseudo)) {
                currentConversation = conversation;
                contactPseudoLabel.setText(participantPseudo); // Mettre à jour le label
                loadMessages(currentConversation.getConversationId()); // Charger les messages de la conversation
                unreadMessageCountMap.put(conversationPseudo, 0);
                listContact.refresh();
                break;
            }
        }
    }

    // Fonction qui permet de charger les messages de la conversation sélectionnée
    private void loadMessages(int conversationId) {
        markMessagesAsRead(conversationId); // Marquer les messages comme lus
        skeleton_controller.updateProfile();
        ArrayList<Message> messages = getCurrentMessages(conversationId);
        ObservableList<Message> observableMessages = FXCollections.observableArrayList(messages);
        messageList.setItems(observableMessages);
    
        setupMessageCellFactory(); // Configurer le CellFactory personnalisé pour les messages
        // Scroller jusqu'au dernier message
        if (!observableMessages.isEmpty()) {
            messageList.scrollTo(observableMessages.size() - 1);
        }
    }
    
    // Fonction qui permet d'envoyer un message
    private void sendMessage(String messageText) {
        int conversationId = currentConversation.getConversationId();
        int senderId = currentUser.getId(); // Assurez-vous que currentUser contient l'ID de l'utilisateur actuel
        int receiverId = (senderId == currentConversation.getParticipant1Id()) ? 
                         currentConversation.getParticipant2Id() : 
                         currentConversation.getParticipant1Id();
        addMessageToDatabase(conversationId, senderId, receiverId, messageText);
    
        loadMessages(conversationId); // Recharger les messages pour la conversation actuelle
    }

    // Méthode pour configurer le CellFactory de la ListView des messages
    private void setupMessageCellFactory() {
        messageList.setCellFactory(lv -> new ListCell<Message>() {
            @Override
            protected void updateItem(Message message, boolean empty) {
                super.updateItem(message, empty);
                if (empty || message == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    String senderPseudo = getUserPseudoById(message.getSenderId());
                    String status = message.isRead() ? "Lu" : "Remis";
                    String messageText = message.getMessageText();

                    Label pseudoLabel = new Label(senderPseudo + " :");
                    pseudoLabel.setFont(new Font("Arial", 14)); // Vous pouvez ajuster la police et la taille selon vos besoins

                    Label messageLabel = new Label(messageText + " (" + status + ")");
                    messageLabel.setWrapText(true);
                    messageLabel.setMaxWidth(lv.getWidth() - 20);

                    VBox vbox = new VBox(pseudoLabel, messageLabel);
                    vbox.setSpacing(5); // Espace entre le pseudo et le message

                    setGraphic(vbox);
                }
            }
        });
    }






    //Gestion des boutons:
    @FXML
    public void handleSend() {
        String messageText = responseField.getText();
        if (messageText != null && !messageText.trim().isEmpty()) {
            sendMessage(messageText);
            responseField.clear(); // Effacer le champ après envoi
        } else {
            System.out.println("Le message ne peut pas être vide.");
            skeleton_controller.flash("Le message ne peut pas être vide.", "red");
        }
    }

    @FXML
    public void handleCreateConversation() {
        String pseudoContactText = pseudoContact.getText();
        if (pseudoContactText != null && !pseudoContactText.trim().isEmpty()) {
            // Logique pour créer une conversation avec le pseudo récupéré
            createConversationWith(pseudoContactText);

        } else {
            // Gérer le cas où le champ est vide
            System.out.println("Veuillez entrer un pseudo valide.");
            skeleton_controller.flash("Veuillez entrer un pseudo valide.", "red");
        }
    }

    @FXML
    public void handleDeleteConversation() {
        if (currentConversation != null) {
            deleteConversation(currentConversation.getConversationId());
            skeleton_controller.loadMessageriePage();
        } else {
            System.out.println("Aucune conversation sélectionnée.");
        }
    }


}
