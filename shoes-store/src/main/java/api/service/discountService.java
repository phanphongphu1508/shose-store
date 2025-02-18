package api.service;

import api.entity.discountEntity;
import api.repository.discountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class discountService {

    @Autowired
    private discountRepository discountRepository;

    // Lấy discount theo id, trả về null nếu không tìm thấy
    public discountEntity getDiscount(String id) {
        Optional<discountEntity> discountOptional = discountRepository.findById(id);
        return discountOptional.orElse(null);  // Nếu không tìm thấy, trả về null
    }

    // Lấy danh sách tất cả các discount
    public List<discountEntity> getListDiscount() {
        return discountRepository.findAll();
    }

    // Tạo discount mới nếu chưa tồn tại
    public discountEntity createDiscount(discountEntity discountEntity) {
        if (discountRepository.existsById(discountEntity.getId())) {
            return null;  // Nếu discount đã tồn tại, không tạo mới và trả về null
        }
        return discountRepository.save(discountEntity);  // Lưu discount mới vào cơ sở dữ liệu
    }

    // Cập nhật discount nếu tồn tại, nếu không trả về null
    public discountEntity updateDiscount(discountEntity discountEntity) {
        if (discountRepository.existsById(discountEntity.getId())) {
            return discountRepository.save(discountEntity);  // Cập nhật discount
        }
        return null;  // Nếu discount không tồn tại, trả về null
    }
}
