package com.libreria.negocio.casouso.multa;

import java.util.List;
import java.util.UUID;

import com.libreria.entidad.MultaEntidad;
import com.libreria.negocio.casouso.CasoUsoConRetorno;

public interface ConsultarMultasPorUsuarioCasoUso extends CasoUsoConRetorno<UUID, List<MultaEntidad>> {}
