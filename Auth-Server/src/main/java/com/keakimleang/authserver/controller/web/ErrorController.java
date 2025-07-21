package com.keakimleang.authserver.controller.web;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class ErrorController {
	@GetMapping("/error/redirect-uri-mismatch")
	public String login(Model model, HttpServletRequest request) {
		String redirectUri = request.getParameter("redirect_uri");
		model.addAttribute("redirectUri", redirectUri);
		return "error/redirect-uri-mismatch";
	}
}
