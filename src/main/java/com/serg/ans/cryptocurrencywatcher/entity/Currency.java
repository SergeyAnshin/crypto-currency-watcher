package com.serg.ans.cryptocurrencywatcher.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Currency {
    @Id
    private long id;
    @EqualsAndHashCode.Include
    @Column(updatable = false)
    private String symbol;
    @JsonProperty("price_usd")
    private double price;
}
