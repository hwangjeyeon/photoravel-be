package trendravel.photoravel_be.entity.enums;

public enum ReviewTypes {

    LOCATION("관광장소", "1"), SPOT("사진스팟","2"), GUIDE("가이드", "3");

    private final String title;
    private final String typeCode;

    ReviewTypes(String title, String typeCode) {
        this.title = title;
        this.typeCode = typeCode;
    }
}
