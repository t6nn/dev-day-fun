package eu.t6nn.demo.codecomp;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.options.MutableDataSet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Singleton;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
@EnableZuulProxy
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

	@Bean("markdownOptions")
	@Singleton
	MutableDataSet markdownOptions() {
		MutableDataSet options = new MutableDataSet();
		return options;
	}

	@Bean
	@Singleton
	Parser markdownParser(MutableDataSet markdownOptions) {
		return Parser.builder(markdownOptions).build();
	}

	@Bean
	@Singleton
	HtmlRenderer markdownRenderer(MutableDataSet markdownOptions) {
		return HtmlRenderer.builder(markdownOptions).build();
	}

//	@Bean
//	ServletRegistrationBean zuulServlet() {
//		ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new ZuulServlet());
//		servletRegistrationBean.addUrlMappings("/session/*");
//		return servletRegistrationBean;
//	}
//
//	@Bean
//	FilterRegistrationBean zuulFilter() {
//		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new ContextLifecycleFilter());
//		filterRegistrationBean.addUrlPatterns("/session/*");
//		return filterRegistrationBean;
//	}
}
