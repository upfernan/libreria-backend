package com.libreria.negocio.fachada.multa;

import java.util.UUID;

import com.libreria.dto.MultaDTO;
import com.libreria.negocio.fachada.FachadaConRetorno;

public interface ConsultarMultaPorIdFachada extends FachadaConRetorno<UUID, MultaDTO> {}
