package com.example.integrate.spring.react.service.impl;

import java.util.List;

import java.util.ArrayList;
import java.util.Optional;

import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.example.integrate.spring.react.exception.ProductApiException;
import com.example.integrate.spring.react.model.Product;
import com.example.integrate.spring.react.service.ProductService;
import com.example.integrate.spring.react.repository.ProductRepository;

import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.LogManager;

import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class ProductRestClient implements ProductService {
     
    private String host="ec2-54-161-255-125.compute-1.amazonaws.com";
    private String port="5432";
    private String username="nefzkgbykdzjob";
    private String password="4a163208688187b2228e16dc20243294454e8918ded1fd440bde603731b2cd3d";
    private String database="ddov8a34vira7t";

    private Connection connection;

	public List<Product> prdts() {
		return getallproducts();
	}

	public Product byId(int id) {
			return getproductbyid(id);		
	}

	public Product addProduct(Product product) {
		try {
        if (product.getCode().length()==10) {
        File file = new File(
            "eur_conversion");
         BufferedReader br
            = new BufferedReader(new FileReader(file));
        String st;
        String token="";
        while ((st = br.readLine()) != null)
            token=token+st;

        float eurconversion = Float.valueOf(0);
        float pricehrk = Float.valueOf(0);
       	try {
          eurconversion=Float.parseFloat(token);
          pricehrk = Float.parseFloat(product.getPricehrk());
    	}
    	catch (NumberFormatException e) {
   	}

        if (pricehrk<0) {
           pricehrk=0;
        }
        product.setPricehrk(String.format("%.2f", pricehrk).replace(",","."));
        product.setPriceeur(String.format("%.2f", pricehrk/eurconversion).replace(",","."));

			Product _product = new Product();
			_product.setCode(product.getCode());
			_product.setName(product.getName());
			_product.setPricehrk(String.format("%.2f", pricehrk).replace(",","."));
			_product.setPriceeur(String.format("%.2f", pricehrk/eurconversion).replace(",","."));
			_product.setDescription(product.getDescription());
			_product.setIsAvailable(product.getIsAvailable());
         if (ifselectmaxidisnull()==0) {
         insertinproductisnull(_product);
	 } else {
         insertinproduct(_product);
         }
         _product.setId((long) getproductid(_product));
			return _product;
        } else
        {
        return null;
	}
		} catch (Exception e) {
			return null;
		}
	}

	public Product updateProduct(int id, Product product) {
		try {
        if (product.getCode().length()==10) {
        File file = new File(
            "eur_conversion");
         BufferedReader br
            = new BufferedReader(new FileReader(file));

        String st;
        String token="";
        while ((st = br.readLine()) != null)
            token=token+st;

        float eurconversion = Float.valueOf(0);
        float pricehrk = Float.valueOf(0);
       	try {
          eurconversion=Float.parseFloat(token);
          pricehrk = Float.parseFloat(product.getPricehrk());
    	}
    	catch (NumberFormatException e) {
   	}

        if (pricehrk<0) {
           pricehrk=0;
        }

        product.setPricehrk(String.format("%.2f", pricehrk).replace(",","."));
        product.setPriceeur(String.format("%.2f", pricehrk/eurconversion).replace(",","."));

			Product _product = new Product();
			_product.setCode(product.getCode());
			_product.setName(product.getName());
			_product.setPricehrk(String.format("%.2f", pricehrk).replace(",","."));
			_product.setPriceeur(String.format("%.2f", pricehrk/eurconversion).replace(",","."));
			_product.setDescription(product.getDescription());
			_product.setIsAvailable(product.getIsAvailable());

        updateproduct(_product, getproductid(_product));

			return _product;
        } else
        {
        return null;
	}
		} catch (Exception e) {
			return null;
		}
	}

	public Product deleteProduct(int id) {
        try {
           Product getProduct =new Product();
           getProduct = getproductbyid(id);
           deleteproduct(id);         
           return getProduct;
	} catch (Exception e) {
            e.printStackTrace(System.out);
           return null;
        }
	}

    public void checkDemo(String table) {
        try {
            this.connect();
            Statement stmt = null;
            String query = "SELECT * FROM " + table;

            stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            //System.out.println("Column " + pkColumn);
            //while (rs.next()) {
            //   String id = new String(rs.getBytes(pkColumn), StandardCharsets.UTF_8);
            //    System.out.println("| Column " + id + " |");
            //}
            this.disconnect();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public void insertinproduct(Product product) {
        try {
            this.connect();
            Statement stmt = null;
            String query = "INSERT INTO products (id, code, name, pricehrk, priceeur, description, isAvailable) VALUES ((SELECT MAX(id)+1 FROM products), '"+product.getCode()+"', '"+product.getName()+"', '"+product.getPricehrk()+"', '"+product.getPriceeur()+"', '"+product.getDescription()+"', "+product.getIsAvailable()+")";

            stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            this.disconnect();
        } catch (Exception e) {
        }
    }

    public void insertinproductisnull(Product product) {
        try {
            this.connect();
            Statement stmt = null;
            String query = "INSERT INTO products (id, code, name, pricehrk, priceeur, description, isAvailable) VALUES (1, '"+product.getCode()+"', '"+product.getName()+"', '"+product.getPricehrk()+"', '"+product.getPriceeur()+"', '"+product.getDescription()+"', "+product.getIsAvailable()+")";

            stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            this.disconnect();
        } catch (Exception e) {
        }
    }

        public int ifselectmaxidisnull() {
        try {
            this.connect();
            Statement stmt = null;
            String query = "SELECT MAX(id) FROM products";

            stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            int maxid=rs.getInt("max");
            this.disconnect();
            return maxid;
        } catch (Exception e) {
            return 0;
        }
    }

    public void updateproduct(Product product, int Productindex) {
        try {
            this.connect();
            Statement stmt = null;
            String query = "UPDATE products SET code='"+product.getCode()+"', name='"+product.getName()+"', pricehrk='"+product.getPricehrk()+"', priceeur='"+product.getPriceeur()+"', description='"+product.getDescription()+"', isAvailable="+product.getIsAvailable()+" where id="+Productindex;

            stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            this.disconnect();
        } catch (Exception e) {
        }
    }

	public void deleteproduct(int Productindex) {
        try {
            this.connect();
            Statement stmt = null;
            String query = "DELETE FROM products WHERE id="+Productindex;

            stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            this.disconnect();
        } catch (Exception e) {
        }
    }

    public int getproductid(Product _product) {
        try {
            this.connect();
            Statement stmt = null;
            String query = "SELECT * FROM products where code='"+_product.getCode()+"' AND name='"+_product.getName()+"' AND pricehrk='"+_product.getPricehrk()+"' AND priceeur='"+_product.getPriceeur()+"' AND description='"+_product.getDescription()+"' and isAvailable="+_product.getIsAvailable();

            stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
           
            rs.next(); 
 
            int r = rs.getInt("id");
           
	    this.disconnect();
	    return r;
        } catch (Exception e) {
            //e.printStackTrace(System.out);
	    return 0;
        }
    }

    public void createtableifnotexists() {
        try {
            this.connect();
            Statement stmt = null;
            String query = "CREATE TABLE IF NOT EXISTS products (id SERIAL PRIMARY KEY, code VARCHAR(255) NOT NULL, name VARCHAR(255) NOT NULL, pricehrk VARCHAR(255) NOT NULL, priceeur VARCHAR(255) NOT NULL, description VARCHAR(255) NOT NULL, isAvailable CHECK)";

            stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
           
	    this.disconnect();
        } catch (Exception e) {
            //e.printStackTrace(System.out);
        }
    }

    public Product getproductbyid(int index) {
        try {
            this.connect();
            Statement stmt = null;
            String query = "SELECT * FROM products where id="+index;

            stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
           
            rs.next(); 

            Product getProduct = new Product();
 
            getProduct.setId((long)rs.getInt("id"));
            getProduct.setCode(rs.getString("code"));
            getProduct.setName(rs.getString("name"));
            getProduct.setPricehrk(rs.getString("pricehrk"));
            getProduct.setPriceeur(rs.getString("priceeur"));
            getProduct.setDescription(rs.getString("description"));
            getProduct.setIsAvailable(rs.getBoolean("isAvailable"));
           
	    this.disconnect();
	    return getProduct;
        } catch (Exception e) {
            //e.printStackTrace(System.out);
	    return null;
        }
    }

    public List<Product> getallproducts() {
        try {
            this.connect();
            Statement stmt = null;
            String query = "SELECT * FROM products";

            stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
	    List<Product> products = new ArrayList<Product>();
           
            while (rs.next()) {

            Product getProduct = new Product();
 
            getProduct.setId((long)rs.getInt("id"));
            getProduct.setCode(rs.getString("code"));
            getProduct.setName(rs.getString("name"));
            getProduct.setPricehrk(rs.getString("pricehrk"));
            getProduct.setPriceeur(rs.getString("priceeur"));
            getProduct.setDescription(rs.getString("description"));
            getProduct.setIsAvailable(rs.getBoolean("isAvailable"));
            products.add(getProduct);
            }
           
	    this.disconnect();
	    return products;
        } catch (Exception e) {
            //e.printStackTrace(System.out);
	    return null;
        }
    }

    public String getResults(String sqlQuery) {
        try {
            String result = "";
            this.connect();
            Statement stmt = null;
            stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            ResultSetMetaData rsMeta = rs.getMetaData();
            int count = rsMeta.getColumnCount();
            int i, j = 1;
            result += "\n| ";
            while (j <= count) {
                String format = "%1$-" + rsMeta.getColumnDisplaySize(j) + "s";
                String formatedValue = String.format(format, rsMeta.getColumnLabel(j));
                result += formatedValue + "| ";
                j++;
            }
            result += "\n" + new String(new char[result.length()]).replace("\0", "-");
            while (rs.next()) {
                i = 1;
                result += "\n| ";
                while (i <= count) {
                    String format = "%1$-" + rsMeta.getColumnDisplaySize(i) + "s";
                    String formatedValue = String.format(format, new String(rs.getBytes(i), StandardCharsets.UTF_8));
                    result += formatedValue + "| ";
                    i++;
                }
            }
            this.disconnect();
            return result;
        } catch (Exception e) {
            e.printStackTrace(System.out);
            return "";
        }
    }

    private void connect() throws Exception {
        Class.forName("org.postgresql.Driver");
        this.connection = null;
        this.connection = DriverManager.getConnection(
                "jdbc:postgresql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
        //Class.forName("com.mysql.cj.jdbc.Driver");
        //this.connection = null;
        //this.connection = DriverManager.getConnection(
        //        "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
        //    "jdbc:mysql://databasetesta.c7kpkrn9gjce.us-east-1.rds.amazonaws.com/database_test?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", this.username, this.password);
    }

    private void disconnect() throws Exception {
        if (this.connection != null) {
            this.connection.close();
            this.connection = null;
        }
    }

}