package com.libreria.negocio.casouso.devolucion;

import java.util.UUID;

import com.libreria.entidad.DevolucionEntidad;
import com.libreria.negocio.casouso.CasoUsoConRetorno;

public interface ConsultarDevolucionPorIdCasoUso extends CasoUsoConRetorno<UUID, DevolucionEntidad> {}
