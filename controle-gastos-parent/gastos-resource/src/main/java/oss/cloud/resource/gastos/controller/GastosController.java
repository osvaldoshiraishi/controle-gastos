package oss.cloud.resource.gastos.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import oss.cloud.resource.gastos.model.Gasto;

@RestController
@RequestMapping(value = "/cadastro/despesas")
public class GastosController {

	private static Logger log = LoggerFactory.getLogger(GastosController.class);
	@Autowired
	private MockGastoService mockGastoService;
	
	
	
	@RequestMapping(value = "findAll", method = RequestMethod.GET)
	public ResponseEntity<List<Gasto>> listAllGastos() {
		List<Gasto> gastos = mockGastoService.mockfindAllGastos();
		if (gastos.isEmpty()) {
			return new ResponseEntity<List<Gasto>>(HttpStatus.NO_CONTENT);
		}
		log.info(String.format("Find all retornou %s", gastos.size()));
		return new ResponseEntity<List<Gasto>>(gastos, HttpStatus.OK);
	}
	
	@RequestMapping(value = "detail/{id}", method = RequestMethod.GET)
	public ResponseEntity<Gasto> detail(@PathVariable Long id) {
		Gasto gasto = mockGastoService.detailGasto(id);
		if (gasto == null) {
			return new ResponseEntity<Gasto>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Gasto>(gasto, HttpStatus.OK);
	}
	
	@RequestMapping(value = "update/{id}", method = RequestMethod.POST)
	public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody Gasto gasto) {
		log.info(String.format("Update Gasto de id %s", id));
		Gasto db = mockGastoService.detailGasto(id);
		if(db == null){
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		}
		
		db.setLugar(gasto.getLugar());
		db.setData(gasto.getData());
		db.setTipo(gasto.getTipo());
		db.setTipoDespesa(gasto.getTipoDespesa());
		db.setValor(gasto.getValor());
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public ResponseEntity<Gasto> create(@RequestBody Gasto gasto) {
		log.info(String.format("Gasto no lugar %s no valor to %s", gasto.getLugar(), gasto.getValor()));
		mockGastoService.mockCreateGasto(gasto);
		return new ResponseEntity<Gasto>(gasto, HttpStatus.OK);
	}
	
	@RequestMapping(value = "remove/{id}", method = RequestMethod.POST)
	public ResponseEntity<Void> remove(@PathVariable Long id,@RequestBody Gasto gasto) {
		log.info(String.format("Delete Gasto de id %s", id));
		Gasto gastoDB = mockGastoService.detailGasto(id);
		if (gasto == null) {
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		}
		mockGastoService.removeGasto(gastoDB);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
}
