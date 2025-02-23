package graphql.client;

import java.net.URI;
import java.util.UUID;

import org.springframework.graphql.client.WebSocketGraphQlClient;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;

import reactor.core.publisher.Flux;

public class UserDeletedSubscriptionClient {
	public static void main(String[] args) {
		String url = "ws://localhost:9090/graphql-ws";
		
		WebSocketGraphQlClient graphQlClient = WebSocketGraphQlClient
				.builder(URI.create(url), new ReactorNettyWebSocketClient())
				.build();

		String subscriptionQuery = """
	            subscription {
	                userDeleted
	            }
	        """;

	        Flux<UUID> userDeletedFlux = graphQlClient
	                .document(subscriptionQuery)
	                .retrieveSubscription("userDeleted")
	                .toEntity(UUID.class)
	                .take(5)
	                .doOnNext(userId -> System.out.println("Utilisateur supprimé : " + userId))
	                .doOnError(error -> System.err.println("Erreur : " + error.getMessage()));

	        userDeletedFlux.blockLast(); 

	}
}
