package graphql.client;

import java.util.concurrent.CountDownLatch;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class GQLSubscrition extends TextWebSocketHandler {
	    private static final String WEBSOCKET_URI = "ws://localhost:9090/graphql-ws";  // Modifiez l'URL si nécessaire

	    public static void main(String[] args) throws Exception {
	    	GQLSubscrition client = new GQLSubscrition();
	        client.connectToWebSocket();
	    }

	    // Méthode pour se connecter au WebSocket
	    public void connectToWebSocket() throws Exception {
	        WebSocketClient client = new StandardWebSocketClient();  // Utilisation du client Spring WebSocket
	        WebSocketSession session = client.doHandshake(this, WEBSOCKET_URI).get();  // Connexion WebSocket

	        // Envoi d'un message d'abonnement pour écouter les messages du serveur (exemple de requête GraphQL)
	        String subscriptionMessage = "{\"type\":\"start\",\"id\":\"1\",\"payload\":{\"query\":\"subscription { newUserCreated { id name }}\"}}";
	        session.sendMessage(new TextMessage(subscriptionMessage));

	        System.out.println("Abonnement au WebSocket...");

	        // Attente de la réponse pendant 60 secondes (le temps que les messages arrivent)
	        new CountDownLatch(1).await();
	    }

	    // Lorsqu'un message est reçu, il est affiché
	    @Override
	    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
	        System.out.println("Message reçu : " + message.getPayload());
	    }

}
