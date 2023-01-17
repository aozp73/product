package shop.mtcoding.orange.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import shop.mtcoding.orange.model.Product;
import shop.mtcoding.orange.model.ProductRepository;

@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private HttpSession session;

    @Autowired
    private HttpServletRequest request;

    // redirect 요청을 하면 기본적으로 파일을 못 찾음 (ViewResolver 설정을 바꿔야 함)
    // response.sendRedirect("/test");
    // 최초에 /redeirect를 요청했지만 주소가 /test로 바뀜
    @GetMapping("/test")
    public String test() {
        return "test";
    }

    // Tomcat이 저 객체로 만들어서 DispatchServlet한테 전달
    // DispatchServlet이 주소를 파싱하여 메서드를 떄려 줌
    // return이 void라 Spring 도움 안 받음
    @GetMapping("/redirect")
    public void redirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // HttpSession session = request.getSession();
        // 키값이 동일하다면 범위가 작은 것 부터 찾음 (page -> request -> session)
        // redirect라 requst엔 없으므로 session에서 찾음
        // session.setAttribute("name", "session metacoding");

        // 아래 코드는 model.addAttrebute와 동일
        request.setAttribute("name", "metacoding");
        // view resolver가 발동하지 못하므로 못 찾음
        response.sendRedirect("/test");
    }

    @GetMapping("/dispatcher")
    public void dispatcher(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("name", "metacoding");
        RequestDispatcher dis = request.getRequestDispatcher("/test");
        // 새로운 request를 생성 하는데
        // 덮어 씌워진 거라 요청하는 사람 입장에서는 redirect 된게 아니구나 인식 함
        dis.forward(request, response);
    }

    @GetMapping({ "/", "/product" })
    public String findAll(Model model) {
        List<Product> productList = productRepository.findAll();
        model.addAttribute("productList", productList);
        return "product/main"; // 이 때 request가 새로 만들어짐 -> 덮어씌움 (프레임 워크가 해주는 것)
    }

    @GetMapping("/product/{id}")
    public String findOne(@PathVariable int id, Model model) {
        Product product = productRepository.findOne(id);
        model.addAttribute("p", product); // 공부에서 헷갈릴까봐 p로잡음 원래 key값 동일 하게
        return "product/detail";
    }

    @GetMapping("/api/product")
    @ResponseBody // 이걸 붙이면 붙인 애만 @RestController 처럼 동작 함 (파일이 아닌 오브젝트 리턴 가능)
    public List<Product> apiFindAllProduct() {
        List<Product> productList = productRepository.findAll();
        return productList;
    }

    @GetMapping("/api/product/{id}") // pathVariable
    @ResponseBody
    public Product apiFindOneProduct(@PathVariable int id) {
        // 여기 id가 xm.파일의 #{id}에 들어갈 거임
        Product product = productRepository.findOne(id);
        return product;
    }

    // viewResolver가 발동해서 단순히 페이지 return
    @GetMapping("/product/addForm")
    public String addForm() {
        return "product/addForm";
    }

    @PostMapping("/product/add")
    // 전송하는 쪽에서 x-www-form으로 보낸다는 것임
    public String add(String name, int price, int qty) {
        int result = productRepository.insert(name, price, qty);
        if (result == 1) {
            return "redirect:/product";
        } else {
            return "redirect:/product/addForm";
        }
    }

    @PostMapping("/product/{id}/delete")
    public String delete(@PathVariable int id) {
        int result = productRepository.delete(id);
        if (result == 1) {
            return "redirect:/";
        } else {
            return "redirect:/product/" + id;
        }
    }

    @GetMapping("/product/{id}/updateForm")
    public String update(@PathVariable int id, Model model) {
        // updateForm 페이지는 정적인 페이지가 아님.
        // 있는 값을 지워서 수정
        Product product = productRepository.findOne(id);
        model.addAttribute("p", product);

        // update는 model 사용해야 함
        return "product/updateForm";
    }

    @PostMapping("/product/{id}/update")
    public String update(@PathVariable int id, String name, int price, int qty) {
        int result = productRepository.update(id, name, price, qty);
        if (result == 1) {
            return "redirect:/";
        } else {
            return "redirect:/product/" + id + "/updateForm";
        }
    }
}

// @PathVariable 안 쓴다면 HttpServletRequest request해서 파싱해야함