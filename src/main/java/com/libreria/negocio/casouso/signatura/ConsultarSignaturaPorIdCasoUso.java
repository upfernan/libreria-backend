package com.libreria.negocio.casouso.signatura;

import com.libreria.entidad.SignaturaEntidad;
import com.libreria.negocio.casouso.CasoUsoConRetorno;
import java.util.UUID;

public interface ConsultarSignaturaPorIdCasoUso extends CasoUsoConRetorno<UUID, SignaturaEntidad> {}
