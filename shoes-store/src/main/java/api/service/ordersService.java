package api.service;

import api.DTO.*;
import api.entity.*;
import api.repository.*;
import jakarta.mail.MessagingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Component
public class ordersService {
    @Autowired
    ordersRepository ordersRepository;

    @Autowired
    api.repository.productsRepository productsRepository;

    @Autowired
    api.repository.customersRepository customersRepository;

    @Autowired
    orderdetailRepository orderdetailRepository;

    @Autowired
    shopcartRepository shopcartRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    imageRepository imageRepository;

    @Autowired
    productdetailRepository productdetailRepository;

    @Autowired
    paymentRepository paymentRepository;

    @Autowired
    repositoryRepository repositoryRepository;

    public List<ordersDTO> getListOrderCustomer(String userid) {
        List<ordersDTO> list = new ArrayList<>();
        customersEntity customers = customersRepository.findByUsername(userid);
        if (customers == null) {
            return list;
        }

        list = ordersRepository.findListOrderCustomer(customers.getId()).stream().map(ordersEntity -> {
            ordersDTO orders = modelMapper.map(ordersEntity, ordersDTO.class);
//                    orders.setPaymentEntity(ordersEntity.getPaymentEntity().getId());

            // orderdetail dto
            List<orderdetailDTO> listOrderdetailDTO = ordersEntity.getOrderdetailEntities().stream().map(orderdetailEntity -> {
                orderdetailDTO orderdetailDTO = modelMapper.map(orderdetailEntity, api.DTO.orderdetailDTO.class);
                orderdetailDTO.setProductid(orderdetailEntity.getProductdetailEntity().getProductsEntity().getId());
                orderdetailDTO.setSize(orderdetailEntity.getProductdetailEntity().getSize());
                return orderdetailDTO;
            }).collect(Collectors.toList());
            orders.setListOrderdetail(listOrderdetailDTO);
            return orders;
        }).collect(Collectors.toList());
        return list;
    }

    public ResultPageOrder getListOrderPageAdmin(int page, int size, Optional<String> title, String typeSort, String orderBy, Optional<String> type) {
        ResultPageOrder resultPage = new ResultPageOrder();
        Pageable pageable;
        Page<ordersEntity> Result;
        ordersEntity.Status status;
        if (ordersEntity.Status.UNCONFIRM.toString().equals(type.get())) {
            status = ordersEntity.Status.UNCONFIRM;
        } else if (ordersEntity.Status.CANCEL.toString().equals(type.get())) {
            status = ordersEntity.Status.CANCEL;
        } else if (ordersEntity.Status.DELIVERING.toString().equals(type.get())) {
            status = ordersEntity.Status.DELIVERING;
        } else {
            status = ordersEntity.Status.DELIVERED;
        }

        if (title.isPresent()) {  // if title is present
            Sort sort;
            if (orderBy.toUpperCase().equals("DESC")) {
                sort = Sort.by(Sort.Order.desc(typeSort));
            } else {
                sort = Sort.by(Sort.Order.asc(typeSort));
            }

            pageable = PageRequest.of(page - 1, size, sort);  // using PageRequest.of() instead of the deprecated constructor
            Result = ordersRepository.findByIdAndStatus(title.get(), status, pageable);
        } else {  // if title is not present, fetch all orders
            Sort sort;
            if (orderBy.toUpperCase().equals("DESC")) {
                sort = Sort.by(Sort.Order.desc(typeSort));
            } else {
                sort = Sort.by(Sort.Order.asc(typeSort));
            }

            pageable = PageRequest.of(page - 1, size, sort);  // using PageRequest.of() instead of the deprecated constructor
            Result = ordersRepository.findByStatus(status, pageable);
        }


        resultPage.setPage(Result.getNumber() + 1);
        resultPage.setListResult(parseListOrderDTO(Result.getContent()));
        resultPage.setTotalpage(Result.getTotalPages());
        return resultPage;
    }


