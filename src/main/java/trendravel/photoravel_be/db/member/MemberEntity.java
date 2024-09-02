package trendravel.photoravel_be.db.member;

import jakarta.persistence.*;
import lombok.*;
import trendravel.photoravel_be.db.BaseEntity;
import trendravel.photoravel_be.db.match.Matching;

@Entity
@Table(name = "member")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false, unique = true)
    private String memberId;
    @Column(length = 255, nullable = false)
    private String password;
    @Column(length = 50, nullable = false)
    private String name;
    @Column(length = 50, nullable = false, unique = true)
    private String nickname;
    @Column(length = 50, nullable = false, unique = true)
    private String email;
    @Column(length = 255, nullable = false)
    private String profileImg;

    public MemberEntity(String memberId, String password, String name, String nickname, String email, String profileImg) {
        this.memberId = memberId;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.profileImg = profileImg;
    }

}
