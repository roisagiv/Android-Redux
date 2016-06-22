package io.github.roisagiv.android.redux.examples.shoppingcart;

import android.os.Parcel;
import android.os.Parcelable;
import io.github.roisagiv.android.redux.State;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCartState implements State, Parcelable {

  public static final Parcelable.Creator<ShoppingCartState> CREATOR =
      new Parcelable.Creator<ShoppingCartState>() {
        @Override public ShoppingCartState createFromParcel(Parcel source) {
          return new ShoppingCartState(source);
        }

        @Override public ShoppingCartState[] newArray(int size) {
          return new ShoppingCartState[size];
        }
      };

  private final Products products;
  private final Cart cart;

  public ShoppingCartState() {
    products = new Products();
    cart = new Cart();
  }

  protected ShoppingCartState(Parcel in) {
    this.products = in.readParcelable(Products.class.getClassLoader());
    this.cart = in.readParcelable(Cart.class.getClassLoader());
  }

  public Cart getCart() {
    return cart;
  }

  public Products getProducts() {
    return products;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(this.products, flags);
    dest.writeParcelable(this.cart, flags);
  }

  public static class Product implements Parcelable {

    public static final Creator<Product> CREATOR = new Creator<Product>() {
      @Override public Product createFromParcel(Parcel source) {
        return new Product(source);
      }

      @Override public Product[] newArray(int size) {
        return new Product[size];
      }
    };

    private int id;
    private String title;
    private double price;
    private int inventory;

    public Product() {
    }

    public Product(int id, String title, double price, int inventory) {
      this.id = id;
      this.title = title;
      this.price = price;
      this.inventory = inventory;
    }

    protected Product(Parcel in) {
      this.id = in.readInt();
      this.title = in.readString();
      this.price = in.readDouble();
      this.inventory = in.readInt();
    }

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public int getInventory() {
      return inventory;
    }

    public void setInventory(int inventory) {
      this.inventory = inventory;
    }

    public double getPrice() {
      return price;
    }

    public void setPrice(double price) {
      this.price = price;
    }

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    @Override public int describeContents() {
      return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
      dest.writeInt(this.id);
      dest.writeString(this.title);
      dest.writeDouble(this.price);
      dest.writeInt(this.inventory);
    }
  }

  public static class Products implements Parcelable {

    public static final Creator<Products> CREATOR = new Creator<Products>() {
      @Override public Products createFromParcel(Parcel source) {
        return new Products(source);
      }

      @Override public Products[] newArray(int size) {
        return new Products[size];
      }
    };

    private final Map<Integer, Product> byId;
    private final List<Integer> visibleIds;

    public Products() {
      visibleIds = new ArrayList<>();
      byId = new LinkedHashMap<>();
    }

    protected Products(Parcel in) {
      int byIdSize = in.readInt();
      this.byId = new LinkedHashMap<>(byIdSize);
      for (int i = 0; i < byIdSize; i++) {
        Integer key = (Integer) in.readValue(Integer.class.getClassLoader());
        Product value = in.readParcelable(Product.class.getClassLoader());
        this.byId.put(key, value);
      }
      this.visibleIds = new ArrayList<>();
      in.readList(this.visibleIds, Integer.class.getClassLoader());
    }

    public Map<Integer, Product> getById() {
      return byId;
    }

    public List<Integer> getVisibleIds() {
      return visibleIds;
    }

    @Override public int describeContents() {
      return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
      dest.writeInt(this.byId.size());
      for (Map.Entry<Integer, Product> entry : this.byId.entrySet()) {
        dest.writeValue(entry.getKey());
        dest.writeParcelable(entry.getValue(), flags);
      }
      dest.writeList(this.visibleIds);
    }
  }

  public static class Cart implements Parcelable {

    public static final Creator<Cart> CREATOR = new Creator<Cart>() {
      @Override public Cart createFromParcel(Parcel source) {
        return new Cart(source);
      }

      @Override public Cart[] newArray(int size) {
        return new Cart[size];
      }
    };

    private final List<Integer> addedIds;
    private final Map<Integer, Integer> quantityById;

    public Cart() {
      addedIds = new ArrayList<>();
      quantityById = new LinkedHashMap<>();
    }

    protected Cart(Parcel in) {
      this.addedIds = new ArrayList<>();
      in.readList(this.addedIds, Integer.class.getClassLoader());
      int quantityByIdSize = in.readInt();
      this.quantityById = new LinkedHashMap<>(quantityByIdSize);
      for (int i = 0; i < quantityByIdSize; i++) {
        Integer key = (Integer) in.readValue(Integer.class.getClassLoader());
        Integer value = (Integer) in.readValue(Integer.class.getClassLoader());
        this.quantityById.put(key, value);
      }
    }

    public List<Integer> getAddedIds() {
      return addedIds;
    }

    public Map<Integer, Integer> getQuantityById() {
      return quantityById;
    }

    @Override public int describeContents() {
      return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
      dest.writeList(this.addedIds);
      dest.writeInt(this.quantityById.size());
      for (Map.Entry<Integer, Integer> entry : this.quantityById.entrySet()) {
        dest.writeValue(entry.getKey());
        dest.writeValue(entry.getValue());
      }
    }
  }
}
