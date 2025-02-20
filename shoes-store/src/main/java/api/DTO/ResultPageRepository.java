package api.DTO;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ResultPageRepository {
    private int page;
    private int totalpage;
    private List<repositoryDTO> listResult = new ArrayList<>();
}
