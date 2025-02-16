package graphql.client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

//mutation createSimpleCAUser($fN: String!, $lN: String!) {
//	  createSimpleCAUser(firstName: $fN, lastName: $lN) {
//	    firstName
//	    lastName
//	    id
//	    location {
//	      city
//	      country
//	      id
//	    }
//	  }
//	}

//{
//  "fN": "Homer",
//  "lN": "Simpson"
//}

public class GQLMutationClient {

	public static void main(String[] args) {
		String url = "http://localhost:9090/graphql";  // L'URL de ton serveur GraphQL

        // Requête pour une mutation
		String mutationQuery = "{ \"query\": \"mutation { createUser(firstName: \\\"John\\\", lastName: \\\"Doe\\\", location: { city: \\\"Paris\\\", country: FR }) { id, firstName, lastName, location { city, country } } }\" }";

        // Configuration des headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // Envoyer la requête via RestTemplate
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(mutationQuery, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        // Affichage de la réponse
        System.out.println(response.getBody());

	}

}
