package com.libreria.negocio.fachada.pago;

import java.util.UUID;

import com.libreria.dto.PagoDTO;
import com.libreria.negocio.fachada.FachadaConRetorno;

public interface ConsultarPagoPorIdFachada extends FachadaConRetorno<UUID, PagoDTO> {}
