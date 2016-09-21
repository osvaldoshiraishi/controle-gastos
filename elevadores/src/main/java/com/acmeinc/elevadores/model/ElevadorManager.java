package com.acmeinc.elevadores.model;

public class ElevadorManager {
	
	public static void main(String[] args) throws InterruptedException {
		Elevador elevador = new Elevador(1);
		//elevador.addUsuario(new Usuario(25));
		//elevador.addUsuario(new Usuario(15));
		elevador.addUsuario(new Usuario(10));
		//elevador.addUsuario(new Usuario(23));
		elevador.addUsuario(new Usuario(3));
		
		Thread t = new Thread(elevador);
	    t.start();
	     
	    Thread.sleep(5000);
	     
	    elevador.addUsuario(new Usuario(2));
		elevador.addUsuario(new Usuario(15));
		elevador.addUsuario(new Usuario(10));
		elevador.addUsuario(new Usuario(23));
		elevador.addUsuario(new Usuario(3));
		
		
		
	}

}
