package eci.cvds.Parcial.ECICredit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.SpringApplication;

import static org.mockito.Mockito.*;

@SpringBootTest
class EciCreditApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void mainTest() {
		// Simula el método run de SpringApplication para evitar que realmente inicie la aplicación
		try (var mockedSpringApplication = mockStatic(SpringApplication.class)) {
			// Ejecutamos el método main
			EciCreditApplication.main(new String[]{});

			// Verificamos que SpringApplication.run() fue llamado correctamente
			mockedSpringApplication.verify(() ->
							SpringApplication.run(EciCreditApplication.class, new String[]{}),
					times(1)
			);
		}
	}

}
