package com.serg.ans.cryptocurrencywatcher.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class AvailableCryptoCurrency {
    private long id;
    private String symbol;
}
