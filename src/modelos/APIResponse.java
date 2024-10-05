package modelos;

import java.util.Map;

public record APIResponse(String base, Map<String, Double> conversion_rates) {
}
