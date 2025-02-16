package graphql.client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GQLQueryClient {
	private static final String GRAPHQL_URL = "http://localhost:9090/graphql"; // Change selon ton URL GraphQL


	public static void main(String[] args) {
//		RestTemplate restTemplate = new RestTemplate();

        // 1. Test de la requête "users" (sans variables)
        String queryUsers = "{ \"query\": \"{ users { id firstName lastName location { city country } } }\" }";
        String responseUsers = executeGraphQLQuery(queryUsers);
        System.out.println("Response - users: " + responseUsers);
        
        String[] userIds = extractUserIds(responseUsers);

        // 2. Test de la requête "userById" avec variable id
        for (String userId : userIds) {
            String queryUserById = "{ \"query\": \"query ($id: ID!) { userById(id: $id) { id firstName lastName location { city country } } }\", \"variables\": {\"id\": \"" + userId + "\"} }";
            String responseUserById = executeGraphQLQuery(queryUserById);
            System.out.println("Response - userById (ID: " + userId + "): " + responseUserById);
        }

        // 3. Test de la requête "usersByCountry" avec variable country
        String queryUsersByCountry = "{ \"query\": \"query ($country: Country!) { usersByCountry(country: $country) { id firstName lastName location { city country } } }\", \"variables\": {\"country\": \"FR\"} }";
        String responseUsersByCountry = executeGraphQLQuery(queryUsersByCountry);
        System.out.println("Response - usersByCountry: " + responseUsersByCountry);

	}

	private static String executeGraphQLQuery(String query) {
        // Configuration des headers HTTP pour une requête GraphQL
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // Crée l'entité HTTP avec le corps de la requête et les headers
        HttpEntity<String> entity = new HttpEntity<>(query, headers);

        // Envoie la requête GraphQL via POST
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(GRAPHQL_URL, HttpMethod.POST, entity, String.class);

        // Retourne la réponse du serveur
        return response.getBody();
    }
	
	private static String[] extractUserIds(String response) {
	    try {
	        // Crée un ObjectMapper pour parser le JSON
	        ObjectMapper objectMapper = new ObjectMapper();

	        // Parse la réponse JSON
	        JsonNode rootNode = objectMapper.readTree(response);
	        JsonNode usersNode = rootNode.path("data").path("users");

	        // Crée un tableau pour stocker les IDs
	        String[] userIds = new String[usersNode.size()];
	        
	        for (int i = 0; i < usersNode.size(); i++) {
	            userIds[i] = usersNode.get(i).path("id").asText();
	        }
	        
	        return userIds;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new String[0]; // Retourne un tableau vide en cas d'erreur
	    }
	}
}
