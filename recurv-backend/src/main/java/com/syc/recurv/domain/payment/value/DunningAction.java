package com.syc.recurv.domain.payment.value;

import java.time.LocalDateTime;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter @Setter
public class DunningAction {
    private String action;
    private String result;
    private String message;
    private LocalDateTime actedAt;
}
