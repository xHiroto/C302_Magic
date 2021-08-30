package sg.edu.rp.c346.id19045784.c302_magic;

public class ColorItem {

    private String id;
    private String cardName;
    private String colorId;
    private String typeId;
    private String price;
    private String quantity;

    public ColorItem(String id, String cardName, String colorId, String typeId, String price, String quantity) {
        this.id = id;
        this.cardName = cardName;
        this.colorId = colorId;
        this.typeId = typeId;
        this.price = price;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getColorId() {
        return colorId;
    }

    public void setColorId(String colorId) {
        this.colorId = colorId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return cardName + " $" + String.format("%.2f", Double.parseDouble(price)) ;
    }
}
