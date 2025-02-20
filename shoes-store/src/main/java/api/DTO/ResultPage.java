package api.DTO;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class ResultPage {

    private int page;
    private int totalpage;
    private List<productsDTO> listResult = new ArrayList<>();
}
