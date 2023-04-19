package com.github.framework.core.mapper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.framework.core.lang.Numbers;

import java.io.IOException;
import java.util.Objects;

/**
 * 
 * @ClassName: LongJsonSerializer 
 * @Description:
 *
 *
 */
public class LongJsonSerializer extends StdSerializer<Long> {

    /** 
     * @Fields serialVersionUID : 
     */
    private static final long serialVersionUID = -3947284898943302963L;

    public LongJsonSerializer() {
        super(Long.class);
    }

    @Override
    public void serialize(final Long value, final JsonGenerator gen, final SerializerProvider provider)
            throws IOException {
        if (Objects.isNull(value)) {
            gen.writeString(Numbers.LONG_ZERO.toString());
        } else {
            gen.writeString(String.valueOf(value));
        }
    }

}
