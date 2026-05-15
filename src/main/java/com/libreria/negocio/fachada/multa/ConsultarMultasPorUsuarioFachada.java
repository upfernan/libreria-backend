package com.libreria.negocio.fachada.multa;

import java.util.List;
import java.util.UUID;

import com.libreria.dto.MultaDTO;
import com.libreria.negocio.fachada.FachadaConRetorno;

public interface ConsultarMultasPorUsuarioFachada extends FachadaConRetorno<UUID, List<MultaDTO>> {}
