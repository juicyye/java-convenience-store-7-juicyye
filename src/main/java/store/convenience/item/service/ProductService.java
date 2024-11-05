package store.convenience.item.service;

import java.util.ArrayList;
import java.util.List;
import store.convenience.item.controller.req.ProductCreateReqDto;
import store.convenience.item.domain.Item;
import store.convenience.item.domain.Product;
import store.convenience.item.domain.ProductInventory;
import store.convenience.item.service.port.ProductRepository;
import store.convenience.promotion.domain.Promotion;

public class ProductService {

    private final ProductRepository productRepository;
    private final ProductRequestMapper mapper = ProductRequestMapper.getInstance();

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void register(ProductCreateReqDto createReqDto, Promotion promotion) {
        Product product = mapper.create(createReqDto.name(), createReqDto.quantity(), promotion);
        productRepository.save(product);
    }

    public List<Product> getProducts(String itemName) {
        return productRepository.findByName(itemName);
    }

    public List<ProductInventory> findAll(){
        List<ProductInventory> inventory = new ArrayList<>();
        productRepository.findAll().forEach(
                p -> inventory.add(new ProductInventory(p))
        );
        return inventory;
    }

    public void addNonPromotionItems(){
        List<List<Product>> products = productRepository.findAll();
        for (List<Product> product : products) {
            boolean hasPromotion = product.stream().anyMatch(item -> item.getPromotion() != null);
            boolean hasNormal = product.stream().anyMatch(item -> item.getPromotion() == null);

            if(hasPromotion && !hasNormal){
                Item item = product.getFirst().getItem();
                productRepository.save(mapper.create(item.getName(), 0,null));
            }
        }
    }

}
