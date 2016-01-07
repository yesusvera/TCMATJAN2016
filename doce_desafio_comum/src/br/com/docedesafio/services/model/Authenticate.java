package br.com.docedesafio.services.model;

import br.com.docedesafio.model.Usuario;

public class Authenticate {
	private Usuario user;
	
	private Mensagem message;
	
	public Usuario getUser() {
		return user;
	}
	public void setUser(Usuario user) {
		this.user = user;
	}
	public Mensagem getMessage() {
		return message;
	}
	public void setMessage(Mensagem message) {
		this.message = message;
	}
}
