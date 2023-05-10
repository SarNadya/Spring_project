package com.example.final_project.controllers;

import com.example.final_project.enumeration.Status;
import com.example.final_project.models.Cart;
import com.example.final_project.models.Order;
import com.example.final_project.models.Product;
import com.example.final_project.repositories.CartRepository;
import com.example.final_project.repositories.OrderRepository;
import com.example.final_project.repositories.ProductRepository;
import com.example.final_project.security.PersonDetails;
import com.example.final_project.services.OrderService;
import com.example.final_project.services.PersonService;
import com.example.final_project.services.ProductService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class MainController {

    private final ProductService productService;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    private final OrderRepository orderRepository;

    private  final OrderService orderService;

    private final PersonService personService;

    public MainController(ProductService productService, ProductRepository productRepository, CartRepository cartRepository, OrderRepository orderRepository, OrderService orderService, PersonService personService) {
        this.productService = productService;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.orderService = orderService;
        this.personService = personService;
    }

    @GetMapping("/person_account")
    public String index(Model model){
        // Получаем объект аутентификации, с помощью SpringContextHolder обращаемся к контексту и на нем вызываем метод аутентификации. Из сессии текущего пользователя получаем объект, который был положен в данную сессию после аутентификации пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        String role = personDetails.getPerson().getRole();
        if(role.equals("ROLE_ADMIN")){
            return "redirect:/admin";
        }
        model.addAttribute("products", productService.getAllProduct());
        return "user/index";
    }

    @GetMapping("/person_account/product/info/{id}")
    public String infoProduct(@PathVariable("id") int id, Model model){
        model.addAttribute("product", productService.getProductId(id));
        return "/user/infoProduct";
    }

    @PostMapping("/person_account/product/search")
    public String productSearch(@RequestParam("search") String search, @RequestParam("min") String min, @RequestParam("max") String max, @RequestParam(value = "price", required = false, defaultValue = "") String price, @RequestParam(value = "contract", required = false, defaultValue = "")String contract, Model model){
        model.addAttribute("products", productService.getAllProduct());

        // перебираем варианты поиска, фильтрации и сортировки
        if(!min.isEmpty() & !max.isEmpty()){
            if(!price.isEmpty()){
                if(price.equals("sorted_by_ascending_price")) {
                    if (!contract.isEmpty()) {
                        if (contract.equals("furniture")) {
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceAsc(search.toLowerCase(), Float.parseFloat(min), Float.parseFloat(max), 1));
                        } else if (contract.equals("appliances")) {
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceAsc(search.toLowerCase(), Float.parseFloat(min), Float.parseFloat(max), 3));
                        } else if (contract.equals("clothes")) {
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceAsc(search.toLowerCase(), Float.parseFloat(min), Float.parseFloat(max), 2));
                        }
                    } else {
                        model.addAttribute("search_product", productRepository.findByTitleOrderByPriceAsc(search.toLowerCase(), Float.parseFloat(min), Float.parseFloat(max)));
                    }
                } else if(price.equals("sorted_by_descending_price")){
                    if(!contract.isEmpty()){
                        System.out.println(contract);
                        if(contract.equals("furniture")){
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(min), Float.parseFloat(max), 1));
                        }else if (contract.equals("appliances")) {
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(min), Float.parseFloat(max), 3));
                        } else if (contract.equals("clothes")) {
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(min), Float.parseFloat(max), 2));
                        }
                    }  else {
                        model.addAttribute("search_product", productRepository.findByTitleOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(min), Float.parseFloat(max)));
                    }
                }
            } else {
                model.addAttribute("search_product", productRepository.findByTitleAndPriceGreaterThanEqualAndPriceLessThanEqual(search.toLowerCase(), Float.parseFloat(min), Float.parseFloat(max)));
            }
        } else {
            model.addAttribute("search_product", productRepository.findByTitleContainingIgnoreCase(search));
        }

        model.addAttribute("value_search", search);
        model.addAttribute("value_price_min", min);
        model.addAttribute("value_price_max", max);
        return "/product/product";

    }

    @GetMapping("/cart/add/{id}")
    public String addProductInCart(@PathVariable("id") int id, Model model){
        // Получаем товар по id
        Product product = productService.getProductId(id);
        // Извлекаем объект аутентифицированного пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        // Извлекаем id пользователя из объекта
        int id_person = personDetails.getPerson().getId();
        Cart cart = new Cart(id_person, product.getId());
        cartRepository.save(cart);
        return "redirect:/cart";
    }

    @GetMapping("/cart")
    public String cart(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        // Извлекаем id пользователя из объекта
        int id_person = personDetails.getPerson().getId();

        List<Cart> cartList = cartRepository.findByPersonId(id_person);
        List<Product> productList = new ArrayList<>();

        // Получаем товар из корзины по id
        for (Cart cart: cartList) {
            productList.add(productService.getProductId(cart.getProductId()));
        }

        // Вычисление итоговой цены
        float price = 0;
        for (Product product: productList) {
            price += product.getPrice();
        }

        model.addAttribute("price", price);
        model.addAttribute("cart_product", productList);
        return "/user/cart";
    }

    @GetMapping("/cart/delete/{id}")
    public String deleteProductFromCart(Model model, @PathVariable("id") int id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        // Извлекаем id пользователя из объекта
        int id_person = personDetails.getPerson().getId();
        List<Cart> cartList = cartRepository.findByPersonId(id_person);
        List<Product> productList = new ArrayList<>();

        // Получаем продукты из корзины по id товара
        for (Cart cart: cartList) {
            productList.add(productService.getProductId(cart.getProductId()));
        }
        cartRepository.deleteCartByProductId(id);
        return "redirect:/cart";
    }

    @GetMapping("/order/create")
    public String order(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        // Извлекаем id пользователя из объекта
        int id_person = personDetails.getPerson().getId();

        List<Cart> cartList = cartRepository.findByPersonId(id_person);
        List<Product> productList = new ArrayList<>();

        // Получаем продукты из корзины по id товара
        for (Cart cart: cartList) {
            productList.add(productService.getProductId(cart.getProductId()));
        }

        // Вычисление итоговой цены
        float price = 0;
        for (Product product: productList) {
            price += product.getPrice();
        }

        String uuid = UUID.randomUUID().toString();
        for(Product product : productList){
            Order newOrder = new Order(uuid, product, personDetails.getPerson(), 1, product.getPrice(), Status.Принят);
            orderRepository.save(newOrder);
            cartRepository.deleteCartByProductId(product.getId());
        }
        return "redirect:/orders";
    }

    @GetMapping("/orders")
    public String orderUser(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        List<Order> orderList = orderRepository.findByPerson(personDetails.getPerson());
        model.addAttribute("orders", orderList);
        return "/user/orders";
    }

    @GetMapping("/admin/orders")
    public String getAllOrder(Model model){
        model.addAttribute("orders", orderService.getAllOrder());
        return "/admin/adminOrders";
    }

    @GetMapping("/order/info/{id}")
    public String infoOrder(@PathVariable("id") int id, Model model){
        model.addAttribute("orders", orderService.getOrderId(id));
        return "/admin/infoOrder";
    }

    @PostMapping("/order/info/{id}")
    public String editStatusOrder(@PathVariable("id") int id, @RequestParam("status") Status status){
        orderService.updateOrderStatus(id, status);
        return "redirect:/order/info/{id}";
    }

    @PostMapping("/admin/orders/search")
    public String orderSearch(@RequestParam("search") String search, Model model) {
        model.addAttribute("orders", orderService.getAllOrder());
        model.addAttribute("search_order", orderService.findOrder(search));
        model.addAttribute("value_search", search);
        return "/admin/adminOrders";
    }

    @GetMapping("/admin/users")
    public String getAllPerson(Model model){
        model.addAttribute("persons", personService.getAllPerson());
        return "/admin/users";
    }

    @GetMapping("/admin/users/info/{id}")
    public String getInfoPerson(@PathVariable("id") int id, Model model){
        model.addAttribute("persons", personService.getPersonId(id));
        return "/admin/infoUser";
    }

    @PostMapping("/admin/users/info/{id}")
    public String changeUserRole(@PathVariable("id") int id, @RequestParam("role") String role){
        personService.changePersonRole(id, role);
        return "redirect:/admin/users/info/{id}";
    }

}