    public boolean createOrders(infoOrderDTO infoOrder, String username) throws MessagingException {
        customersEntity customersEntity = customersRepository.findByUsers_id(username);
        List<orderdetailEntity> listOrderDetail = new ArrayList<>();
        float total = 0;
        for (shopcartDTO shopcartDTO : infoOrder.getShopcart()) {
            shopcartEntity shopcart = shopcartRepository.findById(shopcartDTO.getId());
            if (shopcart == null || !shopcart.getCustomers().getUsersEntitys().getUsername().equals(username)) {
                return false;
            }

            //price
            float price = (shopcart.getProductdetail().getProductsEntity().getPrice() * shopcart.getQuantity());
            if (shopcart.getProductdetail().getProductsEntity().getDiscountEntitys() != null) {
                float discount = price * (shopcart.getProductdetail().getProductsEntity().getDiscountEntitys().getPercent() / 100);
                price -= discount;
            }

            total += price;

            // inventory product
            productdetailEntity productdetail = productdetailRepository.findById(shopcartDTO.getProductdetail().getId());
            if (productdetail.getInventory() == 0) {
                return false;
            }
            long valueNew = productdetail.getInventory() - shopcartDTO.getQuantity();
            if (valueNew < 0) {
                return false;
            }
            productdetail.setInventory(valueNew);
            productdetailRepository.save(productdetail);

            //create orderdetail
            orderdetailEntity orderdetail = new orderdetailEntity();
            orderdetail.setProductdetailEntity(shopcart.getProductdetail());
            orderdetail.setQuantity(shopcart.getQuantity());
            orderdetail.setPrice(price);
            listOrderDetail.add(orderdetail);

            shopcartRepository.delete(shopcart);
        }

        //create order
        // id
        Date date = new Date();
        String id = customersEntity.getUsersEntitys().getUsername().substring(0, 2) + String.valueOf(customersEntity.getId()) + date.getTime();

        ordersEntity ordersEntity = new ordersEntity();
        ordersEntity.setId(id);
        ordersEntity.setAddress(infoOrder.getAddress());
        ordersEntity.setCreatedDate(date);
        ordersEntity.setEmail(infoOrder.getEmail());
        ordersEntity.setFullname(infoOrder.getFullname());
        ordersEntity.setPhone(infoOrder.getPhone());
        ordersEntity.setStatus(api.entity.ordersEntity.Status.UNCONFIRM);
        ordersEntity.setCustomersEntity(customersEntity);
        paymentEntity finalPayment = new paymentEntity();
        if (infoOrder.getPaymentid() == 0) {
            paymentEntity payment = new paymentEntity();
            payment.setName("SHIPCOD");
            finalPayment = paymentRepository.save(payment);
        } else {
            finalPayment = paymentRepository.findById(infoOrder.getPaymentid());
        }
        ordersEntity.setPaymentEntity(finalPayment);
        ordersEntity.setTotal(total);
//        ordersEntity.setOrderdetailEntities(listOrderDetail);
        final ordersEntity finalOrder = ordersRepository.save(ordersEntity);

        // cascade type ALl not working , i will fix
        for (orderdetailEntity order : listOrderDetail) {
            order.setOrders(finalOrder);
            orderdetailRepository.save(order);
        }

//        sendMailService.sendHtmlEmail(finalOrder.getEmail(),"Đơn Hàng "+finalOrder.getCreatedDate(),finalOrder);
        return true;
    }

    public Boolean cancelOrder(ordersDTO orderDTO, String username) {
        // Use Optional to avoid null handling issues
        Optional<ordersEntity> optionalOrder = ordersRepository.findById(orderDTO.getId());

        // If the order is not found, return false
        if (!optionalOrder.isPresent()) {
            return false;
        }

        ordersEntity order = optionalOrder.get();

        // Iterate over order details to update inventory
        for (orderdetailEntity orderdetail : order.getOrderdetailEntities()) {
            // Update product inventory
            productdetailEntity productdetail = orderdetail.getProductdetailEntity();
            long valueNew = productdetail.getInventory() + orderdetail.getQuantity();
            productdetail.setInventory(valueNew);

            // Save the updated product details
            productdetailRepository.save(productdetail);
        }

        // Cancel the order
        order.setStatus(ordersEntity.Status.CANCEL);
        order.setModifiedDate(new Date());
        order.setModifiedBy(username);

        // Save the updated order
        ordersRepository.save(order);

        return true;
    }


