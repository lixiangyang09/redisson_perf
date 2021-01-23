package lxy;

import lombok.Builder;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Builder
@EqualsAndHashCode
public class RedisKey implements Serializable {
    String a;
    String b;
}
