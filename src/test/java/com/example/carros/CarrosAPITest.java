package com.example.carros;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.carros.domain.Carro;
import com.example.carros.domain.dto.CarroDTO;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CarrosApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CarrosAPITest {

	@Autowired
	protected TestRestTemplate rest;

	private ResponseEntity<CarroDTO> getCarro(String url) {
		return rest.withBasicAuth("user", "123").getForEntity(url, CarroDTO.class);
	}

	private ResponseEntity<List<CarroDTO>> getCarros(String url) {
		return rest.withBasicAuth("user", "123").exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<CarroDTO>>() {
		});
	}

	@Test
	public void testSave() {
		Carro carro = new Carro();
		carro.setNome("Porshe");
		carro.setTipo("esportivos");
		ResponseEntity<CarroDTO> response = rest.withBasicAuth("admin", "123").postForEntity("/api/v1/carros", carro, null);
		System.out.println(response);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		String location = response.getHeaders().get("location").get(0);
		CarroDTO c = getCarro(location).getBody();

		assertNotNull(c);
		assertEquals("Porshe", c.getNome());
		assertEquals("esportivos", c.getTipo());

		rest.withBasicAuth("user", "123").delete(location);
		assertEquals(HttpStatus.NOT_FOUND, getCarro(location).getStatusCode());
	}

	@Test
	public void testLista() {
		List<CarroDTO> carros = getCarros("/api/v1/carros").getBody();
		assertNotNull(carros);
		assertEquals(30, carros.size());
	}

	@Test
	public void testListaPorTipo() {
		assertEquals(10, getCarros("/api/v1/carros/tipo/classicos").getBody().size());
		assertEquals(10, getCarros("/api/v1/carros/tipo/esportivos").getBody().size());
		assertEquals(10, getCarros("/api/v1/carros/tipo/luxo").getBody().size());
		assertEquals(HttpStatus.NO_CONTENT, getCarros("/api/v1/carros/tipo/xxx").getStatusCode());
	}

	@Test
	public void testGetOk() {
		ResponseEntity<CarroDTO> response = getCarro("/api/v1/carros/11");
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		CarroDTO c = response.getBody();
		assertEquals("Ferrari FF", c.getNome());
	}

	@Test
	public void testGetNotFound() {
		ResponseEntity<CarroDTO> response = getCarro("/api/v1/carros/1100");
		assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
	}

}
