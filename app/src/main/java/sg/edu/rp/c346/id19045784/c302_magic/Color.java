package sg.edu.rp.c346.id19045784.c302_magic;

public class Color {

    private String id;
    private String colorName;

    public Color(String id, String colorName) {
        this.id = id;
        this.colorName = colorName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    @Override
    public String toString() {
        return colorName;
    }
}
