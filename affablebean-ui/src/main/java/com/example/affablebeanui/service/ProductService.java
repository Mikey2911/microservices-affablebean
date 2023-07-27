package com.example.affablebeanui.service;

import com.example.affablebeanui.entity.Product;
import com.example.affablebeanui.entity.Products;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final int DELIVERY_CHARGE = 3;

    private final CartService cartService;
    private List<Product> products;

    private RestTemplate restTemplate = new RestTemplate();


    @Value("${backend.url}")
    private String base_url;

    public ProductService(CartService cartService1){
        this.cartService = cartService1;
        var productResponse = restTemplate.getForEntity("http://localhost:8090/backend/products", Products.class);
        if(productResponse.getStatusCode().is2xxSuccessful()){
            products = Objects.requireNonNull(productResponse.getBody()).getProducts();
            return;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    public List<Product> showAllProducts(){
        return products;
    }

    public List<Product> findProductByCategory(int categoryId){
      return products.stream().filter(p -> p.getCategory().getId() == categoryId).collect(Collectors.toList());
    }

    public Product findProductById(int id){
        return products.stream().filter(p -> p.getId() == id).findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public void purchaseProduct(int id){
        cartService.addToCart(findProductById(id));
    }

    record TransferData(String from_email,String to_email,double amount){}


   public ResponseEntity transfer(String from_email,String to_email,double amount){
        var data = new TransferData(from_email,to_email,amount + DELIVERY_CHARGE);
        return restTemplate.postForEntity("http://localhost:8070/account/transfer",data,String.class);
    }

  /*  public boolean transfer(String fromEmail, String toEmail, double amount) {
        try {
            var result = restTemplate.postForEntity("http://localhost:8070/account/transfer", new TransferData(fromEmail, toEmail, amount), ResponseData.class);
            if(result.){
                return true;
            }
        }catch (Exception e) {
            return false;
        }
        return false;
    }*/

    public ResponseEntity saveCartItem(){
        return restTemplate.getForEntity("http://localhost:8060/transport/cart/save",String.class);
    }
}
