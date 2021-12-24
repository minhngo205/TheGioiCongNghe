package minh.project.multishop.database.repository;

import minh.project.multishop.database.AppDatabase;
import minh.project.multishop.database.DatabaseUtil;
import minh.project.multishop.database.dao.ProductNameDAO;
import minh.project.multishop.database.entity.ProductName;

import java.util.List;

public class ProductDBRepository {

    private static ProductDBRepository instance;
    private final ProductNameDAO productNameDAO;

    public ProductDBRepository(){
        AppDatabase database = DatabaseUtil.getDatabase();
        this.productNameDAO = database.productNameDAO();
    }

    public static ProductDBRepository getInstance(){
        if(null == instance) instance = new ProductDBRepository();
        return instance;
    }

    public List<ProductName> getAllProductName(){
        return productNameDAO.getAllProductName();
    }

    public List<String> getListNameRecord(){
        return productNameDAO.getListNameRecord();
    }

    public void InsertProductNameData(List<ProductName> productNames){
        productNameDAO.insertName(productNames);
    }
}
