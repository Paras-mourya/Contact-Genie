package smart.smart_contract;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import smart.smart_contract.config.EnvLoader;

@SpringBootApplication
public class SmartContractApplication {

	public static void main(String[] args) {
		 EnvLoader.loadEnv();
		SpringApplication.run(SmartContractApplication.class, args);
	}

}
