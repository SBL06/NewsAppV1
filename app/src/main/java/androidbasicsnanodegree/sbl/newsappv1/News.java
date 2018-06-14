package androidbasicsnanodegree.sbl.newsappv1;

public class News {

    private String Title;
    private String Section;
    private String NewsUrl;
    private String Description;
    private String ImgUrl;
    private String Author;
    private String Date;

    public News(String title, String section, String newsUrl, String description, String imgUrl, String author, String date) {
        Title = title;
        Section = section;
        NewsUrl = newsUrl;
        Description = description;
        ImgUrl = imgUrl;
        Author = author;

        if (date.contains("T")) {
            Date = date.substring(0, date.indexOf("T"));
        } else {
            Date = date;
        }
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getSection() {
        return Section;
    }

    public void setSection(String section) {
        Section = section;
    }

    public String getNewsUrl() {
        return NewsUrl;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImgUrl() {
        return ImgUrl;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date.substring(0, date.indexOf("T"));
    }

    @Override
    public String toString() {
        return "News{" +
                "Title='" + Title + '\'' +
                ", Section='" + Section + '\'' +
                ", NewsUrl='" + NewsUrl + '\'' +
                ", Description='" + Description + '\'' +
                ", ImgUrl='" + ImgUrl + '\'' +
                ", Author='" + Author + '\'' +
                ", Date='" + Date + '\'' +
                '}';
    }
}
