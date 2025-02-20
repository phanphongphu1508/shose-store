package api.DTO;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ResultPageOrder {
    private int page;
    private int totalpage;
    private List<ordersDTO> listResult = new ArrayList<>();
}
