package shop.mtcoding.orange.model;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

//mybatis에서 제공하는 라이브러리임
// interface라 new가 절대 안 됨
// <mapper namespace="shop.mtcoding.orange.model.ProductRepository"> 이 안에서
// select하나하나를 메서드로 판단하고 클래스로 만듬

// 중간언어인 xml을 보고 이걸 알아서 만듬
// IoC에 ProductRepository하나밖에 없으므로 autowired로 알아서 떙김
// class ProductRepositoryIMPI implements ProductRepository {
//     public List<Product> findAll() {
//         결과 s = em.createQuery("select * from product");
//         // 매핑
//         return s;
//     }

//     public Product findOne(int id) {
//         결과 s = em.createQuery("select * from product where id = " + id);
//         // 매핑
//         return s;
//     }
// }

@Mapper
public interface ProductRepository {
    public List<Product> findAll(); // finAll = select id 값

    public Product findOne(int id);

    // void -> int 변경이유 : -1 DB에러, 1 변경된 행이 1건, 0 변경된 행이 없다. (프로토콜임)
    // 이걸 작성하면 controller에서 받아서 xml에 던짐
    public int insert(@Param("name") String name, @Param("price") int price, @Param("qty") int qty);

    public int delete(@Param("id") int id);

    // 다음엔 class로 넘기는게 더 효율적
    public int update(@Param("id") int id, @Param("name") String name, @Param("price") int price,
            @Param("qty") int qty);
}

// id가 어떤 타입인지, 몇개를 리턴할지 모름, 이건 최소한 우리가 해야 함
// ProductRepositoryIMPI(이름 상관 x) 얘를 new해서 만들 것임
// return 타입 -> List<Product> Product, 매개변수 int 이런 것을 이제 알 수있음
// 1. 쿼리실행 실행 응답 이런순서로 동작
// ProductRepositoryIMPI 이것을 다형성 떄문에 부모타입으로 불러올 수 있음