    public Boolean acceptOrder(ordersDTO orderDTO, String username) {
        // Use Optional to avoid null handling issues
        Optional<ordersEntity> optionalOrder = ordersRepository.findById(orderDTO.getId());

        // If the order is not found, return false
        if (!optionalOrder.isPresent()) {
            return false;
        }

        ordersEntity order = optionalOrder.get();

        // Iterate over order details to update product ratings
        for (orderdetailEntity orderdetail : order.getOrderdetailEntities()) {
            // Get associated product entity
            productsEntity productEntity = orderdetail.getProductdetailEntity().getProductsEntity();

            // Update product rating based on the quantity of the order
            int valueNew = productEntity.getRating() + orderdetail.getQuantity();
            productEntity.setRating(valueNew);

            // Save the updated product entity
            productsRepository.save(productEntity);
        }

        // Accept the order (change status to DELIVERING)
        order.setStatus(ordersEntity.Status.DELIVERING);
        order.setModifiedDate(new Date());
        order.setModifiedBy(username);

        // Save the updated order entity
        ordersRepository.save(order);

        return true;
    }
    public Boolean confirmOrder(ordersDTO orderDTO, String username) {
        // Use Optional to avoid null handling issues
        Optional<ordersEntity> optionalOrder = ordersRepository.findById(orderDTO.getId());

        // If the order is not found, return false
        if (!optionalOrder.isPresent()) {
            return false;
        }

        ordersEntity order = optionalOrder.get();

        // Confirm the order by updating its status to DELIVERED
        order.setStatus(ordersEntity.Status.DELIVERED);
        order.setModifiedDate(new Date());
        order.setModifiedBy(username);

        // Save the updated order entity
        ordersRepository.save(order);

        return true;
    }


    public List<ordersDTO> parseListOrderDTO(List<ordersEntity> listOrder) {
        List<ordersDTO> list = listOrder.stream().map(ordersEntity -> {
            ordersDTO orders = modelMapper.map(ordersEntity, ordersDTO.class);
//                    orders.setPaymentEntity(ordersEntity.getPaymentEntity().getId());
            // orderdetail dto
            List<orderdetailDTO> listOrderdetailDTO = ordersEntity.getOrderdetailEntities().stream().map(orderdetailEntity -> {
                orderdetailDTO orderdetailDTO = modelMapper.map(orderdetailEntity, api.DTO.orderdetailDTO.class);
                orderdetailDTO.setProductid(orderdetailEntity.getProductdetailEntity().getProductsEntity().getId());
                orderdetailDTO.setSize(orderdetailEntity.getProductdetailEntity().getSize());
                return orderdetailDTO;
            }).collect(Collectors.toList());
            orders.setListOrderdetail(listOrderdetailDTO);
            return orders;
        }).collect(Collectors.toList());
        return list;
    }


