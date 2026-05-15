package com.libreria.negocio.fachada.reserva;

import com.libreria.dto.ReservaDTO;
import com.libreria.negocio.fachada.FachadaConRetorno;
import java.util.UUID;

public interface ConsultarReservaPorIdFachada extends FachadaConRetorno<UUID, ReservaDTO> {

}
