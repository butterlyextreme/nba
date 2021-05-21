package org.task.model;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Payload;

/**
 * Helper validation annotation to mark field as IBAN for validation and normalization purposes.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NBADate {
    /**
     * Defines the message that will be shown when the input data is not valid.
     *
     * @return error message.
     */
    String message() default "";

    /**
     * Lets the developer select to split the annotations into different groups to apply different validations to
     * each group.
     *
     * @return groups.
     */
    Class<?>[] groups() default {};

    /**
     * Payloads are typically used to carry metadata information consumed by a validation client.
     *
     * @return payloads.
     */
    Class<? extends Payload>[] payload() default {};
}