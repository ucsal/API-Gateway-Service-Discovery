package br.ucsal.gateway.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orquestrador")
public class OrquestradorController {

    private final DiscoveryClient discoveryClient;
    private final WebClient.Builder webClientBuilder;

    public OrquestradorController(DiscoveryClient discoveryClient,
                                  @Qualifier("loadBalancedWebClientBuilder") WebClient.Builder webClientBuilder) {
        this.discoveryClient = discoveryClient;
        this.webClientBuilder = webClientBuilder;
    }

    @GetMapping("/status")
    public Map<String, Object> status() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("gateway", "api-gateway ativo");
        response.put("serviceDiscovery", "Eureka configurado em http://localhost:8761");
        response.put("servicosRegistrados", discoveryClient.getServices());
        return response;
    }

    @GetMapping("/instancias/{serviceId}")
    public List<ServiceInstance> instancias(@PathVariable String serviceId) {
        return discoveryClient.getInstances(serviceId);
    }

    
    @GetMapping("/programa-completo/{programaId}")
    public Mono<Map<String, Object>> programaCompleto(@PathVariable Long programaId) {
        Mono<Object> programa = webClientBuilder.build()
                .get()
                .uri("lb://ms-disciplina/programas/" + programaId)
                .retrieve()
                .bodyToMono(Object.class)
                .onErrorReturn(Map.of("erro", "ms-disciplina indisponível ou endpoint não implementado"));

        Mono<Object> bibliografias = webClientBuilder.build()
                .get()
                .uri("lb://ms-bibliografia/bibliografias/programa/" + programaId)
                .retrieve()
                .bodyToMono(Object.class)
                .onErrorReturn(Map.of("erro", "ms-bibliografia indisponível ou endpoint não implementado"));

        return Mono.zip(programa, bibliografias)
                .map(tuple -> {
                    Map<String, Object> response = new LinkedHashMap<>();
                    response.put("programa", tuple.getT1());
                    response.put("bibliografias", tuple.getT2());
                    response.put("tipoComposicao", "Orquestração realizada pelo API Gateway");
                    return response;
                });
    }
}
