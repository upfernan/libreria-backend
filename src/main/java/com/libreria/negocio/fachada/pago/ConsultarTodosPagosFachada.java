package com.libreria.negocio.fachada.pago;

import java.util.List;

import com.libreria.dto.PagoDTO;
import com.libreria.negocio.fachada.FachadaConRetorno;

public interface ConsultarTodosPagosFachada extends FachadaConRetorno<PagoDTO, List<PagoDTO>> {}
