package com.libreria.negocio.casouso.autor;

import com.libreria.entidad.AutorEntidad;
import com.libreria.negocio.casouso.CasoUsoConRetorno;
import java.util.UUID;

public interface ConsultarAutorPorIdCasoUso extends CasoUsoConRetorno<UUID, AutorEntidad> {}
