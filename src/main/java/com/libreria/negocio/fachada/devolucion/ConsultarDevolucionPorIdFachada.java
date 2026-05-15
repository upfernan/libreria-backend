package com.libreria.negocio.fachada.devolucion;

import java.util.UUID;

import com.libreria.dto.DevolucionDTO;
import com.libreria.negocio.fachada.FachadaConRetorno;

public interface ConsultarDevolucionPorIdFachada extends FachadaConRetorno<UUID, DevolucionDTO> {}
