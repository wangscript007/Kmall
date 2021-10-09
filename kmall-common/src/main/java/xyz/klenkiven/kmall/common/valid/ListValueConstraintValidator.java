package xyz.klenkiven.kmall.common.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ListValueConstraintValidator implements ConstraintValidator<ListValue, Integer> {

    private final List<Integer> valueList = new ArrayList<>();

    @Override
    public void initialize(ListValue constraintAnnotation) {
        Arrays.stream(constraintAnnotation.values())
                .forEach(valueList::add);
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return valueList.contains(value);
    }
}
