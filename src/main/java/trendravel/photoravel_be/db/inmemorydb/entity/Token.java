package trendravel.photoravel_be.db.inmemorydb.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "token", timeToLive = 12)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Token {

    @Id
    private String id;
    private String refreshToken;
}
