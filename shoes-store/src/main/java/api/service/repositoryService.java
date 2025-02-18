package api.service;

import api.DTO.ResultPageOrder;
import api.DTO.ResultPageRepository;
import api.DTO.productdetailDTO;
import api.DTO.repositoryDTO;
import api.entity.*;
import api.repository.productdetailRepository;
import api.repository.productsRepository;
import api.repository.repositoryRepository;
import api.repository.typeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Component
public class repositoryService {
    @Autowired
    repositoryRepository repositoryRepository;

    @Autowired
    productsRepository productsRepository;

    @Autowired
    productdetailRepository productdetailRepository;

    @Autowired
    typeRepository typeRepository;

    @Autowired
    ModelMapper modelMapper;


    public List getListRepository() {
        List<repositoryEntity> list = repositoryRepository.findAll();
        return list;
    }


    public ResultPageRepository getListRepositoryPageAdmin(int page, int size, Optional<String> title, String typeSort, String orderBy, Optional<String> type) {
        ResultPageRepository resultPage = new ResultPageRepository();
        Pageable pageable;
        Page<repositoryEntity> result;

        // Xác định hướng sắp xếp
        Sort sort = orderBy.toUpperCase().equals("DESC") ? Sort.by(Sort.Order.desc(typeSort)) : Sort.by(Sort.Order.asc(typeSort));

        // Tạo Pageable với trang và số lượng
        pageable = PageRequest.of(page - 1, size, sort);

        if (title.isPresent() && type.isPresent()) {
            // Tìm kiếm theo title và type
            result = repositoryRepository.findByType_idAndIdContaining(type.get(), title.get(), pageable);
        } else if (type.isPresent()) {
            // Tìm kiếm theo type nếu không có title
            result = repositoryRepository.findByType_id(type.get(), pageable);
        } else if (title.isPresent()) {
            // Tìm kiếm chỉ theo title nếu không có type
            result = repositoryRepository.findByIdContaining(title.get(), pageable);
        } else {
            // Trường hợp không có title và type
            result = repositoryRepository.findAll(pageable);
        }

        // Thiết lập kết quả trả về
        resultPage.setPage(result.getNumber() + 1);
        resultPage.setListResult(parseListRepo(result.getContent()));
        resultPage.setTotalpage(result.getTotalPages());

        return resultPage;
    }


    // tạo phiếu nhập (PN)
    public repositoryDTO createRepository(repositoryDTO repository, String username) {
        repositoryEntity repositoryEntity = null;
        repositoryDTO repositoryDTOs = null;
        productdetailEntity productdetail = productdetailRepository.findById(repository.getProductdetail().getId());
        Optional<typeEntity> type = typeRepository.findById(repository.getTypeid());
        if (productdetail == null || type == null) {
            return repositoryDTOs;
        }
        String id = "PN" + repository.getDatecreated().getTime();
        repositoryEntity = modelMapper.map(repository, api.entity.repositoryEntity.class);
        repositoryEntity.setId(id);
        repositoryEntity.setProductdetail(productdetail);
        repositoryEntity.setType(type.get());
        repositoryEntity.setDatecreated(repository.getDatecreated());
        repositoryEntity.setPrice(repository.getPrice());
        repositoryEntity.setQuantity(repository.getQuantity());
        repositoryEntity.setCreatedBy(username);
        repositoryRepository.save(repositoryEntity);

        // product detail
        long valueNew = productdetail.getInventory() + repository.getQuantity();
        productdetail.setInventory(valueNew);
        productdetailRepository.save(productdetail);

        // convert DTO
        repositoryDTOs = modelMapper.map(repositoryEntity, repositoryDTO.class);
        repositoryDTOs.setTypeid(repositoryEntity.getType().getId());
        //product detail dto
        productdetailDTO productdetailDTO = modelMapper.map(repositoryEntity.getProductdetail(), productdetailDTO.class);
        productdetailDTO.setProductid(repositoryEntity.getProductdetail().getProductsEntity().getId());
        repositoryDTOs.setProductdetail(productdetailDTO);

        return repositoryDTOs;

    }


