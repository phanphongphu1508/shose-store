package api.service;

import api.entity.categoryEntity;
import api.repository.categoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class categoryService {

    @Autowired
    private categoryRepository categoryRepository;

    // Kiểm tra xem category với id có tồn tại hay không
    public boolean checkid(long id){
        return categoryRepository.existsById(id);
    }

    // Tìm một category theo id, trả về Optional để xử lý không tìm thấy
    public categoryEntity findOnecategory(long id){
        Optional<categoryEntity> categoryOptional = categoryRepository.findById(id);
        return categoryOptional.orElse(null);  // Nếu không tìm thấy, trả về null
    }

    // Lấy tất cả các category
    public List<categoryEntity> getlistcategory(){
        return categoryRepository.findAll();
    }

    // Tạo một category mới nếu chưa tồn tại
    public categoryEntity createcategory(categoryEntity categoryEntity){
        if (categoryRepository.existsById(categoryEntity.getId())) {
            return null;  // Nếu đã tồn tại, không tạo mới và trả về null
        }
        return categoryRepository.save(categoryEntity);
    }

    // Cập nhật một category
    public categoryEntity updatecategory(categoryEntity categoryEntity){
        return categoryRepository.save(categoryEntity);  // Cập nhật hoặc lưu mới
    }
}
