package br.inatel.pos.mobile.dm110.rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import br.inatel.pos.mobile.dm110.impl.AddressServiceImpl;

//caminho da aplica��o � /api
@ApplicationPath("/api")
public class RestApplication extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<>();
		//� necess�rio adicionar a classe AddressServiceImpl, visto que ela n�o se encontra no mesmo pacote que esta classe
		classes.add(AddressServiceImpl.class);
		return classes;
	}

}
