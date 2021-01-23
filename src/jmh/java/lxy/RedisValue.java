package lxy;

import lombok.Builder;
import lombok.ToString;

import java.io.Serializable;

@Builder
@ToString
public class RedisValue implements Serializable {
    String c;
    String d;
}
