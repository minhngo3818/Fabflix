package data.sales;

import java.util.Date;

public class Sales {
    private Integer id;

    private String movieId;

    private String movieTitle;

    private Integer quantity;

    private Double unitPrice;

    private Date salesDate;

    public Sales(Integer id,
                 String movieId,
                 String movieTitle,
                 Integer quantity,
                 Double unitPrice,
                 Date salesDate) {
        this.id = id;
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.salesDate = salesDate;
    }

    public Integer getId() { return id; }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public Date getSalesDate() {
        return salesDate;
    }

    public Integer getQuantity() {return quantity;}

    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Double getUnitPrice() { return unitPrice; }

    public void setUnitPrice(Double unitPrice) { this.unitPrice = unitPrice; }

    public void setSalesDate(Date salesDate) {
        this.salesDate = salesDate;
    }
}
