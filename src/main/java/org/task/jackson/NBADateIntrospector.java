package org.task.jackson;

import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;
import org.task.model.NBADate;


public class NBADateIntrospector extends NopAnnotationIntrospector {
    @Override
    public Object findDeserializer(final Annotated annotated) {
        if (annotated.getAnnotation(NBADate.class) == null) {
            return null;
        } else {
            return NBADateDeserializer.class;
        }
    }
}