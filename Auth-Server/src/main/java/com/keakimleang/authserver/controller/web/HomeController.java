package com.keakimleang.authserver.controller.web;

import com.keakimleang.authserver.oauth2.entity.OAuth2Client;
import com.keakimleang.authserver.oauth2.repository.OAuth2ClientRepository;
import com.keakimleang.authserver.payloads.OAuth2ClientDto;
import com.keakimleang.authserver.service.CustomUserDetails;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final OAuth2ClientRepository oauth2ClientRepository;

    @GetMapping("/")
    public String index(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        if (user != null) {
            Long userId = user.getId();
            List<OAuth2Client> oAuth2Clients = oauth2ClientRepository.findAllByUserId(userId);
            model.addAttribute("oAuth2Clients", oAuth2Clients);
        }
        return "index";
    }

    @GetMapping("/clients/new")
    public String newClient(Model model) {
        OAuth2ClientDto client = new OAuth2ClientDto();
        model.addAttribute("client", client);
        List<String> allScopes = List.of("read", "write", "message.read", "message.write", "email", "profile");
        model.addAttribute("allScopes", allScopes);
        return "client-new";
    }

    @GetMapping("/clients/edit/{id}")
    public String editClient(@PathVariable Long id, Model model) {
        OAuth2Client clientEntity = oauth2ClientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid client ID:" + id));
        OAuth2ClientDto client = OAuth2ClientDto.from(clientEntity);
        model.addAttribute("client", client);
        List<String> allScopes = List.of("read", "write", "message.read", "message.write", "email", "profile");
        model.addAttribute("allScopes", allScopes);
        return "client-edit";
    }

    @PostMapping("/clients/save")
    public String saveClient(
            @RequestParam Long id,
            @RequestParam String clientName,
            @RequestParam(required = false) List<String> scopes,
            @RequestParam(required = false) List<String> redirectUris
    ) {
        OAuth2Client client = oauth2ClientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid client ID:" + id));
        client.setClientName(clientName);
        client.setScopes(scopes != null ? String.join(",", scopes) : "");
        client.setRedirectUris(redirectUris != null ? String.join(",", redirectUris) : "");
        oauth2ClientRepository.save(client);
        return "redirect:/";
    }

    @PostMapping("/clients/delete/{id}")
    public String deleteClient(@PathVariable Long id) {
        OAuth2Client client = oauth2ClientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid client ID:" + id));
//        oauth2ClientRepository.delete(client);
        return "redirect:/";
    }
}
