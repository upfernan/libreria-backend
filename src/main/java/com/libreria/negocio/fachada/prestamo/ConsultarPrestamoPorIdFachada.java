package com.libreria.negocio.fachada.prestamo;

import com.libreria.dto.PrestamoDTO;
import com.libreria.negocio.fachada.FachadaConRetorno;
import java.util.UUID;

public interface ConsultarPrestamoPorIdFachada extends FachadaConRetorno<UUID, PrestamoDTO> {

}
