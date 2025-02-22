package graphql.client;

import java.net.URI;

import org.springframework.graphql.client.WebSocketGraphQlClient;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;

public class GQLSubscriptionClient {
	public static void main(String[] args) {
		String url = "ws://localhost:9090/graphql-ws";
//		String subscriptionQuery = "{ \"query\": \"subscription { newMessage }\" }";
		
		WebSocketGraphQlClient graphQlClient = WebSocketGraphQlClient
				.builder(URI.create(url), new ReactorNettyWebSocketClient())
				.build();

		graphQlClient.document("subscription {newMessage}")
				.retrieveSubscription("newMessage")
				.toEntity(String.class)
				.take(5)
				.doOnNext(System.out::println)
				.blockLast();

	}

}
