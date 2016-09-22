package com.acmeinc.elevadores.manager;

import com.acmeinc.elevadores.model.Usuario;

public interface ElevadorEventListener {

	public void publish(Usuario usuario);
}