    public Object DoanhThu(String mode, long idpro, String strDateFrom, String strDateTo) throws ParseException {
        Float[] strfloat = new Float[2];
        //doanh thu datefrom and dateto
        Date datefrom = new SimpleDateFormat("dd/MM/yyyy").parse(strDateFrom);
        Date dateto = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(strDateTo + " 23:59:59");
        float totaldoanhthu = 0;
        float totalloinhuan = 0;
        ordersEntity.Status status = ordersEntity.Status.DELIVERED;
        List<ordersEntity> list = ordersRepository.findByModifiedDateBetweenAndStatus(datefrom, dateto, status);
        int number = 0;
        if (mode.equals("DETAIL")) {
            // lọc theo mã chi tiết sản phẩm
            for (ordersEntity order : list) {
                for (orderdetailEntity orderdetail : order.getOrderdetailEntities()) {
                    if (orderdetail.getProductdetailEntity().getId() == idpro) {
                        number += orderdetail.getQuantity();
                        totaldoanhthu += orderdetail.getPrice() * orderdetail.getQuantity();
                    }
                }
            }
            strfloat[0] = totaldoanhthu;
            //get repository
            int count = 0;
            List<repositoryEntity> listrepo = repositoryRepository.findAll();
            System.out.println("size " + listrepo.size());
            for (repositoryEntity repo : listrepo) {
                if (repo.getProductdetail().getId() == idpro) {
                    count++;
                    totalloinhuan += repo.getPrice();
                }
            }
            if (count != 0) {
                totalloinhuan = totalloinhuan / count;
                totalloinhuan = totaldoanhthu - (totalloinhuan * number);
            }
            strfloat[1] = totalloinhuan;
        } else if (mode.equals("PRODUCT")) {
            // lọc theo mã sản phẩm
            for (ordersEntity order : list) {
                for (orderdetailEntity orderdetail : order.getOrderdetailEntities()) {
                    if (orderdetail.getProductdetailEntity().getProductsEntity().getId() == idpro) {
                        number += orderdetail.getQuantity();
                        totaldoanhthu += orderdetail.getPrice() * orderdetail.getQuantity();
                    }
                }
            }
            strfloat[0] = totaldoanhthu;
            //get repository
            int count = 0;
            List<repositoryEntity> listrepo = repositoryRepository.findAll();
            for (repositoryEntity repo : listrepo) {
                if (repo.getProductdetail().getProductsEntity().getId() == idpro) {
                    count++;
                    totalloinhuan += repo.getPrice();
                }
            }
            if (count != 0) {
                totalloinhuan = totalloinhuan / count;
                totalloinhuan = totaldoanhthu - (totalloinhuan * number);
            }
            strfloat[1] = totalloinhuan;
        } else {
            // lọc tất cả sản phẩm
            ArrayList<Long> arrId = new ArrayList<>();
            ArrayList<Float> arrPrice = new ArrayList<>();
            float tongnhap = 0;
            List<repositoryEntity> listrepo = repositoryRepository.findAll();
            for (ordersEntity order : list) {
                for (orderdetailEntity orderdetail : order.getOrderdetailEntities()) {
                    // check
                    int check = 0;
                    for (int i = 0; i < arrId.size(); i++) {
                        if (orderdetail.getId() == arrId.get(i)) {
                            check = 1;
                            tongnhap += arrPrice.get(i) * orderdetail.getQuantity();
                        }
                    }
                    if (check == 0) {
                        float totalnhaphang = 0;
                        int count = 0;
                        for (repositoryEntity repo : listrepo) {
                            if (repo.getProductdetail().getId() == orderdetail.getProductdetailEntity().getId()) {
                                count++;
                                totalnhaphang += repo.getPrice();
                            }
                        }
                        if (count != 0) {
                            float tbnhaphnag = totalnhaphang / count;
                            arrId.add(orderdetail.getId());
                            arrPrice.add(tbnhaphnag);
                            tongnhap += tbnhaphnag * orderdetail.getQuantity();
                        }
                    }
                }
                totaldoanhthu += order.getTotal();
            }
            totalloinhuan = totaldoanhthu - tongnhap;
            strfloat[0] = totaldoanhthu;
            strfloat[1] = totalloinhuan;
        }
        return strfloat;
    }

