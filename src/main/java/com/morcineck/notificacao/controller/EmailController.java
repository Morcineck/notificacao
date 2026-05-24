package com.morcineck.notificacao.controller;

import com.morcineck.notificacao.business.EmailService;
import com.morcineck.notificacao.business.dto.TarefasDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {

    private final EmailService emailService;

    @PostMapping
    public ResponseEntity<Void> enviarEmail(@RequestBody TarefasDTO dto){
        emailService.enviaEmail(dto);
        return ResponseEntity.ok().build();
    }
}
