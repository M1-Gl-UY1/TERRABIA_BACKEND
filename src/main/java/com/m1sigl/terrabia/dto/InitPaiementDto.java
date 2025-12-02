package com.m1sigl.terrabia.dto;

import com.m1sigl.terrabia.enums.ModePaiement;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InitPaiementDto {
    @NotNull
    private Long commandeId;

    @NotNull
    private ModePaiement modePaiement;

    private String numeroTelephone;
}
