package com.libreria.negocio.fachada.devolucion;

import java.util.List;

import com.libreria.dto.DevolucionDTO;
import com.libreria.negocio.fachada.FachadaConRetorno;

public interface ConsultarTodasDevolucionesFachada extends FachadaConRetorno<DevolucionDTO, List<DevolucionDTO>> {}
