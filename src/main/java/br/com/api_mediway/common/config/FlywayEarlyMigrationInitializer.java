package br.com.api_mediway.common.config;

import org.flywaydb.core.Flyway;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * Roda as migrations do Flyway antes do contexto Spring subir, garantindo que o
 * schema exista antes do Hibernate tentar valida-lo (spring.jpa.hibernate.ddl-auto=validate).
 *
 * Isso e' feito manualmente (em vez de depender so' da auto-configuracao do Flyway)
 * porque, nesta versao do Spring Boot, a integracao automatica entre o Flyway e o JPA
 * (feita via DatabaseInitializerDetector, nos modulos spring-boot-flyway/spring-boot-jpa)
 * nao garante essa ordem: o Hibernate cria o EntityManagerFactory antes do
 * FlywayMigrationInitializer conseguir migrar o schema, e a aplicacao falha ao subir
 * mesmo com as migrations corretas. Rodar aqui, em ApplicationContextInitializer
 * (etapa anterior ao registro/criacao de qualquer bean), elimina essa dependencia de
 * ordenacao. spring.flyway.enabled=false desativa a auto-configuracao redundante.
 */
public class FlywayEarlyMigrationInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();

        String url = environment.getProperty("spring.datasource.url");
        String username = environment.getProperty("spring.datasource.username");
        String password = environment.getProperty("spring.datasource.password");
        boolean baselineOnMigrate = environment.getProperty(
                "spring.flyway.baseline-on-migrate", Boolean.class, false);
        String baselineVersion = environment.getProperty(
                "spring.flyway.baseline-version", "1");

        Flyway.configure()
                .dataSource(url, username, password)
                .baselineOnMigrate(baselineOnMigrate)
                .baselineVersion(baselineVersion)
                .load()
                .migrate();
    }
}