    public Object tinhTrangDonHang(String mode, long idpro, String strDateFrom, String strDateTo) throws ParseException {
        Integer[] strint = new Integer[4];
        Date datefrom = new SimpleDateFormat("dd/MM/yyyy").parse(strDateFrom);
        Date dateto = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(strDateTo + " 23:59:59");
        int totalsuccess = 0;
        int totalcancel = 0;
        int totaldelivering = 0;
        int totalunconfirm = 0;
        if (mode.equals("DETAIL")) {
            // tt don hang success
            ordersEntity.Status status = ordersEntity.Status.DELIVERED;
            List<ordersEntity> list = ordersRepository.findByModifiedDateBetweenAndStatus(datefrom, dateto, status);

            for (ordersEntity order : list) {
                for (orderdetailEntity orderdetail : order.getOrderdetailEntities()) {
                    if (orderdetail.getProductdetailEntity().getId() == idpro) {
                        totalsuccess++;
                    }
                }
            }

            // tt don hang cancel
            ordersEntity.Status status1 = ordersEntity.Status.CANCEL;
            List<ordersEntity> list1 = ordersRepository.findByModifiedDateBetweenAndStatus(datefrom, dateto, status1);

            for (ordersEntity order : list1) {
                for (orderdetailEntity orderdetail : order.getOrderdetailEntities()) {
                    if (orderdetail.getProductdetailEntity().getId() == idpro) {
                        totalcancel++;
                    }
                }
            }

            // tt don hang delivering
            ordersEntity.Status status2 = ordersEntity.Status.DELIVERING;
            List<ordersEntity> list2 = ordersRepository.findByModifiedDateBetweenAndStatus(datefrom, dateto, status2);

            for (ordersEntity order : list2) {
                for (orderdetailEntity orderdetail : order.getOrderdetailEntities()) {
                    if (orderdetail.getProductdetailEntity().getId() == idpro) {
                        totaldelivering++;
                    }
                }
            }

            // tt don hang delivering
            ordersEntity.Status status3 = ordersEntity.Status.UNCONFIRM;
            List<ordersEntity> list3 = ordersRepository.findByCreatedDateBetweenAndStatus(datefrom, dateto, status3);

            for (ordersEntity order : list3) {
                for (orderdetailEntity orderdetail : order.getOrderdetailEntities()) {
                    if (orderdetail.getProductdetailEntity().getId() == idpro) {
                        totalunconfirm++;
                    }
                }
            }

            strint[0] = totalsuccess;
            strint[1] = totalcancel;
            strint[2] = totaldelivering;
            strint[3] = totalunconfirm;
            return strint;
        } else if (mode.equals("PRODUCT")) {
            // tt don hang success
            ordersEntity.Status status = ordersEntity.Status.DELIVERED;
            List<ordersEntity> list = ordersRepository.findByModifiedDateBetweenAndStatus(datefrom, dateto, status);
            for (ordersEntity order : list) {
                for (orderdetailEntity orderdetail : order.getOrderdetailEntities()) {
                    if (orderdetail.getProductdetailEntity().getProductsEntity().getId() == idpro) {
                        totalsuccess++;
                    }
                }
            }

            // tt don hang cancel
            ordersEntity.Status status1 = ordersEntity.Status.CANCEL;
            List<ordersEntity> list1 = ordersRepository.findByModifiedDateBetweenAndStatus(datefrom, dateto, status1);

            for (ordersEntity order : list1) {
                for (orderdetailEntity orderdetail : order.getOrderdetailEntities()) {
                    if (orderdetail.getProductdetailEntity().getProductsEntity().getId() == idpro) {
                        totalcancel++;
                    }
                }
            }

            // tt don hang delivering
            ordersEntity.Status status2 = ordersEntity.Status.DELIVERING;
            List<ordersEntity> list2 = ordersRepository.findByModifiedDateBetweenAndStatus(datefrom, dateto, status2);

            for (ordersEntity order : list2) {
                for (orderdetailEntity orderdetail : order.getOrderdetailEntities()) {
                    if (orderdetail.getProductdetailEntity().getProductsEntity().getId() == idpro) {
                        totaldelivering++;
                    }
                }
            }

            // tt don hang delivering
            ordersEntity.Status status3 = ordersEntity.Status.UNCONFIRM;
            List<ordersEntity> list3 = ordersRepository.findByCreatedDateBetweenAndStatus(datefrom, dateto, status3);

            for (ordersEntity order : list3) {
                for (orderdetailEntity orderdetail : order.getOrderdetailEntities()) {
                    if (orderdetail.getProductdetailEntity().getProductsEntity().getId() == idpro) {
                        totalunconfirm++;
                    }
                }
            }

            strint[0] = totalsuccess;
            strint[1] = totalcancel;
            strint[2] = totaldelivering;
            strint[3] = totalunconfirm;
            return strint;
        } else {

            // tt don hang success
            ordersEntity.Status status = ordersEntity.Status.DELIVERED;
            List<ordersEntity> list = ordersRepository.findByModifiedDateBetweenAndStatus(datefrom, dateto, status);
            // tt don hang cancel
            ordersEntity.Status status1 = ordersEntity.Status.CANCEL;
            List<ordersEntity> list1 = ordersRepository.findByModifiedDateBetweenAndStatus(datefrom, dateto, status1);
            // tt don hang delivering
            ordersEntity.Status status2 = ordersEntity.Status.DELIVERING;
            List<ordersEntity> list2 = ordersRepository.findByModifiedDateBetweenAndStatus(datefrom, dateto, status2);
            // tt don hang delivering
            ordersEntity.Status status3 = ordersEntity.Status.UNCONFIRM;
            List<ordersEntity> list3 = ordersRepository.findByCreatedDateBetweenAndStatus(datefrom, dateto, status3);

            strint[0] = list.size();
            strint[1] = list1.size();
            strint[2] = list2.size();
            strint[3] = list3.size();
            return strint;
        }
    }
}