    @Transactional
    public repositoryDTO updateRepository(repositoryDTO repositorydto) {
        repositoryDTO res = null;

        // Tìm repository theo ID
        repositoryEntity repository = repositoryRepository.findById(repositorydto.getId()).get();
        if (repository == null) {
            return res;  // Không tìm thấy repository
        }

        // Tìm productdetailEntity theo ID
        productdetailEntity productdetailEntity = productdetailRepository.findById(repositorydto.getProductdetail().getId());
        if (productdetailEntity == null) {
            return res;  // Không tìm thấy productdetailEntity
        }

        // Cập nhật repository với productdetail mới và các thông tin khác
        repository.setProductdetail(productdetailEntity);
        repository.setPrice(repositorydto.getPrice());
        repository.setQuantity(repositorydto.getQuantity());
        repositoryRepository.save(repository);  // Lưu repository sau khi cập nhật

        // Cập nhật inventory của productdetail
        long newInventory = productdetailEntity.getInventory() - repository.getQuantity() + repositorydto.getQuantity();
        productdetailEntity.setInventory(newInventory);  // Cập nhật lại inventory
        productdetailRepository.save(productdetailEntity);  // Lưu productdetail sau khi cập nhật

        return repositorydto;  // Trả về DTO đã được cập nhật
    }


    public List<repositoryDTO> parseListRepo(List<repositoryEntity> listRep) {
        List<repositoryDTO> list = listRep.stream().map(repositoryEntity -> {
            repositoryDTO repositoryDTOs = modelMapper.map(repositoryEntity, repositoryDTO.class);
            repositoryDTOs.setTypeid(repositoryEntity.getType().getId());
            //product detail dto
            productdetailDTO productdetailDTO = modelMapper.map(repositoryEntity.getProductdetail(), productdetailDTO.class);
            productdetailDTO.setProductid(repositoryEntity.getProductdetail().getProductsEntity().getId());
            repositoryDTOs.setProductdetail(productdetailDTO);
            return repositoryDTOs;
        }).collect(Collectors.toList());

        return list;
    }


    public Object slNhapHang(String mode, long idpro, String strDateFrom, String strDateTo) throws ParseException {
        Float[] strfloat = new Float[2];
        Date datefrom = new SimpleDateFormat("dd/MM/yyyy").parse(strDateFrom);
        Date dateto = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(strDateTo + " 23:59:59");
        float totalnhaphang = 0;
        float totalprice = 0;
        if (mode.equals("DETAIL")) {
            //get repository
            List<repositoryEntity> listrepo = repositoryRepository.findByDatecreatedBetween(datefrom, dateto);
            for (repositoryEntity repo : listrepo) {
                if (repo.getProductdetail().getId() == idpro) {
                    totalnhaphang += repo.getQuantity();
                    totalprice += (repo.getPrice() * repo.getQuantity());
                }
            }
            strfloat[0] = totalnhaphang;
            strfloat[1] = totalprice;
        } else if (mode.equals("PRODUCT")) {
            //get repository
            List<repositoryEntity> listrepo = repositoryRepository.findByDatecreatedBetween(datefrom, dateto);
            for (repositoryEntity repo : listrepo) {
                if (repo.getProductdetail().getProductsEntity().getId() == idpro) {
                    totalnhaphang += repo.getQuantity();
                    totalprice += (repo.getPrice() * repo.getQuantity());
                }
            }
            strfloat[0] = totalnhaphang;
            strfloat[1] = totalprice;
        } else {
            //get repository
            List<repositoryEntity> listrepo = repositoryRepository.findByDatecreatedBetween(datefrom, dateto);
            for (repositoryEntity repo : listrepo) {
                totalnhaphang += repo.getQuantity();
                totalprice += (repo.getPrice() * repo.getQuantity());
            }
            strfloat[0] = totalnhaphang;
            strfloat[1] = totalprice;
        }
        return strfloat;
    }

}
