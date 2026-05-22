package com.libreria.negocio.fachada.categoria.impl;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.CategoriaDTO;
import com.libreria.negocio.assembler.dto.impl.CategoriaDTOAssembler;
import com.libreria.negocio.casouso.categoria.AgregarCategoriaCasoUso;
import com.libreria.negocio.casouso.categoria.impl.AgregarCategoriaCasoUsoImpl;
import com.libreria.negocio.fachada.categoria.AgregarCategoriaFachada;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class AgregarCategoriaFachadaImpl implements AgregarCategoriaFachada {

	private final DAOFactory daoFactory;
	private final AgregarCategoriaCasoUso casoUso;

	public AgregarCategoriaFachadaImpl() {
		daoFactory = DAOFactory.getFactory();
		casoUso = new AgregarCategoriaCasoUsoImpl(daoFactory);
	}

	@Override
	public void ejecutar(final CategoriaDTO datos) {
		try {
			daoFactory.iniciarTransaccion();

			casoUso.ejecutar(CategoriaDTOAssembler.getInstance().ensamblarDominio(datos));

			daoFactory.confirmarTransaccion();

		} catch (GestorLibreriaExcepcion excepcion) {
			daoFactory.cancelarTransaccion();
			throw excepcion;

		} catch (Exception excepcion) {
			daoFactory.cancelarTransaccion();
			throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al agregar la categoría.", "Error técnico inesperado en AgregarCategoriaFachadaImpl: " + excepcion.getMessage());

		} finally {
			daoFactory.cerrarConexion();
		}
	}

}
