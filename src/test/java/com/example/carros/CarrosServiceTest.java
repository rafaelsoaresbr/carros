package com.example.carros;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.fail;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.carros.domain.Carro;
import com.example.carros.domain.CarroService;
import com.example.carros.domain.dto.CarroDTO;
import com.example.carros.domain.exception.ObjectNotFoundException;

@SpringBootTest
class CarrosServiceTest {

	@Autowired
	private CarroService service;

	@Test
	public void testSave() {
		Carro carro = new Carro();
		carro.setNome("Ferrari");
		carro.setTipo("esportivos");
		CarroDTO c = service.insert(carro);
		assertNotNull(c);

		Long id = c.getId();
		assertNotNull(id);

		c = service.getCarroById(id);
		assertNotNull(c);

		assertEquals("Ferrari", c.getNome());
		assertEquals("esportivos", c.getTipo());

		service.delete(id);
		try {
			assertNull(service.getCarroById(id));
			fail("O carro não foi excluído");
		} catch (ObjectNotFoundException ex) {
		}
	}

	@Test
	public void testList() {
		List<CarroDTO> carros = service.getCarros();
		assertEquals(30, carros.size());
	}

	@Test
	public void testGet() {
		CarroDTO c = service.getCarroById(11L);
		assertNotNull(c);
		assertEquals("Ferrari FF", c.getNome());
	}

	@Test
	public void testListaPorTipo() {
		assertEquals(10, service.getCarrosByTipo("classicos").size());
		assertEquals(10, service.getCarrosByTipo("esportivos").size());
		assertEquals(10, service.getCarrosByTipo("luxo").size());
		assertEquals(0, service.getCarrosByTipo("x").size());
	}

}
