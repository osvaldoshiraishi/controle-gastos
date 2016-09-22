package com.acmeinc.elevadores.manager;

import com.acmeinc.elevadores.model.Usuario;

public interface ElevadorEventListener {

	/**
	 * Publica os dados do usuário no arquivo de resultado
	 * 
	 * @param usuario usuário que chegou no seu destino
	 */
	public void publish(Usuario usuario);
}
