package api.service;

import api.DTO.wishlistDTO;
import api.entity.customersEntity;
import api.entity.productsEntity;
import api.entity.wishlistEntity;
import api.repository.customersRepository;
import api.repository.productsRepository;
import api.repository.wishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Component
public class wishlistService {
    @Autowired
    wishlistRepository wishlistRepository;

    @Autowired
    customersRepository customersRepository;

    @Autowired
    productsRepository productsRepository;


    public List<wishlistDTO> getListWishlist(String username){
        customersEntity customersEntity = customersRepository.findByUsers_id(username);
        List<wishlistDTO> list = wishlistRepository.findByCustomers(customersEntity).stream().map(
                wishlistEntity -> {
                    wishlistDTO wishlistDTO = new wishlistDTO();
                    wishlistDTO.setId(wishlistEntity.getId());
                    wishlistDTO.setCustomerid(wishlistEntity.getCustomers().getId());
                    wishlistDTO.setProductid(wishlistEntity.getProducts().getId());
                    return  wishlistDTO;
                }
        ).collect(Collectors.toList());


        return  list;
    }

    public boolean createWishlist(wishlistDTO wishlistDTO) {
        // Lấy khách hàng từ repository, kiểm tra sự tồn tại của khách hàng
        Optional<customersEntity> optionalCustomers = customersRepository.findById(wishlistDTO.getCustomerid());
        if (!optionalCustomers.isPresent()) {
            return false;  // Nếu không tìm thấy khách hàng, trả về false
        }
        customersEntity customers = optionalCustomers.get();  // Lấy khách hàng từ Optional

        // Kiểm tra sự tồn tại của sản phẩm
        productsEntity product = productsRepository.findById(wishlistDTO.getProductid());
        if (product == null) {
            return false;  // Nếu không tìm thấy sản phẩm, trả về false
        }

        // Kiểm tra xem wishlist đã tồn tại chưa
        wishlistEntity wishlist = wishlistRepository.findByCustomersAndProducts(customers, product);
        if (wishlist != null) {
            return false;  // Nếu wishlist đã tồn tại, trả về false
        }

        // Tạo mới wishlist và lưu vào cơ sở dữ liệu
        wishlist = new wishlistEntity();
        wishlist.setCustomers(customers);
        wishlist.setProducts(product);
        wishlistRepository.save(wishlist);

        return true;  // Trả về true khi wishlist được tạo thành công
    }


    public boolean deleteWishlist(wishlistDTO wishlistDTO){
        wishlistDTO wishlistdto = null;
        wishlistEntity wishlist = wishlistRepository.findById(wishlistDTO.getId());
        if(wishlist == null){
            return  false;
        }
        wishlistRepository.delete(wishlist);
        return  true;
    }

}
