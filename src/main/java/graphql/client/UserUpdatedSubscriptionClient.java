package graphql.client;

import java.net.URI;

import org.springframework.graphql.client.WebSocketGraphQlClient;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;

import graphql.client.model.UserDTO;
import reactor.core.publisher.Flux;

public class UserUpdatedSubscriptionClient {
	public static void main(String[] args) {
		String url = "ws://localhost:9090/graphql-ws";
		
		WebSocketGraphQlClient graphQlClient = WebSocketGraphQlClient
				.builder(URI.create(url), new ReactorNettyWebSocketClient())
				.build();
		
//		String userId = "123e4567-e89b-12d3-a456-426614174000";
		String userId = args[0];

		String subscriptionQuery = String.format("""
	            subscription {
	                userUpdated(userId: "%s") {
	                    id
	                    firstName
	                    lastName
	                    location {
				            id
				            city
				            country
				        }
	                }
	            }
	        """, userId);

		Flux<UserDTO> userUpdates = graphQlClient
                .document(subscriptionQuery)
                .retrieveSubscription("userUpdated")
                .toEntity(UserDTO.class)
                .doOnNext(userDTO -> System.out.println("Utilisateur mis à jour : " + userDTO))
                .doOnError(error -> System.err.println("Erreur : " + error.getMessage()));

        userUpdates.blockLast();

	}
}
