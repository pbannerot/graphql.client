package graphql.client.model;

import java.util.UUID;

public record LocationDTO(UUID id, String city, Country country) {

}
