package eu.telecomnancy.labfx.messagerie;

public class Conversation {
    //Classe qui représente une conversation entre deux utilisateurs, elles sont affiché dans la liste des conversations qui est la partie gauche
    private int conversationId;
    private int participant1Id;
    private int participant2Id;

    // Constructeurs
    public Conversation(int conversation_id, int participant1_id, int participant2_id){
        this.conversationId = conversation_id;
        this.participant1Id = participant1_id;
        this.participant2Id = participant2_id;
    }


    // Getters et setter de conversationId
    public int getConversationId() {
        return conversationId;
    }
    public void setConversationId(int conversationId) {
        this.conversationId = conversationId;
    }

    // Getters et setter de participant1Id
    public int getParticipant1Id() {
        return participant1Id;
    }
    public void setParticipant1Id(int participant1Id) {
        this.participant1Id = participant1Id;
    }

    // Getters et setter de participant2Id
    public int getParticipant2Id() {
        return participant2Id;
    }
    public void setParticipant2Id(int participant2Id) {
        this.participant2Id = participant2Id;
    }





}
