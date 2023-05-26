package imat;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ShoppingItem;

public class ProductCard extends AnchorPane {
    @FXML
    Label productName;
    @FXML
    Label productPrice;
    @FXML
    Label productCount;
    @FXML
    ImageView productImage;
    @FXML
    ImageView imageFavorite;

    MainViewController parentController;
    Product product;
    Boolean favorite = false;
    Double count = 0d;

    public ProductCard(Product product, MainViewController parentController) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("product_card_new.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.product = product;
        this.parentController = parentController;
        if (this.parentController.iMatDataHandler.favorites().contains(this.product)) {
            favorite = true;
        }
        if (favorite) {
            Image image;
            image = new Image(getClass().getResource("resources/like_filled.png").toString());
            imageFavorite.setImage(image);
        } else {
            Image image;
            image = new Image(getClass().getResource("resources/like_hollow.png").toString());
            imageFavorite.setImage(image);
        }
        this.productName.setText(product.getName());
        this.productPrice.setText(product.getPrice() + " kr");
        this.productImage.setImage(parentController.iMatDataHandler.getFXImage(product));

        update();
        this.parentController.iMatDataHandler.getShoppingCart().addShoppingCartListener(evt -> {
            update();
        });
    }

    public void update() {
        for (ShoppingItem sItem : parentController.iMatDataHandler.getShoppingCart().getItems()) {
            if (sItem.getProduct() == product) {
                count = sItem.getAmount();
            }
        }
        this.productCount.setText(((Integer) count.intValue()).toString());
    }

    @FXML
    public void addCurrentToCart() {
        parentController.iMatDataHandler.getShoppingCart().addProduct(product);
        update();
    }

    @FXML
    public void removeCurrentFromCart() {
        for (ShoppingItem sItem : parentController.iMatDataHandler.getShoppingCart().getItems()) {
            if (sItem.getProduct() == product) {
                sItem.setAmount(sItem.getAmount() - 1);
                parentController.iMatDataHandler.getShoppingCart().fireShoppingCartChanged(sItem, isCache());
            }
            if (sItem.getAmount() == 0) {
                parentController.iMatDataHandler.getShoppingCart().removeProduct(product);
            }
        }
        update();
    }

    @FXML
    public void toggleFavorite() {
        if (!favorite) {
            parentController.iMatDataHandler.addFavorite(product);
        } else {
            System.out.println("PING");
            parentController.iMatDataHandler.removeFavorite(product);
        }
        favorite = !favorite;
        if (favorite) {
            Image image;
            image = new Image(getClass().getResource("resources/like_filled.png").toString());
            imageFavorite.setImage(image);
        } else {
            Image image;
            image = new Image(getClass().getResource("resources/like_hollow.png").toString());
            imageFavorite.setImage(image);
        }
    }
}
