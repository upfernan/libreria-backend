package com.libreria.negocio.casouso.categoria;

import com.libreria.negocio.casouso.CasoUsoConRetorno;
import com.libreria.entidad.CategoriaEntidad;
import java.util.UUID;

public interface ConsultarCategoriaPorIdCasoUso extends CasoUsoConRetorno<UUID, CategoriaEntidad> {}
