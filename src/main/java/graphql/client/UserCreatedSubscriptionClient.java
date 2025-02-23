package graphql.client;

import java.net.URI;

import org.springframework.graphql.client.WebSocketGraphQlClient;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;

import graphql.client.model.UserDTO;
import reactor.core.publisher.Flux;

public class UserCreatedSubscriptionClient {
	public static void main(String[] args) {
		String url = "ws://localhost:9090/graphql-ws";
		
		WebSocketGraphQlClient graphQlClient = WebSocketGraphQlClient
				.builder(URI.create(url), new ReactorNettyWebSocketClient())
				.build();

		String subscriptionQuery = """
	            subscription {
	                userCreated {
	                    id
	                    firstName
	                    lastName
	                    location {
	                        city
	                        country
	                    }
	                }
	            }
	        """;

	        Flux<UserDTO> userCreatedFlux = graphQlClient
	                .document(subscriptionQuery)
	                .retrieveSubscription("userCreated")
	                .toEntity(UserDTO.class)
	                .take(5)
	                .doOnNext(user -> System.out.println("Utilisateur créé : " + user))
	                .doOnError(error -> System.err.println("Erreur : " + error.getMessage()));

	        userCreatedFlux.blockLast();
	        
	        // ======== OR with JSON ===========
	        /*
	        Flux<String> userCreatedFlux = graphQlClient
	                .document(subscriptionQuery)
	                .retrieveSubscription("userCreated")
	                .toEntity(String.class)
	                .take(5)
	                .doOnNext(System.out::println)
	                .doOnError(error -> System.err.println("Erreur : " + error.getMessage()));

	        userCreatedFlux.subscribe(responseJson -> {
	            try {
	                ObjectMapper objectMapper = new ObjectMapper();
	                Map<String, Object> userMap = objectMapper.readValue(responseJson, Map.class);
	                
	                System.out.println("Utilisateur créé : " + userMap);
	            } catch (Exception e) {
	                System.err.println("Erreur de parsing JSON : " + e.getMessage());
	            }
	        });
	        userCreatedFlux.blockLast();
	        */
	    }

}
