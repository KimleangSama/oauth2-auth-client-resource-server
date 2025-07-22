package com.keakimleang.authserver.utils.jackson2.deserializer;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.keakimleang.authserver.entities.User;
import com.keakimleang.authserver.entities.Role;
import com.keakimleang.authserver.service.CustomUserDetails;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class CustomUserDetailsDeserializer extends JsonDeserializer<CustomUserDetails> {
    @Override
    public CustomUserDetails deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode jsonNode = mapper.readTree(jp);
        final JsonNode authoritiesNode = readJsonNode(jsonNode, "authorities");
        Set<Role> userAuthorities = getUserAuthorities(mapper, authoritiesNode);

        Long id = readJsonNode(jsonNode, "id").asLong();
        String username = readJsonNode(jsonNode, "username").asText();
        JsonNode passwordNode = readJsonNode(jsonNode, "password");
        String password = passwordNode.asText("");

        User user = new User(id, username, password, userAuthorities);
        if (passwordNode.asText(null) == null) {
            user.eraseCredentials();
        }
        return new CustomUserDetails(user);
    }

    private JsonNode readJsonNode(JsonNode jsonNode, String field) {
        return jsonNode.has(field) ? jsonNode.get(field) : MissingNode.getInstance();
    }

    private Set<Role> getUserAuthorities(final ObjectMapper mapper, final JsonNode authoritiesNode) throws JsonParseException, JsonMappingException, IOException {
        Set<Role> userAuthorities = new HashSet<>();
        if (authoritiesNode != null) {
            if (authoritiesNode.isArray()) {
                for (final JsonNode objNode : authoritiesNode) {
                    if (objNode.isArray()) {
                        ArrayNode arrayNode = (ArrayNode) objNode;
                        for (JsonNode elementNode : arrayNode) {
                            Role role = mapper.readValue(elementNode.traverse(mapper), Role.class);
                            userAuthorities.add(role);
                        }
                    }
                }
            }
        }
        return userAuthorities;
    }

}
