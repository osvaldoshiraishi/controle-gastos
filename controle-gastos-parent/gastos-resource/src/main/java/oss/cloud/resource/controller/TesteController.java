package oss.cloud.resource.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class TesteController {

	@RequestMapping(value = "/teste/aberto", method = RequestMethod.GET)
	public ResponseEntity<Void> testeAberto() {
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	@PreAuthorize("#oauth2.hasScope('read') and #oauth2.hasScope('write') and hasAnyRole('ROLE_ASSOCIADO', 'ROLE_USUARIO', 'ROLE_OPERADOR', 'ROLE_ADMIN')")
	@RequestMapping(value="/teste/autenticado", method = RequestMethod.GET)
	public ResponseEntity<Void> testeAutenticado(){
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
