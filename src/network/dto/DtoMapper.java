package network.dto;

public interface DtoMapper<S, D> {

    D toDTO(S source);
}
