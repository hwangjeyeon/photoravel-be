package trendravel.photoravel_be.db.review.enums;



public enum ReviewTypes {
    LOCATION("관광장소", "1"),
    SPOT("사진스팟","2"),
    PHOTOGRAPHER("사진작가", "3");

    private final String title;
    private final String typeCode;

    ReviewTypes(String title, String typeCode) {
        this.title = title;
        this.typeCode = typeCode;
    }
}
