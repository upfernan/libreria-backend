package com.libreria.negocio.casouso.multa;

import java.util.UUID;

import com.libreria.entidad.MultaEntidad;
import com.libreria.negocio.casouso.CasoUsoConRetorno;

public interface ConsultarMultaPorIdCasoUso extends CasoUsoConRetorno<UUID, MultaEntidad> {}
