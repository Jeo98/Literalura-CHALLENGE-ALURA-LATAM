package Literalura.CHALLENGE.ALURA.Literalura;

import Literalura.CHALLENGE.ALURA.Literalura.Principal.MainPrincipal;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraluraApplication {

	public static void main(String[] args) {
		var context = SpringApplication.run(LiteraluraApplication.class, args);
		MainPrincipal principal = context.getBean(MainPrincipal.class);
		principal.menu();
	}
}
