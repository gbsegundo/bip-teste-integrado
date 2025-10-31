package com.example.backend.controller;

import com.example.backend.dto.TransferenciaRequest;
import com.example.backend.model.Beneficio;
import com.example.backend.service.BeneficioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/beneficios")
@CrossOrigin(origins = {"http://localhost:4200", "http://127.0.0.1:4200"})
@Tag(name = "Benefícios", description = "API para gerenciamento de benefícios")
public class BeneficioController {

    private final BeneficioService beneficioService;

    public BeneficioController(BeneficioService beneficioService) {
        this.beneficioService = beneficioService;
    }

    @GetMapping
    @Operation(summary = "Lista todos os benefícios")
    public ResponseEntity<List<Beneficio>> listarTodos() {
        return ResponseEntity.ok(beneficioService.listarTodos());
    }

    @GetMapping("/ativos")
    @Operation(summary = "Lista benefícios ativos")
    public ResponseEntity<List<Beneficio>> listarAtivos() {
        return ResponseEntity.ok(beneficioService.listarAtivos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca benefício por ID")
    public ResponseEntity<Beneficio> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(beneficioService.buscarPorId(id));
    }

    @GetMapping("/buscar")
    @Operation(summary = "Busca benefícios por nome")
    public ResponseEntity<List<Beneficio>> buscarPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(beneficioService.buscarPorNome(nome));
    }

    @PostMapping
    @Operation(summary = "Cria um novo benefício")
    public ResponseEntity<Beneficio> criar(@Valid @RequestBody Beneficio beneficio) {
        Beneficio novoBeneficio = beneficioService.criar(beneficio);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoBeneficio);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um benefício existente")
    public ResponseEntity<Beneficio> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody Beneficio beneficio) {
        return ResponseEntity.ok(beneficioService.atualizar(id, beneficio));
    }

    @PatchMapping("/{id}/desativar")
    @Operation(summary = "Desativa um benefício")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        beneficioService.desativar(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove permanentemente um benefício")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        beneficioService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/transferir")
    @Operation(summary = "Transfere valor entre benefícios")
    public ResponseEntity<Void> transferir(@Valid @RequestBody TransferenciaRequest request) {
        beneficioService.transferir(
                request.getFromId(),
                request.getToId(),
                request.getAmount()
        );
        return ResponseEntity.ok().build();
    }
}






