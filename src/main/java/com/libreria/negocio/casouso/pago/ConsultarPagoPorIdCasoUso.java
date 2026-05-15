package com.libreria.negocio.casouso.pago;

import java.util.UUID;

import com.libreria.entidad.PagoEntidad;
import com.libreria.negocio.casouso.CasoUsoConRetorno;

public interface ConsultarPagoPorIdCasoUso extends CasoUsoConRetorno<UUID, PagoEntidad> {}
