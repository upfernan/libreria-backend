package com.libreria.negocio.fachada.tarifamulta;

import com.libreria.dto.TarifaMultaDTO;
import com.libreria.negocio.fachada.FachadaConRetorno;
import java.util.UUID;

public interface ConsultarTarifaMultaPorIdFachada extends FachadaConRetorno<UUID, TarifaMultaDTO> {}
