package trendravel.photoravel_be.db.enums;


import lombok.Getter;


public enum Category {

    None("카테고리 없음"),
    FIRST("\uD83D\uDD25 8월의 인기장소"),
    SECOND(" ⛱\uFE0F 여유로운 여행지"),
    THIRD("\uD83C\uDF0A 액티비티 여행지"),
    FOURTH("\uD83D\uDCF1 인스타 속 그 장소!");

    private final String message;

    Category(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
