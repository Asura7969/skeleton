package com.asura.akka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author asura7969
 * @create 2022-07-27-21:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "myakka")
public class AkkaConfig {

    private String name;
    private Integer remote_port = 2551;
    private List<String> seed_nodes;



}
