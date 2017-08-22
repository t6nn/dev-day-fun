package eu.t6nn.demo.codecomp;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
@Configuration
public class CodeCompetitionFeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodeCompetitionFeApplication.class, args);
	}

	@Bean
	ExecutorService executorService() {
		return Executors.newCachedThreadPool();
	}

	@Bean
	DockerClient dockerClient() throws DockerCertificateException, DockerException, InterruptedException {

        //DefaultDockerClient docker = new DefaultDockerClient("unix:///var/run/docker.sock");
        DefaultDockerClient docker = DefaultDockerClient.fromEnv().build();
        System.out.println(docker.listNodes());
        return docker;
	}
}
