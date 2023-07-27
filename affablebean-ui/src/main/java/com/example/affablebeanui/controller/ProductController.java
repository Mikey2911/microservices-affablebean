package com.example.affablebeanui.controller;

import com.example.affablebeanui.entity.Product;
import com.example.affablebeanui.model.CartItem;
import com.example.affablebeanui.service.CartService;
import com.example.affablebeanui.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/webui")
public class ProductController {

    private final ProductService productService;
    private final CartService cartService;


    @QueryMapping
    public List<CartItem> cartItems(){
        return cartService.getAllProducts().stream().map(
                p -> new CartItem(p.getId(),p.getName(),p.getPrice()
                        ,p.getDescription(),p.getQuantity(),p.getLastUpdate())).collect(Collectors.toList());
    }


    @GetMapping("/products/{id}")
    @ResponseBody
    public List<Product> showAll(@PathVariable int id){
        return productService.findProductByCategory(id);
    }

    @GetMapping("/products/category")
    public String showProduct(@RequestParam int id, Model model){
        model.addAttribute("products", productService.findProductByCategory(id));
        return "products";
    }

    @GetMapping("/product/purchase")
    public String addToCarts(@RequestParam int id){
        var product = productService.findProductById(id);
        productService.purchaseProduct(id);
        return "redirect:/webui/products/category?id="+product.getCategory().getId();
    }

    @GetMapping("/")
    public String home(@RequestParam(value = "transfer", defaultValue = "false")boolean transfer, Model model){
        model.addAttribute("transfer",transfer);
        return "home";
    }

    @ModelAttribute("cartSize")
    public int cartSize(){
        return cartService.cartSize();
    }

    @GetMapping("/product/cartView")
    public String viewCart(Model model){
        model.addAttribute("cartItems",cartService.getAllProducts());
        model.addAttribute("product",new Product());
        return "cartView";
    }

    @GetMapping("/cart/clear")
    public String clearCart(){
        cartService.clearCart();
        return "redirect:/webui/";
    }

    @PostMapping("/product/checkout")
    public String checkOut(Product product){
        int i = 0;
        System.out.println("======================" + product.getQuantityList());
        for (Product cartItem:cartService.getAllProducts()){
            cartItem.setQuantity(product.getQuantityList().get(i));
            i++;
        }
        return "redirect:/webui/checkout-view";
    }



    @GetMapping("/checkout-view")
    public String toCheckoutView(Model model){
        model.addAttribute("transferError",model.containsAttribute("transferError"));
        return "checkoutView";
    }

    @GetMapping("/transfer")
    public String checkoutTransfer(@ModelAttribute("total")double total, RedirectAttributes redirectAttributes){
        ResponseEntity responseEntity = productService.transfer("john@gmail.com","mary@gmail.com",total);
        if(responseEntity.getStatusCode().is2xxSuccessful()){
            cartService.clearCart();
            redirectAttributes.addFlashAttribute("transfer",true);
            return "redirect:/";
        }
        redirectAttributes.addAttribute("transferError",true);
        return "redirect:/webui/checkout-view";
    }

    /*@GetMapping("/transfer")
    public String checkoutTransfer(@ModelAttribute("total") double total, RedirectAttributes redirectAttributes){
        var responseEntity =productService.transfer("marry@gmail.com", "john@gmail.com", total);
        if(responseEntity){
            cartService.clearCart();
            return "redirect:/webui/?transfer=true";
        }
        return "redirect:/webui/checkoutView?transferError=true";
    }*/

    @ModelAttribute("total")
    public double total(){
        return cartService.getAllProducts().stream()
                .map(p -> p.getQuantity() * p.getPrice())
                .mapToDouble(i -> i).sum();
    }

    @GetMapping("/transport")
    public String transport(){
        ResponseEntity responseEntity = productService.saveCartItem();
        if(responseEntity.getStatusCode().is2xxSuccessful()){
            return "redirect:/";
        }
        return "redirect:/webui/checkout-view";
    }
}
